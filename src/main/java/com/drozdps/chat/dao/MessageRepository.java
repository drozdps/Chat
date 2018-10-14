package com.drozdps.chat.dao;

import java.util.List;
import com.drozdps.chat.model.Message;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface MessageRepository extends CassandraRepository<Message> {
	
	List<Message> getMessagesByUsernameAndRoomId(String username, String roomId);
	
}
