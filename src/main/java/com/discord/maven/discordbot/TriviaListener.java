package com.discord.maven.discordbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.discord.trivia.service.TriviaService;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class TriviaListener extends ListenerAdapter{

	TriviaService ts;

	public TriviaListener(){
		ts = new TriviaService();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.isFromType(ChannelType.TEXT)){
			String message = event.getMessage().getContentDisplay();
			DiscordBot instance = DiscordBot.getInstance();
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
				instance.setTriviaMode(false);
				instance.unregisterMessageListener(this);
			} else if(message.split(" ")[0].equals("!set")){
				String category = message.split(" ")[1];
				if(!ts.getCategories().containsKey(category)){
					instance.sendMessage(chan.getId(), "Not a valid category name, case matters!");
				} else {
					ts.setCategory(category);
					instance.sendMessage(chan.getId(), "Category set to science!");
				}
			}
			else if(message.equals("!begin")){
				try {
					ts.generateQuestions();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
}
