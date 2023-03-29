package ru.clevertec.knyazev.server;

import java.util.Random;
import java.util.concurrent.Callable;

import ru.clevertec.knyazev.collection.SimpleList;
import ru.clevertec.knyazev.data.MultiThreadingDataTransfer;
import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;

public class ServerConcurrentImpl extends Server<SimpleList<Integer>> implements MultiThreadingDataTransfer<Response, Request> {

	public ServerConcurrentImpl(SimpleList<Integer> serverData) {
		super(serverData);
	}

	@Override
	void getRequest(Request clientRequest) {
		try {
			serverData.add(clientRequest.getRequestData());
			Thread.sleep(new Random().nextInt(100, 1001));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	Response sendResponse() {
		return new Response(serverData.size());
	}

	@Override
	public Callable<Response> send() {
		return () -> sendResponse();
	}

	@Override
	public Runnable receive(Request request) {
		return () -> getRequest(request);
	}

	public Callable<Response> doRequestOnResponse(Request clientRequest) {
		getRequest(clientRequest);
		return send();
	}

}
