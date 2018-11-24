package com.drozdps.chat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.drozdps.chat.dao.UserRepository;
import com.drozdps.chat.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChatApplicationTests {
	
	@Autowired
	private MockMvc mvc;


	@Test
	public void contextLoads() throws Exception {
		this.mvc.perform(get("/chat")).andExpect(redirectedUrl("http://localhost/"))
			.andExpect(content().string(""));
	}
	
	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private UserRepository repository;

	@Test
	public void testExample() throws Exception {
		this.entityManager.persist(new User("sboot", "1234","test","asd@das.com"));
		List<User> user = this.repository.findAll();
		System.out.println(">>>>>>>>>>\n"+user.toString());
		//assertThat(user.getUsername()).isEqualTo("sboot");
		//assertThat(user.getVin()).isEqualTo("1234");
	}


}
