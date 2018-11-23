package com.drozdps.chat.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import com.drozdps.chat.dao.UserRepository;import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import com.drozdps.chat.model.User;

@Component
public class NewUserValidator implements Validator {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void validate(Object target, Errors errors) {
		User newUser = (User) target;
		if (userRepository.exists(newUser.getUsername())) {
			errors.rejectValue("username", "new.account.username.already.exists");
		}
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}
}
