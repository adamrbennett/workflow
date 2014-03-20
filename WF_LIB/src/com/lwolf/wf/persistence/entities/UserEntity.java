package com.lwolf.wf.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lwolf.scud.persistence.Entity;

@Table(name="users")
public class UserEntity implements Entity {
	
	@Id
	@Column(name="user_name")
	public String userName;
	
	@Column(name="full_name")
	public String fullName;

}
