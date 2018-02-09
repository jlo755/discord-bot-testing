package com.discord.maven.discordbot;

import java.io.IOException;

import com.discord.trivia.service.TriviaService;
import com.discord.trivia.util.Question;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class QuestionListener  extends ListenerAdapter {
	private TriviaService ts;
	private DiscordBot instance;
	private String chanIdListenTo;
	
	public QuestionListener(TriviaService ts, String chanIdListenTo){
		this.ts = ts;
		this.chanIdListenTo = chanIdListenTo;
		instance = DiscordBot.getInstance();
	}
	
	public void startQuiz() throws IOException{
		ts.generateQuestions();
		ts.startQuiz();
		Question q = ts.getCurrentQuestion();
		readQuestion(q, chanIdListenTo);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		Message m = event.getMessage();
		String userId = m.getAuthor().getId();
		String channelId = m.getChannel().getId();
		if(channelId.equals(chanIdListenTo)){
			if(m.getContentDisplay().equals("1")){
				instance.sendMessage(chanIdListenTo, "hELLO");
			}
		}
	}
	
	private void readQuestion(Question q, String channelId){
		String message = "Question: "+q.question;
		message += "\n";
		message += "\nOption 1:  "+q.option1;
		message += "\nOption 2:  "+q.option2;
		message += "\nOption 3:  "+q.option3;
		message += "\nOption 4:  "+q.option4;
		System.out.println(message);
		instance.sendMessage(channelId, message);
	}
}
