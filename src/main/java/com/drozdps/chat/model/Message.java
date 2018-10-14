package com.drozdps.chat.model;

import java.util.Date;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.drozdps.chat.utils.SystemUsers;

// wire with cassandra table
@Table("message")
public class Message {
	
	@PrimaryKeyColumn(name = "roomId", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String roomId;
	
	@JsonIgnore
	@PrimaryKeyColumn(name = "username", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String username;
	
	
	@PrimaryKeyColumn(name = "date", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
	private Date date;
	
	private String fromUser;
	private String text;
	private String toUser;
	
	public Message() { 
		this.date = new Date();
	}
	
	public boolean isPublic() {
		return Strings.isNullOrEmpty(this.toUser);
	}
	public boolean isFromAdmin() {
		return this.fromUser.equals(SystemUsers.ADMIN.getUsername());
	}
	public String getUsername() {
		return username;
	}
	
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getToUser() {
		return toUser;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		final int coefficient = 31;
		hash = coefficient * hash + ((roomId == null) ? 0 : roomId.hashCode());
		hash = coefficient * hash + ((fromUser == null) ? 0 : fromUser.hashCode());
		hash = coefficient * hash + ((username == null) ? 0 : username.hashCode());
		hash = coefficient * hash + ((text == null) ? 0 : text.hashCode());
		hash = coefficient * hash + ((toUser == null) ? 0 : toUser.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (roomId == null) {
			if (other.roomId != null)
				return false;
		} else if (!roomId.equals(other.roomId))
			return false;
		if (fromUser == null) {
			if (other.fromUser != null)
				return false;
		} else if (!fromUser.equals(other.fromUser))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (toUser == null) {
			if (other.toUser != null)
				return false;
		} else if (!toUser.equals(other.toUser))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
