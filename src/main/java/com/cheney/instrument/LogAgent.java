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
        commonMain(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        commonMain(agentArgs, inst);
        System.out.println("retransform support:" + inst.isRetransformClassesSupported());
        inst.retransformClasses(PageCommonController.class);
        System.out.println("retransform class");
    }

    private static void commonMain(String agentArgs, Instrumentation inst) throws Exception {
        inst.addTransformer(new LogOutTransformer(), true);
        System.out.println("log agent start success~");
    }

}
