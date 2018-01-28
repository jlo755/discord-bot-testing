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
		JDA jda = new JDABuilder(AccountType.BOT)
				.setToken("Mzk4MzMxODM2MDU4ODI4ODMw.DS8_AQ.Eetp-hQZnb32yBnYFDrlTfiCbT4")
				.buildBlocking();
		
		TextChannel test = jda.getTextChannelById("398331379332546563");
		Message m = new MessageBuilder().append("Hello world!").build();
		MessageAction ma = test.sendMessage(m);
		ma.queue();
		System.out.println("Hello?");
		
	}
}
