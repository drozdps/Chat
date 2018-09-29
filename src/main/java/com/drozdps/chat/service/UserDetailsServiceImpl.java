package com.drozdps.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.drozdps.chat.dao.UserRepository;
import com.drozdps.chat.model.User;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOne(username);

        if (user == null) {
        	throw new UsernameNotFoundException("We cannot find such user ;-(");
        }  
        Set<SimpleGrantedAuthority> grantedAuthorities = user.getRoles().stream()
        							.map(role -> new SimpleGrantedAuthority(role.getName()))
        							.collect(Collectors.toSet());
	    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);  
    }
}
