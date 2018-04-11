package com.nubicall.model;


/**
 * @author Wilson Salamanca
 *
 */
public class Response {

	/**
	 * Universally Unique TX Identifier
	 */
	private String uuid;
	
	/**
	 * Short Result Description
	 */
	private String message;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
