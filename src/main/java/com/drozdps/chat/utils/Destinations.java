package com.drozdps.chat.utils;

public class Destinations {

	public static class ChatRoom {
		
		public static String privateMessages(String roomId) {
			return "/queue/" + roomId + ".private.messages";
		}
		
		public static String publicMessages(String roomId) {
			return "/topic/" + roomId + ".public.messages";
		}
		
		public static String connectedUsers(String roomId) {
			return "/topic/" + roomId + ".connected.users";
		}
	}
}
