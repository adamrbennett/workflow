package com.lwolf.wf.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.lwolf.wf.vo.Notification;

public abstract class DataTransferObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Date createdOn;
	public String createdBy;
	
	public Date modifiedOn;
	public String modifiedBy;
	
	public Collection<Notification> notifications = new ArrayList<Notification>();
	
}
