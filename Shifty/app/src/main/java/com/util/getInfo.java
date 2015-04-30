package com.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import edu.csci.shiftyencryption.ShiftyCipher;


public class getInfo {
    public static DataPOJO currentPojo = new DataPOJO();
    public static String employeeID;
    public static String employeeName;
    private String returnValue = "";
    public static String secretQuestion = "";
    public static String secretAnswer = "";


    public static String postToServer(Type type, Action action, String json) {
        String line = "";

        try {
            ShiftyCipher ciph = new ShiftyCipher();
            String urlParameters = Com.SECRETKEY.getProtocol() + "=" + Com.CONNECTIONSECRET.getProtocol();
            urlParameters += "&" + Com.CHOOSEACTION.getProtocol() + "=" + type.getActions().get(action);
            urlParameters += "&" + Com.JSONDATA.getProtocol() + "=" + json;
            urlParameters = ciph.encrypt(urlParameters);
            //urlParameters = "";
            byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
            int postDataLength = postData.length;
            String request = "http://timeslip.ddrcweb.com/licenseServer";
            URL url = new URL(request);
            HttpURLConnection cox = (HttpURLConnection) url.openConnection();
            cox.setDoOutput(true);
            cox.setDoInput(true);
            cox.setInstanceFollowRedirects(false);
            cox.setRequestMethod("POST");
            cox.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            cox.setRequestProperty("charset", "utf-8");
            cox.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            cox.setUseCaches(false);
            try {
                DataOutputStream wr = new DataOutputStream(cox.getOutputStream());
                wr.write(postData);
                wr.close();
            }catch(IOException e){
                e.printStackTrace(System.err);
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(cox.getInputStream()));
                String tempLine;
                while ((tempLine = reader.readLine()) != null) {
                    line += tempLine;
                }
                reader.close();

                line = ciph.decrypt(line);
            } catch (Throwable t) {
                t.printStackTrace(System.err);
                return "unable to connect";
            }
        } catch (Throwable t) {

            t.printStackTrace(System.err);
            return  "unable to connect";
        }
        return  line;
    }

    public static void refreshData(){
        //Refreshing the data since we just modified it, we'd expect to see it.
        Gson gson = new Gson();
        currentPojo.setEmployeeID(Integer.valueOf(employeeID));
        getInfo.currentPojo = gson.fromJson(getInfo.postToServer(Type.SHIFTY, Action.GETALLDATA, gson.toJson(currentPojo, DataPOJO.class)), DataPOJO.class);
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

}
