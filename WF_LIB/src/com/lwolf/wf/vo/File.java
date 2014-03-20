package com.lwolf.wf.vo;

import java.io.BufferedInputStream;

public class File {
	
	private String _name;
	private String _type;
	private BufferedInputStream _data;
	
	public File() {
		
	}
	
	public File(String name, String type, BufferedInputStream data) {
		_name = name;
		_type = type;
		_data = data;
	}
	
	public String name() {
		return _name;
	}
	
	public String type() {
		return _type;
	}
	
	public BufferedInputStream data() {
		return _data;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public void setType(String type) {
		_type = type;
	}
	
	public void setData(BufferedInputStream data) {
		_data = data;
	}

}
