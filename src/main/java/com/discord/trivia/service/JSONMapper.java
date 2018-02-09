package com.discord.trivia.service;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONMapper {              
	ObjectMapper mapper; 

	public JSONMapper() {                             
		mapper = new ObjectMapper();             
	}

	public void jsonToFile(Object json, String fileName, Class jsonClass) throws JsonProcessingException, IOException, ClassNotFoundException {                              //Class<?> clazz = Class.forName(className);                              //if(clazz.isInstance(json)) {
		Object test = jsonClass.cast(json);                              
		FileWriter fw = new FileWriter(fileName);                             
		BufferedWriter bw = new BufferedWriter(fw);                              
		bw.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(test));                             
		bw.flush();                              
		bw.close();                              
		fw.close();                              //}
	}

	public boolean isArray(JsonNode node) {                              
		return node.isArray();              
	}

	public Object genericMapToObject(String json) throws JsonParseException, JsonMappingException, IOException {                              
		Object o = mapper.readValue(json, Object.class);                              
		return o;              
	}

	public ArrayList<String> findFieldValues(String jsonResponse, String field) throws JsonProcessingException, IOException{                              
		ArrayList<String> listOfValues = new ArrayList<String>();                              
		JsonNode node = mapper.readTree(jsonResponse);                              
		ArrayList<JsonNode> elements = findJSONNode(node, field); 
		for(JsonNode element:elements) { 
			listOfValues.add(element.get(field).asText());
		}                              
		return listOfValues;             
	}

	/** 
	 * Recursively iterates through the JsonTree to find the desired 
	 * element - if it's not found, an empty set is returned.                            
	 * @param node              
	 * @param field              
	 * @return              
	 * @throws JsonProcessingException              
	 * @throws IOException             
	 */              
	public ArrayList<JsonNode> findJSONNode(JsonNode node, String field) throws JsonProcessingException, IOException{
		ArrayList<JsonNode> listOfValues = new ArrayList<JsonNode>();
		if(node.isArray()) {                                              
			Iterator<JsonNode> elements = node.elements();
			while(elements.hasNext()) {         
				JsonNode element = elements.next();  
				Iterator<String> fieldNames = element.fieldNames();   
				boolean found = false;                           
				while(fieldNames.hasNext()) {      
					if(field.equals(fieldNames.next())) {    
						found = true;                            
					}            
				}                
				if(found) {      
					listOfValues.add(element); 
				} 
				else {                    
					fieldNames = element.fieldNames();      
					while(fieldNames.hasNext()) { 
						JsonNode getElement = element.get(fieldNames.next());   
						listOfValues.addAll(findJSONNode(getElement, field));
					}                                                   
				}                                            
			}                              
		}                             
		return listOfValues;           
	}

	public String printJSONResponseAsClass(String response, String nameOfResponseClass) throws JsonProcessingException, IOException { 
		JsonNode node = mapper.readTree(response);
		Queue<JsonNode> queue = new LinkedList<JsonNode>(); 
		Queue<String> namesOfNodes = new LinkedList<String>();  
		queue.add(node);  
		namesOfNodes.add(nameOfResponseClass);     
		String classString = "public ";     
		while(!queue.isEmpty()) {    
			node = queue.poll();       
			String nameOfNode = namesOfNodes.poll(); 
			classString += "class "+nameOfNode+" {\n";    
			Iterator<String> test = null;            
			if(node.isArray()) {                     
				test = node.get(0).fieldNames();     
			} else {          
				test = node.fieldNames();   
			}                           
			while(test.hasNext()) {             
				String fieldName = test.next();  
				JsonNode currentNode = null;    
				if(node.isArray()) {            
					currentNode = node.get(0).get(fieldName); 
				} else {                                    
					currentNode = node.get(fieldName);       
				}                                        
				if(currentNode.isArray()) {         
					classString += "  public "+fieldName+"[] "+fieldName+";\n";
					queue.add(currentNode);                                 
					namesOfNodes.add(fieldName);                     
				} else if(currentNode.fieldNames().hasNext()){  
					classString += "  public "+fieldName+" "+fieldName+";\n"; 
					queue.add(currentNode);                               
					namesOfNodes.add(fieldName);                   
				}  else {                                    
					classString += "  public String "+fieldName+";\n"; 
				}                                            
			} 
			classString+="}\n\n";                              }                              return classString;              }

}