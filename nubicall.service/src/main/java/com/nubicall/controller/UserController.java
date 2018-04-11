package com.nubicall.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nubicall.model.Response;
import com.nubicall.model.User;
import com.nubicall.service.UserService;

@RestController
public class UserController {
	
	private static final String TEST_RESPONSE_PREFIX = "testUuid-";
	private static final String TEST_RESPONSE_END = "-nubicall";
	private static final String USER_TO_CREATE_MESSAGE = "User to create ";
	private static final String USER_CREATED_MESSAGE = "User created ";
	private static final String USER_TO_UPDATE_MESSAGE = "User to update ";
	private static final String USER_UPDATED_MESSAGE = "User updated ";
	private static final String USER_TO_FIND_MESSAGE = "Find user  ";
	private static final String USER_FOUND_MESSAGE = "User found "; 
	private static final String USER_NOT_FOUND_MESSAGE = "User not found"; 
	private static final String USER_TO_DELETE_MESSAGE = "Delete user "; 
	private static final String USER_DELETED_MESSAGE = "User Deleted  "; 
	
	
	private  Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	

	@RequestMapping(value = ServiceDirectory.USERS, method = RequestMethod.POST,  consumes=MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response createUser(@RequestBody User user, HttpServletResponse httResp) throws ServletRequestBindingException{
		Response response = this.createResponse();
		if(user == null){
			throw new ServletRequestBindingException(HttpStatus.BAD_REQUEST.getReasonPhrase());
		}
		try{
			logger.info(USER_TO_CREATE_MESSAGE+user.getUsername());
			user = userService.createUser(user);
			httResp.setStatus(HttpStatus.CREATED.value());
			response.setMessage(HttpStatus.CREATED.getReasonPhrase());
			logger.info(USER_CREATED_MESSAGE);
		}catch (Exception e) {
			logger.error(e);
			throw new ServletRequestBindingException(e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value = ServiceDirectory.USERS, method = RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateUser(@RequestParam String username,@RequestBody(required=false) User user, 
			HttpServletResponse httResp) throws ServletRequestBindingException{
		
		Response response = this.createResponse();
		try{
			User oldUser = userService.getUser(username);
			if(oldUser == null){
				throw new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE);
			}
			logger.info(USER_TO_UPDATE_MESSAGE+user.getUsername());
			oldUser = user;
			user = userService.updateUser(oldUser);
			response.setMessage(USER_UPDATED_MESSAGE);
			logger.info(USER_UPDATED_MESSAGE);
		}catch (Exception e) {
			logger.error(e);
			throw new ServletRequestBindingException(e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value = ServiceDirectory.USERS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody User getUser(@RequestParam String username){
		User user = null;
		try{
			logger.info(USER_TO_FIND_MESSAGE+username);
			user = userService.getUser(username);
		}catch (Exception e) {
			throw new ResourceNotFoundException(new Exception());
		}
		if(user == null){
			throw new ResourceNotFoundException(new Exception(USER_NOT_FOUND_MESSAGE));
		}
		logger.info(USER_FOUND_MESSAGE);
		return user;
	}
	
	@RequestMapping(value = ServiceDirectory.USERS, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response deleteUser(@RequestParam String username) throws ServletRequestBindingException{
		Response response = this.createResponse();
		try{
			logger.info(USER_TO_DELETE_MESSAGE+username);
			if(userService.deleteUser(username) == null){
				throw new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE);
			}
			logger.info(USER_DELETED_MESSAGE);
			response.setMessage(USER_DELETED_MESSAGE);
		}catch (Exception e) {
			logger.error(e);
			throw new ServletRequestBindingException(e.getMessage());
		}
		return response;
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public @ResponseBody Response handleNotFound(ResourceNotFoundException ex, HttpServletResponse response) {
		Response responseModel = this.createResponse();
		response.setStatus(HttpStatus.NOT_FOUND.value());
	    responseModel.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
	    logger.error(ex.getCause());
	    return responseModel;
	}

	@ExceptionHandler(ServletRequestBindingException.class)
	public @ResponseBody Response handleBadRequest(ServletRequestBindingException ex, HttpServletResponse response) {
		Response responseModel = this.createResponse();
		response.setStatus(HttpStatus.BAD_REQUEST.value());
	    responseModel.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
	    logger.error(ex);
	    return responseModel;
	}
	
	private Response createResponse(){
		Response response = new Response();
		response.setUuid(TEST_RESPONSE_PREFIX+(new Date()).getTime()+TEST_RESPONSE_END);
		return response;
	}
}
