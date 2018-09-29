package com.drozdps.chat.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.drozdps.chat.dao.RoleRepository;
import com.drozdps.chat.dao.UserRepository;
import com.drozdps.chat.model.Role;
import com.drozdps.chat.model.User;
/**
 * 
 * @author drozdps@gmail.com
 *
 */

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder crypto;	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	@Transactional
	public User createUser(User user) {
		if (user == null) {
			// TO DO: throw exception
			return null;
		}
		user.addRoles(Arrays.asList(roleRepository.findByName("USER")));
		user.setPassword(crypto.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
