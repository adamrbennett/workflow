package com.lwolf.wf.persistence.entities;

import java.io.BufferedInputStream;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.lwolf.scud.persistence.Entity;

@Table(name="process")
public class ProcessEntity implements Entity {
	
	@Id
	@GeneratedValue
	@Column(name="process_id")
	public Integer processId;
	
	@Column(name="model_id")
	public Integer modelId;
	
	@Column(name="created_on")
	public Date createdOn;
	
	@Column(name="created_by")
	public String createdBy;
	
	@Column(name="modified_on")
	public Date modifiedOn;
	
	@Column(name="modified_by")
	public String modifiedBy;
	
	@OrderBy
	@Column(name="name")
	public String name;
	
	@Lob
	@Column(name="process_data")
	public BufferedInputStream data;

}
