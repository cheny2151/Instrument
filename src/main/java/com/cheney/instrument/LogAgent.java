package com.cheney.instrument;

import com.cheney.controller.PageCommonController;
import com.cheney.instrument.transformer.LogOutTransformer;

import java.lang.instrument.Instrumentation;

/**
 * @author cheney
 * @date 2020-03-25
 */
public class LogAgent {

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        inst.addTransformer(new LogOutTransformer(), true);
        System.out.println("log agent start success~");
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        LogOutTransformer transformer = new LogOutTransformer();
        inst.addTransformer(transformer, true);
        System.out.println("log agent start success~");
        boolean retransformClassesSupported = inst.isRetransformClassesSupported();
        System.out.println("retransform support:" + retransformClassesSupported);
        if (retransformClassesSupported) {
            inst.retransformClasses(PageCommonController.class);
        }
        inst.removeTransformer(transformer);
    }

}
