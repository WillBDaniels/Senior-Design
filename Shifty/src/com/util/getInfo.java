package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class getInfo {

	public static String sendData(List<String> data) {
		HttpURLConnection serverSocket = null;
		PrintWriter osw;
		String returnOut = "";
		String serverLocation = "http://54.68.136.126:443";
		String output;
		BufferedReader in;
		try {
			
			URL obj = new URL(serverLocation);
			serverSocket = (HttpURLConnection) obj.openConnection();
			
			serverSocket.setDoOutput(true);
			serverSocket.setDoInput(true);
			serverSocket.setRequestMethod("POST");
			osw = new PrintWriter(serverSocket.getOutputStream(), true);

			for (String line : data) {
				osw.println(line);
			}
			in = new BufferedReader(new InputStreamReader(
					serverSocket.getInputStream()));
			
			while ((output = in.readLine()) != null && (!output.equals("Done"))) {
				returnOut += output;
			}
			osw.flush();
			osw.close();
		} catch (IOException ex) {
//			returnOut = "error";
			ex.printStackTrace(System.err);
		} finally {
			if (serverSocket != null) {
				serverSocket.disconnect();
			}
		}
		return returnOut;
	}
	
	public static ArrayList<String> formatData(String returnOut) {
		String info = "";
		ArrayList<String> lay = new ArrayList<String>();
		while(returnOut.contains("&|")){
	        while(true){	
	        	returnOut = returnOut.substring(returnOut.indexOf("=\"")+2);
	        	
	        	info += returnOut.substring(0, returnOut.indexOf("\"&"));
	        	
	        	returnOut = returnOut.substring(returnOut.indexOf("\"&")+2);
	        	//returnOut = returnOut.substring(returnOut.indexOf("\"&")+2);
	        	if(returnOut.indexOf("|")==0){
	        		break;
	        	}
	        	info += "~~";
	        }
	        lay.add(info);
	    	info = "";
	       }
		return lay;
	}

}
