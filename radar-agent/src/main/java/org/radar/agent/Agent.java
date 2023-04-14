package org.radar.agent;

import org.radar.agent.transformer.PerformanceTransformer;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println("-----------------Starting Radar Instrumentation---------------------");
        System.out.println("--------------------------------------------------------------------");
        if ("true".equals(System.getProperty(PerformanceTransformer.PERFORMANCE_ACTIVE))){
            System.out.println("Performance Plugin is active");
            inst.addTransformer(new PerformanceTransformer());
        }
        System.out.println("---------------------------------------------------------------");
        System.out.println("-----------------End Radar Instrumentation---------------------");
        System.out.println("---------------------------------------------------------------");
    }


}