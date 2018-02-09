package com.discord.trivia.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.discord.trivia.util.Question;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TriviaService {

	public String category;
	public HashMap<String, String> categories;

	public TriviaService(){
		try {
			categories = populateCategories();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	private HashMap<String, String> populateCategories() throws IOException{
		HashMap<String,String> categories = new HashMap<String,String>();
		ObjectMapper om = new ObjectMapper();
		int page = 1;
		while(true) {
			WebService ws = new WebService("https://qriusity.com/v1/categories?page="+page, "GET", false);
			ws.startConnection();
			String s = ws.retrieveContent();
			JsonNode node = om.readTree(s);
			ArrayList<JsonNode> categoryNodes = new JSONMapper().findJSONNode(node, "name");
			if(categoryNodes.isEmpty()) {
				break;
			}
			for(JsonNode categoryNode : categoryNodes) {
				//System.out.println(node3);
				String categoryName = categoryNode.get("name").asText().replaceAll("\"", "");
				String id = categoryNode.get("id").asText().replaceAll("\"", "");
				categories.put(categoryName, id);
			}
			page++;
		}
		return categories;
	}

	public HashMap<String, String> getCategories(){
		return categories;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String generateQuestions() throws IOException{
		ObjectMapper om = new ObjectMapper();
		int rndID = (int) Math.random();
		int page = 1;
		
		String categoryId = (category.isEmpty() ? "" : categories.get(category));
		
		ArrayList<Question> questions = new ArrayList<Question>();
		
		while(true) {
			WebService ws = new WebService("https://qriusity.com/v1/categories/"+categoryId+"/questions?page="+page, "GET", false);
			ws.startConnection();
			String s = ws.retrieveContent();
			JsonNode node = om.readTree(s);
			ArrayList<JsonNode> categoryNodes = new JSONMapper().findJSONNode(node, "question");
			if(!node.isArray()) {
				break;
			}
			for(JsonNode questionNode : node) {
				Question q = om.readValue(questionNode.toString(), Question.class);
				questions.add(q);
			}
			page++;
		}
		System.out.println(questions.size());
		return "";
	}
}
