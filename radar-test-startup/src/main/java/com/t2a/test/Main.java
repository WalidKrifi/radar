package com.t2a.test;

import com.t2a.test.performance.DetectLeakPerformance;

public class Main {
    public static void main(String[] args) {
        DetectLeakPerformance.doConnectWithoutParam(1,2);
        DetectLeakPerformance.doConnectWithoutParam();
    }
}