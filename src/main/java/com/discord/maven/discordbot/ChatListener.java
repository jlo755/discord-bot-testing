package com.discord.maven.discordbot;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ChatListener extends ListenerAdapter{
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.isFromType(ChannelType.TEXT)){
			if(event.getMessage().getContentDisplay().equals("!trivia") && !DiscordBot.getInstance().getTriviaMode()){
				MessageChannel chan = event.getChannel();
				DiscordBot.getInstance().sendMessage(chan.getId(), "Trivia mode activated, enter !categories to choose a category.");
				DiscordBot.getInstance().setTriviaMode(true);
				DiscordBot.getInstance().registerMessageListener(new TriviaListener());
			}
			
		}
	}

}
