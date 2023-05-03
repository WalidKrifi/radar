package org.radar.agent.logging;

import java.util.ArrayList;

public class LogManager {
    private static final ThreadLocal<Integer> depth = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue()
        {
            return Integer.valueOf(0);
        }
    };
    private static final Integer ZERO = Integer.valueOf(0);
    private static final ThreadLocal<ArrayList<String>> stackTrace = new ThreadLocal<ArrayList<String>>(){
        @Override
        protected ArrayList<String> initialValue()
        {
            return new ArrayList<String>();
        }
    };

    private static Integer getDepth() {
        return depth.get();
    }

    public static void incrementDepth() {
        depth.set(depth.get() + 1);
    }
    private static void decrementDepth() {
        depth.set(depth.get() - 1);
    }

    public static void logPerformance(String methodName,long duration){

        if(ZERO.compareTo(getDepth()) == 0){
            printThreadInfo();
           for(int i = 0;i < stackTrace.get().size();i++){
             String stack =  stackTrace.get().get(i);
             for(int j=0;j<i;j++){
                 stack = "+" + stack;
             }
               logLine(stack);
             decrementDepth();
           }
        }else{
            stackTrace.get().add(methodName +" : "+ duration +" ms");
        }
    }
    private static void logLine(String line){
        System.out.println(line);
    }
    private static void printThreadInfo(){
        String line = Thread.currentThread().getName() + " , priority: " +Thread.currentThread().getPriority() +" , State : "+Thread.currentThread().getState();
        logLine(line);
    }
}
