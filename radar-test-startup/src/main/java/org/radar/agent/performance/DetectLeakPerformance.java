package org.radar.agent.performance;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetectLeakPerformance {
   public static void doConnectWithoutParam(){
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL("http://xxxx");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            org.radar.agent.logging.LogManager.incrementDepth();

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    public static void doConnectWith2Param(int var1,int var2){
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL("http://xxxx.xx");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");



            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public static boolean doConnectWith3Param(int var1,int var2,int var3){
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL("http://xxxx.xx");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");



            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return true;
    }
}
