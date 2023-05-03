package org.radar.agent;

import org.junit.Test;
import org.radar.agent.performance.DetectLeakPerformance;

import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class MatchClassTest {
   @Test
    public void match(){
       boolean found = Pattern.compile("com/t2a/test/.*") .matcher("com/t2a/test/performance/DetectLeakPerformance").matches();
       assertTrue(found);
    }
    @Test
    public void doConnectWith2Param(){
        DetectLeakPerformance.doConnectWith2Param(1,2);
    }
    @Test
    public void doConnectWithoutParam(){
        DetectLeakPerformance.doConnectWithoutParam();
    }
    public void doConnectWith3Param(){
        DetectLeakPerformance.doConnectWith3Param(1,2,3);
    }

}
