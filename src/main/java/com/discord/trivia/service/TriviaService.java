package com.discord.trivia.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.discord.trivia.util.Question;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TriviaService {

	private String category;
	private HashMap<String, String> categories;
	private ArrayList<Question> questions;
	private int questionIndex;
	private Question currentQuestion;
	
	public TriviaService(){
		try {
			categories = populateCategories();
			questions = new ArrayList<Question>();
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

	public void startQuiz(){
		questionIndex = 0;
		nextQuestion();
	}
	
	public void incrementQuestionIndex(){
		questionIndex++;
	}
	
	public void nextQuestion(){
		currentQuestion = questions.get(questionIndex);
	}
	
	public Question getCurrentQuestion(){
		return currentQuestion;
	}
	
	public void generateQuestions() throws IOException{
		ObjectMapper om = new ObjectMapper();
		int rndID = (int) Math.random();
		int page = 1;
		
		String categoryId = (category.isEmpty() ? "" : categories.get(category));
		
		ArrayList<Question> localQuestions = new ArrayList<Question>();
		
		while(true) {
			WebService ws = new WebService("https://qriusity.com/v1/categories/"+categoryId+"/questions?page="+page, "GET", false);
			ws.startConnection();
			String s = ws.retrieveContent();
			JsonNode node = om.readTree(s);
			ArrayList<JsonNode> questionNodes = new JSONMapper().findJSONNode(node, "question");
			if(questionNodes.isEmpty()) {
				break;
			}
			for(JsonNode questionNode : questionNodes) {
				Question q = om.readValue(questionNode.toString(), Question.class);
				localQuestions.add(q);
			}
			page++;
			System.out.println(page);
		}
		System.out.println("The retrieved questions size is: "+localQuestions.size());
		int currentNoQuestions = 0;
		while(currentNoQuestions != 5 || currentNoQuestions > localQuestions.size()){
			int randomIndex = (int) Math.round(Math.random()*(localQuestions.size()-1));
			Question q = localQuestions.get(randomIndex);
			if(!this.questions.contains(q)){
				currentNoQuestions++;
				System.out.println(currentNoQuestions);
				this.questions.add(q);
			}
		}
	}
}
