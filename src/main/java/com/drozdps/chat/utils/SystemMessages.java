package com.drozdps.chat.utils;

import com.drozdps.chat.model.Message;
import com.drozdps.chat.service.MessageBuilder;

public class SystemMessages {
	
	public static final Message welcome(String roomId, String username) {
		return new MessageBuilder()
				.newMessage()
				.withRoomId(roomId)
				.systemMessage()
				// default message to be printed when a user joins a chat room
				.withText(username + " joined us!!!");
	}

	public static final Message goodbye(String roomId, String username) {
		return new MessageBuilder()
				.newMessage()
				.withRoomId(roomId)
				.systemMessage()
				// default message to be printed when a user leaves a chat room
				.withText(username + " left us :-(");
	}
}
