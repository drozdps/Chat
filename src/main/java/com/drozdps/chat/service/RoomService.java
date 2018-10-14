package com.drozdps.chat.service;

import com.drozdps.chat.model.Room;
import com.drozdps.chat.model.RoomMember;
import com.drozdps.chat.model.Message;
import java.util.List;

public interface RoomService {	
	Room save(Room room);
	void sendPrivateMessage(Message message);
	Room join(RoomMember joiningUser, Room room);
	Room findById(String roomId);
	void sendPublicMessage(Message message);
	Room leave(RoomMember leavingUser, Room room);
	List<Room> findAll();
}
