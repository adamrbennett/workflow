package com.lwolf.wf.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TaskListDto extends DataTransferObject implements List<TaskDto> {

	private static final long serialVersionUID = 1L;
	
	public Integer processId;
	
	private ArrayList<TaskDto> _list = new ArrayList<TaskDto>();
	
	public TaskListDto() {
		
	}
	
	public TaskListDto(Integer processId) {
		this.processId = processId;
	}

	@Override
	public int size() {
		return _list.size();
	}

	@Override
	public boolean isEmpty() {
		return _list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return _list.contains(o);
	}

	@Override
	public Iterator<TaskDto> iterator() {
		return _list.iterator();
	}

	@Override
	public Object[] toArray() {
		return _list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return _list.toArray(a);
	}

	@Override
	public boolean add(TaskDto e) {
		return _list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return _list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return _list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends TaskDto> c) {
		return _list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends TaskDto> c) {
		return _list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return _list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return _list.retainAll(c);
	}

	@Override
	public void clear() {
		_list.clear();
	}

	@Override
	public TaskDto get(int index) {
		return _list.get(index);
	}

	@Override
	public TaskDto set(int index, TaskDto element) {
		return _list.set(index, element);
	}

	@Override
	public void add(int index, TaskDto element) {
		_list.add(index, element);
	}

	@Override
	public TaskDto remove(int index) {
		return _list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return _list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return _list.lastIndexOf(o);
	}

	@Override
	public ListIterator<TaskDto> listIterator() {
		return _list.listIterator();
	}

	@Override
	public ListIterator<TaskDto> listIterator(int index) {
		return _list.listIterator(index);
	}

	@Override
	public List<TaskDto> subList(int fromIndex, int toIndex) {
		return _list.subList(fromIndex, toIndex);
	}

}
