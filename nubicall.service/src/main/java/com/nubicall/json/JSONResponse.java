package com.nubicall.json;

/**
 * @author Wilson Salamanca
 *
 */
public class JSONResponse {

	private Object items;
	
	private boolean success = true;
	
	public JSONResponse(Object items, boolean success) {
		this.items = items;
		this.success = success;
	}

	public Object getItems() {
		return items;
	}

	public void setItems(Object items) {
		this.items = items;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
