package org.radar.agent;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class MatchClassTest {
   @Test
    public void match(){
       boolean found = Pattern.compile("com/t2a/test/.*") .matcher("com/t2a/test/performance/DetectLeakPerformance").matches();
       assertTrue(found);
    }
}
