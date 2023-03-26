package ru.clevertec.knyazev.server;

import ru.clevertec.knyazev.collection.SimpleList;
import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;

public abstract class Server<E> {
	final SimpleList<E> serverData;
	
	Server(SimpleList<E> serverData) {
		this.serverData = serverData;
	}
	
	abstract void getRequest(Request clientRequest);
	
	abstract Response sendResponse();
}
