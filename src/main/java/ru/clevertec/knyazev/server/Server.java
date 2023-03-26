package ru.clevertec.knyazev.server;

import java.util.concurrent.Callable;

import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;

public abstract class Server<T> implements Callable<T> {
	
	public abstract void getRequest(Request clientRequest);
	
	public abstract Response sendResponse();
}
