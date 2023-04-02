package ru.clevertec.knyazev.server;

import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;

public abstract class Server<E> {
	final E serverData;
	
	Server(E serverData) {
		this.serverData = serverData;
	}
	
	abstract void getRequest(Request clientRequest);
	
	abstract Response sendResponse();
}
