package com.drozdps.chat.service;

import com.drozdps.chat.model.Room;
import com.drozdps.chat.model.RoomMember;
import com.drozdps.chat.model.InstantMessage;
import java.util.List;

public interface RoomService {	
	Room save(Room room);
	Room findById(String roomId);
	void sendPublicMessage(InstantMessage instantMessage);
	void sendPrivateMessage(InstantMessage instantMessage);
	Room join(RoomMember joiningUser, Room room);
	Room leave(RoomMember leavingUser, Room room);
	List<Room> findAll();
}
