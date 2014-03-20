package com.lwolf.wf.dto;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class InputMapDto extends DataTransferObject implements Map<String, InputDto> {

	private static final long serialVersionUID = 1L;
	
	public Integer processId;
	public Integer taskId;
	
	private LinkedHashMap<String, InputDto> _map = new LinkedHashMap<String, InputDto>();
	
	public InputMapDto() {
		
	}
	
	public InputMapDto(Integer processId, Integer taskId) {
		this.processId = processId;
		this.taskId = taskId;
	}

	@Override
	public int size() {
		return _map.size();
	}

	@Override
	public boolean isEmpty() {
		return _map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return _map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return _map.containsValue(value);
	}

	@Override
	public InputDto get(Object key) {
		return _map.get(key);
	}

	@Override
	public InputDto put(String key, InputDto value) {
		return _map.put(key, value);
	}

	@Override
	public InputDto remove(Object key) {
		return _map.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends InputDto> m) {
		_map.putAll(m);
	}

	@Override
	public void clear() {
		_map.clear();
	}

	@Override
	public Set<String> keySet() {
		return _map.keySet();
	}

	@Override
	public Collection<InputDto> values() {
		return _map.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, InputDto>> entrySet() {
		return _map.entrySet();
	}

}
