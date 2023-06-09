package ru.clevertec.knyazev.client;

import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;

public abstract class Client<E> {
	final E clientData;

	public Client(E clientData) {
		this.clientData = clientData;
	}	

	abstract Request sendRequest();
	
	abstract void getResponse(Response serverResponse);
}
