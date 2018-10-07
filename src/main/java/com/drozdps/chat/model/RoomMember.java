package com.drozdps.chat.model;

import java.util.Date;

public class RoomMember implements Comparable<RoomMember> {
	
	private String username;
	private Date joinedAt = new Date();
	
	public RoomMember() {}
	
	public RoomMember(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getJoinedAt() {
		return joinedAt;
	}
	
	@Override
	public String toString() {
		return this.username;
	}

	@Override
	public int compareTo(RoomMember roomMember) {
		return this.username.compareTo(roomMember.getUsername());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		RoomMember other = (RoomMember) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
