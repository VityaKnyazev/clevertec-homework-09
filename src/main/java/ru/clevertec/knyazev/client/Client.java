package ru.clevertec.knyazev.client;

import java.util.concurrent.Callable;

import ru.clevertec.knyazev.collection.SimpleList;
import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;

public abstract class Client<T> implements Callable<T> {
	SimpleList<Integer> values;

	public Client(SimpleList<Integer> values) {
		this.values = values;
	}
	

	abstract Request sendRequest();
	
	abstract void getResponse(Response response);
}
