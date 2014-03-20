package com.lwolf.wf.persistence.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lwolf.scud.persistence.Entity;

@Table(name="input")
public class InputEntity implements Entity {
	
	@Id
	@Column(name="process_id")
	public Integer processId;
	
	@Id
	@Column(name="task_id")
	public Integer taskId;
	
	@Id
	@GeneratedValue
	@Column(name="input_id")
	public Integer inputId;

	@Column(name="name")
	public String name;

	@Column(name="value")
	public String value;

}
