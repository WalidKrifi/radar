package com.t2a.test.transformer;

import javassist.*;
import javassist.bytecode.MethodInfo;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Adding performance log for only :
 * - given  class
 * - instrument non abstract and non native class
 * - instrument a void method
 */
public class PerformanceTransformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;

        //Add instrumentation to class
      //  if (!className.startsWith("java") && !className.startsWith("com/intellij")&& !className.startsWith("jdk")&&!className.startsWith("sun/nio")) {
        if (className.equals("com/t2a/test/performance/DetectLeakPerformance")) {
            try {
                ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod[] methods = ctClass.getDeclaredMethods();
                for (CtMethod method : methods) {
                    if (method.getModifiers() != Modifier.ABSTRACT && method.getModifiers() != Modifier.NATIVE) {
                        if (method.getReturnType().equals(CtClass.voidType))
                            instrumentVoidMethod(ctClass, method);
                    }
                }
                byteCode = ctClass.toBytecode();
                ctClass.detach();
            } catch (Throwable ex) {
                System.out.println("Exception to instrument : " +  className );
                ex.printStackTrace();
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
       // instrBody+="String _logPref = \""+mName+"\";";
        //--prepare calling methode
        MethodInfo methodInfo = method.getMethodInfo();
        int numberOfParam = method.getParameterTypes().length;
        instrBody += mName + "(";
        for (int i = 0; i < numberOfParam; i++) {
            if (i == numberOfParam - 1)
                instrBody += "$" + (i + 1) ;
            else
                instrBody += "$" + (i + 1) + ",";
        }
        instrBody += ");";
        instrBody += "System.out.println(System.currentTimeMillis() - start);";
        instrBody += "}";
        ctNewMethod.setName(mName);
        ctClass.addMethod(ctNewMethod);
        method.setBody(null);
        method.insertBefore(instrBody);
    }
}
