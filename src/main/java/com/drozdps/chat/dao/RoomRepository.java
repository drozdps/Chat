package com.drozdps.chat.dao;

import com.drozdps.chat.model.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, String> {}
