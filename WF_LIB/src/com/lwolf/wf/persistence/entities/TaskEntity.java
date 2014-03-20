package com.lwolf.wf.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lwolf.scud.persistence.Entity;

@Table(name="task")
public class TaskEntity implements Entity {
	
	@Id
	@Column(name="process_id")
	public Integer processId;
	
	@Id
	@GeneratedValue
	@Column(name="task_id")
	public Integer taskId;
	
	@Column(name="script_id")
	public String scriptId;
	
	@Column(name="created_on")
	public Date createdOn;
	
	@Column(name="created_by")
	public String createdBy;
	
	@Column(name="modified_on")
	public Date modifiedOn;
	
	@Column(name="modified_by")
	public String modifiedBy;

	@Column(name="name")
	public String name;

	@Column(name="role")
	public String role;
	
	@Column(name="buttons")
	public String buttons;
	
	@Column(name="options")
	public String options;
	
	@Column(name="decision")
	public String decision;
	
	@Column(name="due_on")
	public Date dueOn;
	
	@Column(name="completed_on")
	public Date completedOn;
	
}
