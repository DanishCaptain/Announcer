package org.mendybot.announcer.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.mendybot.announcer.common.model.dto.SyncRequest;
import org.mendybot.announcer.common.model.dto.SyncResponse;
import org.mendybot.announcer.tools.InterfaceTool;

import com.google.gson.Gson;

public class EngineClient {
	private final String USER_AGENT = "Mozilla/5.0";
	private UUID uuid;
	
	public EngineClient()
	{
		uuid = UUID.randomUUID();
	}
	
	public void sendSync()
	{
		String url = "http://innovannounce.bks.net:8000/sync";
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setDoOutput(true);
			con.setDoInput(true);
			
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestMethod("POST");

//			String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
			Gson gson = new Gson();
			SyncRequest request = new SyncRequest();
			request.setUuid(uuid);
			request.setName(InetAddress.getLocalHost().getCanonicalHostName());
			request.setKey(InterfaceTool.PASS_KEY);
			
			String json = gson.toJson(request);
//			InterfaceTool.PASS_KEY
			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//			wr.writeBytes(urlParameters);
			wr.writeBytes(json);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
//			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			SyncResponse response = gson.fromJson(in, SyncResponse.class);
			System.out.println(gson.toJson(response));
			in.close();
			
			System.out.println(response.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
