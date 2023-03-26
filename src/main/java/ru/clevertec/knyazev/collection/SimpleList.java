package ru.clevertec.knyazev.collection;

public interface SimpleList<E> {
	int size();
	E get(int index);
	void add(E e);
	E remove(int index);	
	boolean contains(E e);
}
