package org.radar.agent;

import org.radar.agent.performance.DetectLeakPerformance;

public class Main {
    public static void main(String[] args) {
        DetectLeakPerformance.doConnectWithoutParam(1,2);
        DetectLeakPerformance.doConnectWithoutParam();
    }
}