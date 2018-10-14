package com.drozdps.chat.service;

import com.drozdps.chat.model.Message;
import com.drozdps.chat.utils.SystemUsers;

public class MessageBuilder {
	
	private Message message;
	private MessageContent messageContent;
	private OutboundMessage outboundMessage;
	private MessageRoom room;
	private MessagetType type;
	private InboundMessage inboundMessage;
	
	public MessageBuilder() {}
	
	public MessageRoom newMessage() {
		message = new Message();
		room = new MessageRoom();
		return room;
	}
	
	
	public class MessageRoom {
		public MessagetType withRoomId(String roomId) {
			message.setRoomId(roomId);
			type = new MessagetType();
			return type;
		}
	}
	
	public class InboundMessage {
		public OutboundMessage toUser(String username) {
			message.setToUser(username);
			outboundMessage = new OutboundMessage();
			return outboundMessage;
		}
	}
	
	public class OutboundMessage {
		public MessageContent fromUser(String username) {
			message.setFromUser(username);
			messageContent = new MessageContent();
			return messageContent;
		}
	}
	
	public class MessageContent {
		public Message withText(String text) {
			message.setText(text);
			return message;
		}
	}
	
	public class MessagetType {
		public MessageContent systemMessage(){
			message.setFromUser(SystemUsers.ADMIN.getUsername());
			messageContent = new MessageContent();
			return messageContent;
		}
		
		public InboundMessage privateMessage(){
			inboundMessage = new InboundMessage();
			return inboundMessage;
		}
		
		public OutboundMessage publicMessage() {
			message.setToUser(null);
			outboundMessage = new OutboundMessage();
			return outboundMessage;
		}
		
	}
	
}
