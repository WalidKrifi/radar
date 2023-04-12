package com.t2a.test;

import com.t2a.test.transformer.PerformanceTransformer;

import java.lang.instrument.Instrumentation;

public class MonitorPerformanceAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("----------------------------------------------------");
        System.out.println("-----------------starting agent---------------------");
        System.out.println("----------------------------------------------------");
        inst.addTransformer(new PerformanceTransformer());
    }


}