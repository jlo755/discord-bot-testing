package com.discord.maven.discordbot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class DiscordBotTest {
	public static void main(String[] args) throws LoginException, InterruptedException{
		String token = args[0];
		DiscordBot db = new DiscordBot(token);
		
		db.registerMessageListener(new ChatListener());
		
	}
}
