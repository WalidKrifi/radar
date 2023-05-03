package org.radar.agent;

import org.radar.agent.performance.DetectLeakPerformance;

public class Main {
    public static void main(String[] args) {
        try{
            org.radar.agent.logging.LogManager.incrementDepth();
            DetectLeakPerformance.doConnectWith3Param(1,2,3);
            DetectLeakPerformance.doConnectWithoutParam();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}