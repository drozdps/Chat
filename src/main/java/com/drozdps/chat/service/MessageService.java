package com.drozdps.chat.service;

import java.util.List;
import com.drozdps.chat.model.Message;

public interface MessageService {
	
	List<Message> getDirectMessages(String username, String roomId);
	void addMessage(Message message);
	
}
