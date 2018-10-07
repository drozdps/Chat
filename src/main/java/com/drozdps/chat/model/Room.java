package com.drozdps.chat.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.google.common.annotations.VisibleForTesting;

@RedisHash("room")
public class Room {
	
	@Id
	private String id;
	private String name;
	private String description;
	private List<RoomMember> connectedUsers = new ArrayList<>();
	
	public Room() {}
	
	@VisibleForTesting
	public Room(String id, String name, String description) {
		this.id = id;
		this.description = description;
		this.name = name;
		
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<RoomMember> getConnectedUsers() {
		return connectedUsers;
	}
	public void addUser(RoomMember user) {
		this.connectedUsers.add(user);
	}
	public void removeUser(RoomMember user) {
		this.connectedUsers.remove(user);
	}
	public int getNumberOfConnectedUsers(){
		return this.connectedUsers.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
