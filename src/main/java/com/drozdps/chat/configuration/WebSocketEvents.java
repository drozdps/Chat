package com.drozdps.chat.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.drozdps.chat.model.RoomMember;
import com.drozdps.chat.service.RoomService;

@Component
public class WebSocketEvents {

	@Autowired
	private RoomService roomService;
	
	@EventListener
	private void handleSessionConnected(SessionConnectEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		String roomId = headers.getNativeHeader("roomId").get(0);
		headers.getSessionAttributes().put("roomId", roomId);
		RoomMember joiningUser = new RoomMember(event.getUser().getName());
		
		roomService.join(joiningUser, roomService.findById(roomId));
	}

	@EventListener
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		if (headers.getSessionAttributes().get("roomId") != null) {
			String roomId = headers.getSessionAttributes().get("roomId").toString();
			RoomMember leavingUser = new RoomMember(event.getUser().getName());
			roomService.leave(leavingUser, roomService.findById(roomId));
		}
	}
}
