package com.t2a.test.performance;

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


    public static void doConnectWithoutParam(int var1,int var2){
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL("http://xxxx");
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

}
