package com.drozdps.chat.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.drozdps.chat.model.Room;
import com.drozdps.chat.service.RoomService;

@Controller
public class ChatController {
	
	@Autowired
	private RoomService roomService;
	
    @RequestMapping("/chat")
    public ModelAndView getRooms() {
    	ModelAndView modelAndView = new ModelAndView("chat");
    	modelAndView.addObject("rooms", roomService.findAll());
    	return modelAndView;
    }
}
