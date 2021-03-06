package com.discord.maven.discordbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.discord.trivia.service.TriviaService;
import com.discord.trivia.util.Question;
import com.discord.trivia.util.State;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class TriviaListener extends ListenerAdapter{

	TriviaService ts;
	DiscordBot instance;
	
	public TriviaListener(){
		ts = new TriviaService();
		instance = DiscordBot.getInstance();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.isFromType(ChannelType.TEXT)){
			String message = event.getMessage().getContentDisplay();
			MessageChannel chan = event.getChannel();
			if(message.equals("!categories")){
				HashMap<String, String> categories = ts.getCategories();
				instance.sendMessage(chan.getId(), "Retrieving categories...");
				String categoryMessage = "";
				for(String category : categories.keySet()){
					categoryMessage+=category+", ";
				}
				categoryMessage = categoryMessage.substring(0,categoryMessage.length()-2);
				instance.sendMessage(chan.getId(), categoryMessage);
			} else if(message.equals("!quit")){
				instance.sendMessage(chan.getId(), "Ending Trivia...");
				instance.setState(State.NORMAL_STATE);
				instance.unregisterMessageListener(this);
			} else if(message.split(" ")[0].equals("!set")){
				String category = "";
				for(int i = 1; i<message.split(" ").length; i++){
					if(i == 1){
						category += message.split(" ")[i];
					} else {
						category += " "+message.split(" ")[i];
					}
				}
				if(!ts.getCategories().containsKey(category)){
					instance.sendMessage(chan.getId(), "Not a valid category name, case matters!");
				} else {
					ts.setCategory(category);
					instance.sendMessage(chan.getId(), "Category set to "+category+"!");
				}
			}
			else if(message.equals("!begin")){
				try {
					instance.sendMessage(chan.getId(), "Generating questions...");
					QuestionListener ql = new QuestionListener(ts, chan.getId());
					ql.startQuiz();
					instance.unregisterMessageListener(this);
					instance.registerMessageListener(ql);
					instance.setState(State.QUESTION_MODE);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
