package com.drozdps.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.drozdps.chat.model.User;

public interface UserRepository extends JpaRepository<User, String> {}
