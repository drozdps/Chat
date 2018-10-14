package com.drozdps.chat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.annotation.Secured;

import com.drozdps.chat.model.Room;
import com.drozdps.chat.service.MessageService;
import com.drozdps.chat.service.RoomService;
import com.drozdps.chat.model.RoomMember;
import com.drozdps.chat.model.Message;

@Controller
public class RoomController {

	@Autowired
	private RoomService roomService;

	@Autowired
	private MessageService messageService;

	@SubscribeMapping("/connected.users")
	public List<RoomMember> getRoomMembers(SimpMessageHeaderAccessor headerAccessor) {
		return roomService.findById(headerAccessor.getSessionAttributes().get("roomId").toString())
				.getConnectedUsers();
	}

	@RequestMapping("/room/{roomId}")
	public ModelAndView joinParticularRoom(@PathVariable String roomId, Principal principal) {
		ModelAndView modelAndView = new ModelAndView("room");
		modelAndView.addObject("room", roomService.findById(roomId));
		return modelAndView; 
	}

	@SubscribeMapping("/old.messages")
	public List<Message> getOldMessages(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
		return messageService.getDirectMessages(principal.getName(), 
				headerAccessor.getSessionAttributes().get("roomId").toString());
	}

	@MessageMapping("/send.message")
	public void sendMessage(@Payload Message message, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
		String roomId = headerAccessor.getSessionAttributes().get("roomId").toString();
		message.setFromUser(principal.getName());
		message.setRoomId(roomId);

		if (message.isPublic()) {
			roomService.sendPublicMessage(message);
		} else {
			roomService.sendPrivateMessage(message);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/room", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public Room createRoom(@RequestBody Room room) {
		return roomService.save(room);
	}
}
