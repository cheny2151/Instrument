package com.cheney.instrument;

import com.cheney.instrument.transformer.LogOutTransformer;

import java.lang.instrument.Instrumentation;

/**
 * @author cheney
 * @date 2020-03-25
 */
public class LogAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        commonMain(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        commonMain(agentArgs, inst);
    }

    private static void commonMain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new LogOutTransformer());
        System.out.println("log agent start success~");
    }

}
