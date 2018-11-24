package com.drozdps.chat.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import com.drozdps.chat.model.Room;
import com.drozdps.chat.model.RoomMember;
import com.drozdps.chat.service.MessageBuilder;
import com.drozdps.chat.service.MessageService;
import com.drozdps.chat.service.RedisChatRoomService;
import com.drozdps.chat.service.RoomService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.drozdps.chat.dao.RoomRepository;
import com.drozdps.chat.model.Message;
import com.drozdps.chat.utils.Destinations;
import com.drozdps.chat.utils.SystemMessages;

@RunWith(MockitoJUnitRunner.class)
public class KeyValueVaultTest {

	@InjectMocks
	private RoomService roomService = new RedisChatRoomService();
	@Mock
	private RoomRepository roomRepo;
	@Captor
	private ArgumentCaptor<String> receiverCaptro;
	@Captor
	private ArgumentCaptor<String> destCaptor;
	@Captor
	private ArgumentCaptor<Message> messageCaptor;
	@Captor
	private ArgumentCaptor<Object> genericMessageCaptor;
	@Mock
	private MessageService messageService;
	@Mock
	private SimpMessagingTemplate socketMessageTemplate;
	@Captor
	private ArgumentCaptor<Room> roomCaptor;
	
	@Test
	public void test_MustPersistRoom() {
		Room room = new Room("testRoom1", "Test Test", "Bla bla bla");
		roomService.save(room);
		verify(roomRepo, times(1)).save(room);
	}

	@Test
	public void test_MustReturnRoomById() {
		String roomId = "roomId";
		roomService.findById(roomId);
		verify(roomRepo, times(1)).findOne(roomId);
	}

	@Test
	public void test_MustReturnAllRooms() {
		roomService.findAll();
		verify(roomRepo, times(1)).findAll();
	}

	@Test
	public void test_UserShouldBeAssignedToRoom() {
		Room room = new Room("testRoom1", "tst tst", "blablabla");
		// newly created room must not contain users
		assertThat(room.getNumberOfConnectedUsers(), is(0));
		
		RoomMember user = new RoomMember("Test Usr1");
		roomService.join(user, room);
		// not room must contain 1 user: Test Usr1
		assertThat(room.getNumberOfConnectedUsers(), is(1));
		// persist room
		verify(roomRepo, times(1)).save(room);
		verify(socketMessageTemplate, times(2)).convertAndSend(destCaptor.capture(),
				genericMessageCaptor.capture());
		verify(messageService, times(1)).addMessage(messageCaptor.capture());

		List<Object> messageObjects = genericMessageCaptor.getAllValues();

		assertThat(destCaptor.getAllValues().get(1), is(Destinations.ChatRoom.connectedUsers(room.getId())));
		assertThat(destCaptor.getAllValues().get(0), is(Destinations.ChatRoom.publicMessages(room.getId())));
		assertEquals(SystemMessages.welcome(room.getId(), user.getUsername()), (Message) messageObjects.get(0));
		assertEquals(SystemMessages.welcome(room.getId(), user.getUsername()),
				messageCaptor.getValue());
		assertThat(((List<RoomMember>) messageObjects.get(1)).contains(user), is(true));
	}

	@Test
	public void test_UserMustLeaveRoom() {
		Room room = new Room("testRoom1", "tst tst1", "blablabla");
		RoomMember user = new RoomMember("test1");
		room.addUser(user);
		assertThat(room.getNumberOfConnectedUsers(), is(1));
		roomService.leave(user, room);
		assertThat(room.getNumberOfConnectedUsers(), is(0));
		
		verify(socketMessageTemplate, times(2)).convertAndSend(destCaptor.capture(),
				genericMessageCaptor.capture());
		verify(roomRepo, times(1)).save(room);
		verify(messageService, times(1)).addMessage(messageCaptor.capture());

		assertThat(destCaptor.getAllValues().get(0), is(Destinations.ChatRoom.publicMessages(room.getId())));
		assertThat(destCaptor.getAllValues().get(1), is(Destinations.ChatRoom.connectedUsers(room.getId())));
		assertEquals(SystemMessages.goodbye(room.getId(), user.getUsername()), (Message) (genericMessageCaptor.getAllValues()).get(0));
		assertEquals(SystemMessages.goodbye(room.getId(), user.getUsername()), messageCaptor.getValue());
		assertThat(((List<RoomMember>) (genericMessageCaptor.getAllValues()).get(1)).contains(user), is(false));
	}

	@Test
	public void test_UserMustSendBroadCastMessage() {
		Room room = new Room("testRoom1", "tst tst1", "blablabla");
		RoomMember user = new RoomMember("test1");
		room.addUser(user);
		assertThat(room.getNumberOfConnectedUsers(), is(1));

		Message publicMessage = new MessageBuilder()
									.newMessage()
									.withRoomId(room.getId())
									.publicMessage()
									.fromUser(user.getUsername())
									.withText("blabla");

		roomService.sendPublicMessage(publicMessage);

		verify(messageService, times(1)).addMessage(messageCaptor.capture());
		verify(socketMessageTemplate, times(1)).convertAndSend(destCaptor.capture(),
				genericMessageCaptor.capture());

		assertThat(destCaptor.getValue(), is(Destinations.ChatRoom.publicMessages(room.getId())));
		assertEquals(publicMessage, (Message) genericMessageCaptor.getValue());
		assertEquals(publicMessage, messageCaptor.getValue());
	}

	@Test
	public void test_UserMustSendPrivateMessage() {
		Room room = new Room("testRoom1", "tst tst1", "blablabla");
		RoomMember test1 = new RoomMember("test1");
		RoomMember test2 = new RoomMember("test2");
		room.addUser(test1);
		room.addUser(test2);
		assertThat(room.getNumberOfConnectedUsers(), is(2));

		roomService.sendPrivateMessage(new MessageBuilder()
										.newMessage()
										.withRoomId(room.getId())
										.privateMessage()
										.toUser(test2.getUsername())
										.fromUser(test1.getUsername())
										.withText("blablabla"));

		verify(socketMessageTemplate, times(2)).convertAndSendToUser(receiverCaptro.capture(),
				destCaptor.capture(), genericMessageCaptor.capture());
		verify(messageService, times(1)).addMessage(messageCaptor.capture());

		assertThat(destCaptor.getAllValues().get(0), is(Destinations.ChatRoom.privateMessages(room.getId())));
		assertThat(receiverCaptro.getAllValues().get(0), is(test2.getUsername()));
		assertThat(destCaptor.getAllValues().get(1), is(Destinations.ChatRoom.privateMessages(room.getId())));
		assertThat(receiverCaptro.getAllValues().get(1), is(test1.getUsername()));
	}
}
