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
    private static final String CLASS_TO_EXCLUDES = "radar.performance.excludes";
    public static final String PERFORMANCE_ACTIVE = "radar.performance.active";

    List<Pattern> classToInstrument = new ArrayList<Pattern>();
    List<Pattern> classToExclude = new ArrayList<Pattern>();

    public PerformanceTransformer() {
        String classToInstrument = System.getProperty(CLASS_TO_TRANSFORM);
        String classToExcludes = System.getProperty(CLASS_TO_EXCLUDES);

        if (classToInstrument != null) {
            String[] arrPattern = classToInstrument.split(";");
            for (String strPattern : arrPattern) {
                Pattern pattern = Pattern.compile(strPattern);
                this.classToInstrument.add(pattern);
            }
        }

        if (classToExcludes != null) {
            String[] arrPattern = classToExcludes.split(";");
            for (String strPattern : arrPattern) {
                Pattern pattern = Pattern.compile(strPattern);
                this.classToExclude.add(pattern);
            }
        }
    }

    private boolean match(String className) {
        boolean match = false;
        if (classToInstrument == null || classToInstrument.size() == 0) {
            return true;
        }
        for (Pattern pattern : classToInstrument) {
            match = pattern.matcher(className).matches();
            if (match)
                return true;
        }
        return false;
    }

    private boolean exluded(String className) {
        boolean match = false;
        if (classToExclude == null || classToExclude.size() == 0) {
            return true;
        }
        for (Pattern pattern : classToExclude) {
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
        try {

            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            if (match(className) && !ctClass.isAnnotation() && !ctClass.isEnum() && !ctClass.isInterface() && !exluded(className)) {

                CtMethod[] methods = ctClass.getDeclaredMethods();
                for (CtMethod method : methods) {
                    if (method.getModifiers() != Modifier.ABSTRACT && method.getModifiers() != Modifier.NATIVE) {
                        instrumentVoidMethod(ctClass, method);
                        System.out.println(className + " instrumented");
                    }
                }
                byteCode = ctClass.toBytecode();
                ctClass.detach();
            }
        } catch (Throwable ex) {
            System.out.println("Exception to instrument : " + className);
            ex.printStackTrace();
        }
        // }
        return byteCode;
    }

    private void instrumentVoidMethod(CtClass ctClass, CtMethod method) throws CannotCompileException, NotFoundException {
        //--Clone method
        CtMethod ctNewMethod = CtNewMethod.copy(method, ctClass, null);

        //--prepare new name
        String mName = method.getName();
        mName += "Instr";

        String instrBody = "{ " +
                "long start = System.currentTimeMillis();" +
                "String _logPref = \"" + Thread.currentThread().getName()+":" + method.getLongName() + " : \";" +
                "try{";

        //--prepare calling method
        MethodInfo methodInfo = method.getMethodInfo();
        int numberOfParam = method.getParameterTypes().length;
        if (!method.getReturnType().equals(CtClass.voidType)) {
            CtClass returnType = method.getReturnType();
            String strReturnType = returnType.getName();
            instrBody += strReturnType + " _insResult = ";
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

        if (!method.getReturnType().equals(CtClass.voidType)) {
            instrBody += "return _insResult;";
        }
        instrBody += "}";

        instrBody += "catch(Throwable _th){";
        instrBody += "throw _th;";
        instrBody += "}";
        instrBody += "finally{ " +
                "System.out.println(_logPref + (System.currentTimeMillis() - start)+\"ms\");" +
                "}";
        instrBody += "}";
        ctNewMethod.setName(mName);
        ctClass.addMethod(ctNewMethod);
        method.setBody(null);
        method.insertBefore(instrBody);
    }
}
