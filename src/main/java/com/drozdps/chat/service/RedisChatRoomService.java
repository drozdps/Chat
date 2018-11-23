package com.drozdps.chat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.drozdps.chat.model.Message;
import com.drozdps.chat.utils.Destinations;
import com.drozdps.chat.utils.SystemMessages;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.drozdps.chat.model.Room;
import com.drozdps.chat.dao.RoomRepository;
import com.drozdps.chat.model.RoomMember;

@Service
public class RedisChatRoomService implements RoomService {

	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private SimpMessagingTemplate webSocketMessagingTemplate;

	@Autowired
	private MessageService messageService;

	@Override
	public Room save(Room room) {
		return roomRepository.save(room);
	}

	@Override
	public Room join(RoomMember joiningUser, Room room) {
		room.addUser(joiningUser);
		roomRepository.save(room);
		sendPublicMessage(SystemMessages.welcome(room.getId(), joiningUser.getUsername()));
		updateConnectedUsersViaWebSocket(room);
		return room;
	}
	
	@Override
	public Room findById(String roomId) {
		return roomRepository.findOne(roomId);
	}

	@Override
	public Room leave(RoomMember leavingUser, Room room) {
		sendPublicMessage(SystemMessages.goodbye(room.getId(), leavingUser.getUsername()));	
		room.removeUser(leavingUser);
		roomRepository.save(room);
		updateConnectedUsersViaWebSocket(room);
		return room;
	}

	@Override
	public void sendPublicMessage(Message message) {
		webSocketMessagingTemplate.convertAndSend(
				Destinations.ChatRoom.publicMessages(message.getRoomId()),
				message);

		messageService.addMessage(message);
	}

	@Override
	public void sendPrivateMessage(Message message) {
		webSocketMessagingTemplate.convertAndSendToUser(
				message.getToUser(),
				Destinations.ChatRoom.privateMessages(message.getRoomId()), 
				message);
		
		webSocketMessagingTemplate.convertAndSendToUser(
				message.getFromUser(),
				Destinations.ChatRoom.privateMessages(message.getRoomId()), 
				message);

		messageService.addMessage(message);
	}

	@Override
	public List<Room> findAll() {
		return (List<Room>) roomRepository.findAll();
	}
	
	private void updateConnectedUsersViaWebSocket(Room room) {
		webSocketMessagingTemplate.convertAndSend(
				Destinations.ChatRoom.connectedUsers(room.getId()),
				room.getConnectedUsers());
	}
}
