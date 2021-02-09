package com.zyrox.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.zyrox.Server;

public class HttpUtils {

	public static void visit(String link, int timeout) {
		
		Server.getHttpThread().submit(new Runnable() {
			@Override
			public void run() {
				try {
					/*BufferedWriter out = new BufferedWriter(new FileWriter("./data/newcomerslog.log",true));
					out.write(link);
					out.newLine();
					out.close();*/
					//System.out.println(link);
					URL url = new URL(link);
					if(link.startsWith("http://")) {
						//System.out.println("Started reading http link");
						HttpURLConnection http = (HttpURLConnection) url.openConnection();
						http.setReadTimeout(timeout);
						http.setConnectTimeout(timeout);
						//System.out.println("Responsecode: " + http.getResponseCode());
						InputStreamReader isr = new InputStreamReader(http.getInputStream());
						BufferedReader in = new BufferedReader(isr);
						String line;
						while((line = in.readLine()) != null) {
							//System.out.println(line);
						}
						while(isr.read() != -1);
					} else if(link.startsWith("https://")){
						HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
						http.setReadTimeout(timeout);
						http.setConnectTimeout(timeout);
						InputStreamReader isr = new InputStreamReader(http.getInputStream());
						while(isr.read() != -1);
					}
				} catch (Exception e) {
					System.out.println("Issue reading: " + link);
					e.printStackTrace();

				}
			}
		});
		
	}
}
