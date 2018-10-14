package com.drozdps.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drozdps.chat.model.Room;
import com.drozdps.chat.dao.MessageRepository;
import com.drozdps.chat.model.Message;

@Service
public class CassandraMessageService implements MessageService {
	
	@Autowired
	private RoomService roomService;

	@Autowired
	private MessageRepository messageRepository;
	
	@Override
	public List<Message> getDirectMessages(String username, String roomId) {
		return messageRepository.getMessagesByUsernameAndRoomId(username, roomId);
	}
	
	@Override
	public void addMessage(Message message) {
		if (message.isPublic() || message.isFromAdmin()) {
			Room room = roomService.findById(message.getRoomId());
			// a little bit lambdas =D
			room.getConnectedUsers().forEach(connectedUser -> {
				message.setUsername(connectedUser.getUsername());
				messageRepository.save(message);
			});
		} else {
			message.setUsername(message.getFromUser());
			messageRepository.save(message);
			
			message.setUsername(message.getToUser());
			messageRepository.save(message);
		}
	}
}
