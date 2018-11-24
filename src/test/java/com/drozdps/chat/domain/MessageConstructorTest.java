package com.drozdps.chat.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

import com.drozdps.chat.model.Message;
import com.drozdps.chat.service.MessageBuilder;
import com.drozdps.chat.utils.SystemUsers;


public class MessageConstructorTest {
	
	private final String to = "Receiver";
	private final String publicText = "just a mock text";
	private final String privateText = "mock private test";
	private final String roomId = "test_room";
	private final String from = "Test Test";
	private final String broadcastAdminText = "broadCast from ADMIN!!";

	@Test
	public void test_CreateAdminPublicMessage() {
		Message systemMessage = new MessageBuilder()
				.newMessage()
				.withRoomId(roomId)
				.systemMessage()
				.withText(broadcastAdminText);

		assertThat(systemMessage.isPublic(), is(true));
		assertThat(systemMessage.isFromAdmin(), is(true));
		assertThat(systemMessage.getRoomId(), is(roomId));
		assertThat(systemMessage.getFromUser(), is(SystemUsers.ADMIN.getUsername()));
		assertThat(systemMessage.getToUser(), is(nullValue()));
		assertThat(systemMessage.getText(), is(broadcastAdminText));
	}
	
	@Test
	public void shouldCreatePublicInstantMessage() {
		Message publicMessage = new MessageBuilder()
				.newMessage()
				.withRoomId(roomId)
				.publicMessage()
				.fromUser(from)
				.withText(publicText);

		assertThat(publicMessage.getRoomId(), is(roomId));
		assertThat(publicMessage.getFromUser(), is(from));
		assertThat(publicMessage.getToUser(), is(nullValue()));
		assertThat(publicMessage.isPublic(), is(true));
		assertThat(publicMessage.isFromAdmin(), is(false));		
		assertThat(publicMessage.getText(), is(publicText));
	}

	@Test
	public void test_CreatePrivateMessage() {
		Message privateMessage = new MessageBuilder()
				.newMessage()
				.withRoomId(roomId)
				.privateMessage()
				.toUser(to)
				.fromUser(from).
				withText(privateText);

		assertThat(privateMessage.isPublic(), is(false));
		assertThat(privateMessage.isFromAdmin(), is(false));
		assertThat(privateMessage.getRoomId(), is(roomId));
		assertThat(privateMessage.getFromUser(), is(from));
		assertThat(privateMessage.getToUser(), is(to));
		assertThat(privateMessage.getText(), is(privateText));
	}
}
