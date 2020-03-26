package com.cheney.instrument.transformer;

import com.alibaba.fastjson.JSON;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.util.proxy.ProxyFactory;

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
                CtClass cc = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtBehavior[] declaredBehaviors = cc.getDeclaredBehaviors();
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
                for (CtBehavior behavior : declaredBehaviors) {
                    System.out.println(behavior.getName());
                    if ("test".equals(behavior.getName())) {
                        behavior.insertBefore("System.out.println(\"javassist\");");
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
