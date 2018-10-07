package com.drozdps.chat.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.drozdps.chat.model.User;
import com.drozdps.chat.service.UserService;
import com.drozdps.chat.utils.NewUserValidator;

@Controller
public class AuthenticationController {

	private static final String REGISTRATION_PAGE_NAME = "registration";
	
	@Autowired
	private UserService userService;

	@Autowired
	private NewUserValidator newUserValidator;


	@RequestMapping("/")
	public String login() {
		return "login"; // TO BE IMPLEMENTED
	}

	@RequestMapping("/"+REGISTRATION_PAGE_NAME)
	public String newAccount(Model model) {
		model.addAttribute("user", new User());
		return REGISTRATION_PAGE_NAME;
	}

	@RequestMapping(path = "/"+REGISTRATION_PAGE_NAME, method = RequestMethod.POST)
	public String createAccount(@Valid User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return REGISTRATION_PAGE_NAME;
		}
		userService.createUser(user);
		return "redirect:/";
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(newUserValidator);
	}
}
