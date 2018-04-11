package com.nubicall.enums;

public enum UserStatus {
	
	STATUS_ACTIVE("Active"),
	STATUS_BLOCKED("Blocked");
	
	private String name;
	
	UserStatus(String name){
		this.name = name;
	}

	public String getName() {
        return name;
    }
    
	public String toLowerCase(){
		return toString().toLowerCase();
	}
	
	public String toUpperCase(){
		return toString().toUpperCase();
	}
}
