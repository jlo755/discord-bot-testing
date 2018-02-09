package com.discord.trivia.service;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebService {
	protected String URL;
	protected String methodType;
	protected boolean doOutput;
	public HttpURLConnection conn;

	public WebService(String URL, String methodType, boolean doOutput) {
		this.URL = URL;
		this.methodType = methodType;
		this.doOutput = doOutput;
	}

	public WebService() {
		// TODO Auto-generated constructor stub
	}
	
	public void setHeader(String header, String value) {
		conn.setRequestProperty(header, value);
	}

	public void setContentType(String contentType) {
		conn.setRequestProperty("Content-Type", contentType);
	}

	public void startConnection() throws IOException {
		URL obj = new URL(URL);
		conn = (HttpURLConnection) obj.openConnection();
		conn.setDoOutput(doOutput);
		conn.setRequestMethod(methodType);
	}
	public void endConnection() {
		conn.disconnect();
	}

	public void sendContent(String text) throws IOException {
		OutputStream os = conn.getOutputStream();
		DataOutputStream wr = new DataOutputStream(os);
		wr.write(text.getBytes());
		System.out.println("This one?");
		wr.flush();
		wr.close();
	}

	public void sendFileContent(String text) throws IOException {
		File file = new File(text);
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		OutputStream os = conn.getOutputStream();
		DataOutputStream wr = new DataOutputStream(os);
		wr.write(fileBytes);
		wr.flush();
		wr.close();
	}

	public String retrieveContent() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		String jsonResult = "";
		while ((inputLine = in.readLine()) != null) {
			jsonResult += inputLine+"\n";
		}
		in.close();
		return jsonResult;
	}

	public URL getURL() {
		return conn.getURL();
	}

	public byte[] encodeBase64(String url) throws IOException {
		Path file = Paths.get(url);
		return Base64.getEncoder().encode(Files.readAllBytes(file));
	}
}