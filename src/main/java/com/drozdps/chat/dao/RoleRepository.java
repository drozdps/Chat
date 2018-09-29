package com.drozdps.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drozdps.chat.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByName(String name);
}
