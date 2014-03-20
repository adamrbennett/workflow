package com.lwolf.wf.vo;

public class Notification {

	public enum NotificationType {
		PARENT_KEY_DEPENDENCY;
	}
	
	public NotificationType type;
	public String message;
	
	public Notification() {
		
	}
	
	public Notification(NotificationType type) {
		this.type = type;
	}

}
