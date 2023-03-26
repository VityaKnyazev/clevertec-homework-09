package ru.clevertec.knyazev.collection;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class ConcurrentList<E> implements SimpleList<E> {
	private static final int INIT_LIST_SIZE = 3;
	
	private static final Lock lock = new ReentrantLock();

	private int totalListSize;
	private int elListSize;

	private Object[] object;

	public ConcurrentList() {
		totalListSize = INIT_LIST_SIZE;
		object = new Object[totalListSize];
	}

	@Override
	public int size() {
		return elListSize;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		lock.lock();
		
		if (index < 0 || index >= elListSize) {
			throw new IndexOutOfBoundsException();
		}
		
		lock.unlock();
		
		return (E) object[index];
	}

	@Override
	public void add(E e) {
		lock.lock();
		
		if (elListSize == totalListSize) {
			resize();
		}

		object[elListSize] = e;
		elListSize++;
		
		lock.unlock();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E remove(int index) {
		lock.lock();
		
		if (elListSize == 0 && index >= 0) {
			throw new IllegalStateException("Collection is empty. Can't remove element with index=" + index);
		}
		
		if (index < 0 || index >= elListSize) {
			throw new NoSuchElementException("Elemen with index=" + index + " doesn't exist in collection.");
		}
		
		E element = (E) object[index];

		Object[] objHead;
		Object[] objTail;

		if (index == 0 || index == elListSize - 1) {
			if (index == 0) {
				objTail = Arrays.copyOfRange(object, 1, elListSize);
				object = Arrays.copyOf(objTail, totalListSize);
			} else {
				object[index] = null;
			}
		} else {
			objHead = Arrays.copyOfRange(object, 0, index);
			objTail = Arrays.copyOfRange(object, index + 1, elListSize);
			Object[] obj = Stream.concat(Arrays.stream(objHead), Arrays.stream(objTail)).toArray();
			object = Arrays.copyOf(obj, totalListSize);
		}

		elListSize--;
		
		lock.unlock();
		
		return element;		
	}

	@Override
	public boolean contains(E e) {
		lock.lock();
		
		for (int i = 0; i < elListSize; i++) {
			if (e.equals(object[i])) {
				lock.unlock();
				return true;
			}
				
		}

		lock.unlock();
		
		return false;
	}

	private void resize() {
		totalListSize *= 2;
		object = Arrays.copyOf(object, totalListSize);
	}

}
