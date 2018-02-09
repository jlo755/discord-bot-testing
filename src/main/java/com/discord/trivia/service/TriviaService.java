package com.discord.trivia.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TriviaService {
	public HashMap<String, String> getCategories() throws IOException{

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
}
