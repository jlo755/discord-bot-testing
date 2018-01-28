package com.discord.maven.discordbot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
	public static final String TOKEN = "Mzk4MzMxODM2MDU4ODI4ODMw.DS8_AQ.Eetp-hQZnb32yBnYFDrlTfiCbT4";
	public static final String URL = "https://discordapp.com/api/v6";

	public static void main(String[] args) throws IOException{
		String url = URL+"/gateway/bot";
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", "Bot "+TOKEN);
		conn.setRequestProperty("User-Agent", "bot-testing");
		conn.setRequestProperty("Content-Type","application/json");

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		ObjectMapper om = new ObjectMapper();
		JsonNode node = om.readTree(response.toString());
		JsonNode wssURL = node.get("url");
		System.out.println(wssURL.asText()+"/?v=6&encoding=json");

		try {
			// open websocket
			DiscordClientEndpoint clientEndPoint = new DiscordClientEndpoint(new URI(wssURL.asText()+"/?v=6&encoding=json"));

			// add listener
			clientEndPoint.addMessageHandler(new DiscordClientEndpoint.MessageHandler() {
				public void handleMessage(String message) {
					System.out.println(message);
				}
			});

			// send message to websocket
			//clientEndPoint.sendMessage("{'event':'addChannel','channel':'ok_btccny_ticker'}");

			// wait 5 seconds for messages from websocket
			// Thread.sleep(5000);

		} catch (URISyntaxException ex) {
			System.err.println("URISyntaxException exception: " + ex.getMessage());
		}
	}
}

