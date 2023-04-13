package org.radar.agent.transformer;

import javassist.*;
import javassist.bytecode.MethodInfo;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Adding performance log for only :
 * - given  class
 * - instrument non abstract and non native class
 * - instrument a void method
 */
public class PerformanceTransformer implements ClassFileTransformer {
    private static final String CLASS_TO_TRANSFORM = "radar.performance.classes";
    public static final String PERFORMANCE_ACTIVE = "radar.performance.active";

    List<Pattern> classToInstrument = new ArrayList<Pattern>();

    public PerformanceTransformer() {
        String classToInstrument = System.getProperty(CLASS_TO_TRANSFORM);
        if (classToInstrument != null) {
            String[] arrPattern = classToInstrument.split(";");
            for (String strPattern : arrPattern) {
                Pattern pattern = Pattern.compile(strPattern);
                this.classToInstrument.add(pattern);
            }
        }
    }

    private boolean match(String className) {
        boolean match = false;
        for (Pattern pattern : classToInstrument) {
            match = pattern.matcher(className).matches();
            if (match)
                return true;
        }
        return false;
    }

    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;

        //Add instrumentation to class
        if (!className.startsWith("java") && !className.startsWith("com/intellij") && !className.startsWith("jdk") && !className.startsWith("sun/nio")) {
            if (match(className)) {
                try {
                    ClassPool classPool = ClassPool.getDefault();
                    CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                    CtMethod[] methods = ctClass.getDeclaredMethods();
                    for (CtMethod method : methods) {
                        if (method.getModifiers() != Modifier.ABSTRACT && method.getModifiers() != Modifier.NATIVE) {
                            //if (method.getReturnType().equals(CtClass.voidType))
                                instrumentVoidMethod(ctClass, method);
                        }
                    }
                    byteCode = ctClass.toBytecode();
                    ctClass.detach();
                } catch (Throwable ex) {
                    System.out.println("Exception to instrument : " + className);
                    ex.printStackTrace();
                }
            }
        }
        return byteCode;
    }

    private void instrumentVoidMethod(CtClass ctClass, CtMethod method) throws CannotCompileException, NotFoundException {
        //--Clone method
        CtMethod ctNewMethod = CtNewMethod.copy(method, ctClass, null);

        //--prepare new name
        String mName = method.getName();
        mName += "Instr";

        String instrBody = "{long start = System.currentTimeMillis();";
        instrBody += "String _logPref = \"" + method.getLongName() + " : \";";
        //--prepare calling method
        MethodInfo methodInfo = method.getMethodInfo();
        int numberOfParam = method.getParameterTypes().length;
        if(!method.getReturnType().equals(CtClass.voidType)){
            CtClass returnType = method.getReturnType();
            String strReturnType = returnType.getName();
            instrBody +=  strReturnType + " _insResult = ";
        }
        //--calling method
        instrBody += mName + "(";
        for (int i = 0; i < numberOfParam; i++) {
            if (i == numberOfParam - 1)
                instrBody += "$" + (i + 1);
            else
                instrBody += "$" + (i + 1) + ",";
        }
        instrBody += ");";
        instrBody += "System.out.println(_logPref + (System.currentTimeMillis() - start));";
        if(!method.getReturnType().equals(CtClass.voidType)){
            instrBody += "return _insResult;";
        }
        instrBody += "}";
        ctNewMethod.setName(mName);
        ctClass.addMethod(ctNewMethod);
        method.setBody(null);
        method.insertBefore(instrBody);
    }
}
