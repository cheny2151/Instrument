package com.cheney.instrument.transformer;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author cheney
 * @date 2020-03-25
 */
public class LogOutTransformer implements ClassFileTransformer {

    private final static String TARGET_CLASS = "com/cheney/controller/PageCommonController";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (TARGET_CLASS.equals(className)) {
            System.out.println("find target class:" + className);
            try {
                ClassPool pool = ClassPool.getDefault();
                ClassClassPath classPath = new ClassClassPath(this.getClass());
                pool.insertClassPath(classPath);
                CtClass cc = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
                ClassLoader classLoader = pool.getClassLoader();
                CtBehavior[] declaredMethods = cc.getDeclaredBehaviors();
                System.out.println("frozen:" + cc.isFrozen());
                System.out.println("ClassLoader:" + loader);
                System.out.println("pool classLoader:" + classLoader);
                /*Class<?> targetClass = cc.toClass();
                ProxyFactory proxyFactory = new ProxyFactory();
                proxyFactory.setSuperclass(targetClass);
                proxyFactory.setFilter(method -> "test".equals(method.getName()));
                proxyFactory.create(null, null,
                        (self, method, proceed, args) -> {
                            Object result = proceed.invoke(self, args);
                            System.out.println("执行结果:" + JSON.toJSONString(result));
                            return result;
                        });
                CtMethod test = cc.getDeclaredMethod("test");
                test.insertBefore("System.out.println(\"javassist\");");*/
                for (CtBehavior behavior : declaredMethods) {
                    System.out.println(behavior.getName());
                    if ("test".equals(behavior.getName())) {
                        behavior.insertBefore("{" +
                                "java.math.BigDecimal a = new java.math.BigDecimal(1);" +
                                "$0.test2();" +
                                "System.out.println(a);" +
                                "}");
                    }
                }
                return cc.toBytecode();
            } catch (Exception e) {
                System.out.println("代理异常:" + e.getMessage());
                e.printStackTrace();
                return classfileBuffer;
            }
        }
        return classfileBuffer;
    }

}
