package com.nubicall.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nubicall.controller.ServiceDirectory;
import com.nubicall.controller.UserController;
import com.nubicall.enums.UserStatus;
import com.nubicall.model.User;
import com.nubicall.service.UserService;

public class UserControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserController userController;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		
		mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
	}
	
	@Test
	public void testGetUserOk() throws Exception{
		String username = "nacho";
		User user = new User();
		user.setUsername("nacho");
		user.setFirstName("Ignacio");
		user.setLastName("Galieri");
		user.setEmail("user@example.com");
		user.setPassword("Nub1C4llSup3RS3GuR0");
		user.setPhone("555-5555");
		user.setUserStatus(UserStatus.STATUS_ACTIVE.getName());
		
		when(userService.getUser(username)).thenReturn(user);
		
		mockMvc.perform(get(ServiceDirectory.USERS).param("username", username))
        	.andExpect(status().isOk());
		
		verify(userService, times(1)).getUser(username);
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void testGetUserNotFound() throws Exception{
		String username = "test";
		when(userService.getUser(username)).thenReturn(null);
		
		mockMvc.perform(get(ServiceDirectory.USERS).param("username", username))
			.andExpect(status().isNotFound());
		
		verify(userService, times(1)).getUser(username);
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void testCreateUserSuccess() throws Exception{
		User user = new User();
		user.setUsername("nacho");
		user.setFirstName("Ignacio");
		user.setLastName("Galieri");
		user.setEmail("user@example.com");
		user.setPassword("Nub1C4llSup3RS3GuR0");
		user.setPhone("555-5555");
		
		user.setUserStatus(UserStatus.STATUS_ACTIVE.getName());
		
		when(userService.createUser(user)).thenReturn(anyObject());
		
		mockMvc.perform(post(ServiceDirectory.USERS)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(asJsonString(user)))
				.andExpect(status().isCreated());
		
		verify(userService, times(1)).createUser(anyObject());
		verifyNoMoreInteractions(userService);
		
	}
	
	@Test
	public void testCreateUserBadRequest() throws Exception{
		User user = new User();
		user.setUsername("nacho");
		user.setFirstName("Ignacio");
		user.setLastName("Galieri");
		user.setEmail("user@example.com");
		user.setPassword("Nub1C4llSup3RS3GuR0");
		user.setPhone("555-5555");
		user.setUserStatus(UserStatus.STATUS_ACTIVE.getName());
		
		when(userService.createUser(user)).thenReturn(user);
		
		mockMvc.perform(post(ServiceDirectory.USERS)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(asJsonString(null)))
				.andExpect(status().isBadRequest());
		
		verifyNoMoreInteractions(userService);
		
	}
	
	@Test
	public void testUpdateUserSuccess() throws Exception{
		User user = new User();
		user.setUsername("nacho");
		user.setFirstName("Ignacio");
		user.setLastName("Galieri");
		user.setEmail("user@example.com");
		user.setPassword("Nub1C4llSup3RS3GuR0");
		user.setPhone("555-5555");
		user.setUserStatus(UserStatus.STATUS_ACTIVE.getName());
		
		when(userService.getUser(user.getUsername())).thenReturn(user);
		when(userService.updateUser(user)).thenReturn(user);
		
		mockMvc.perform(put(ServiceDirectory.USERS).param("username", user.getUsername())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(asJsonString(user)))
				.andExpect(status().isOk());
		
		verify(userService, times(1)).getUser(anyObject());
		verify(userService, times(1)).updateUser(anyObject());
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void testUpdateUserBadRequest() throws Exception{
		User user = new User();
		user.setUsername("nacho");
		user.setFirstName("Ignacio");
		user.setLastName("Galieri");
		user.setEmail("user@example.com");
		user.setPassword("Nub1C4llSup3RS3GuR0");
		user.setPhone("555-5555");
		user.setUserStatus(UserStatus.STATUS_ACTIVE.getName());
		
		when(userService.getUser(user.getUsername())).thenReturn(user);
		when(userService.updateUser(user)).thenReturn(user);
		
		mockMvc.perform(put(ServiceDirectory.USERS)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(asJsonString(null)))
				.andExpect(status().isBadRequest());
		
		verifyNoMoreInteractions(userService);
	}
	
	
	@Test
	public void testDeleteUserBadRequest() throws Exception{
		String userName = "nacho";
		
		when(userService.deleteUser(userName)).thenReturn(anyObject());
		
		mockMvc.perform(delete(ServiceDirectory.USERS).param("username", userName)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest());
		
		verify(userService, times(1)).deleteUser(anyObject());
		
		verifyNoMoreInteractions(userService);
	}
	
	private String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

}
