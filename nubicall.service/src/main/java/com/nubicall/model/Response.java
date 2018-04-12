package com.nubicall.model;

import java.util.UUID;

/**
 * @author Wilson Salamanca
 *
 */
public class Response {

	/**
	 * Universally Unique TX Identifier
	 */
	private UUID uuid;
	
	/**
	 * Short Result Description
	 */
	private String message;
	
	public Response(){
		this.uuid = UUID.randomUUID();
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
