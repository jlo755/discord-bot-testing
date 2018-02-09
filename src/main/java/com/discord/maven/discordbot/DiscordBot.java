package com.discord.maven.discordbot;

import javax.security.auth.login.LoginException;

import com.discord.trivia.util.State;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class DiscordBot {
	private static DiscordBot instance;
	private JDA jda;
	private String token;
	private boolean triviaMode;
	public State state;
	
	public DiscordBot(String token) throws LoginException, InterruptedException{
		if(instance == null){
			instance = this;
			setToken(token);
			startJDA();
			state = State.NORMAL_STATE;
		}
	}
	
	private void startJDA() throws LoginException, InterruptedException{
		jda = new JDABuilder(AccountType.BOT)
				.setToken(token)
				.buildBlocking();
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public State getState(){
		return this.state;
	}
	
	public void setState(State s){
		this.state = s;
	}
	
	public static DiscordBot getInstance(){
		return instance;
	}
	
	public void sendMessage(String channelId, String message){
		TextChannel test = jda.getTextChannelById(channelId);
		Message m = new MessageBuilder().append(message).build();
		MessageAction ma = test.sendMessage(m);
		ma.queue();
	}
	
	public void registerMessageListener(ListenerAdapter la){
		jda.addEventListener(la);
	}
	
	public void unregisterMessageListener(ListenerAdapter la){
		jda.removeEventListener(la);
	}
}
