package ru.clevertec.knyazev.client;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import ru.clevertec.knyazev.collection.SimpleList;
import ru.clevertec.knyazev.data.MultiThreadingDataTransfer;
import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;

public class ClientConcurrentImpl extends Client<SimpleList<Integer>> implements MultiThreadingDataTransfer<Request, Response> {
	private AtomicInteger accumulator;

	public ClientConcurrentImpl(SimpleList<Integer> clientData) {
		super(clientData);

		this.accumulator = new AtomicInteger(0);
	}

	@Override
	Request sendRequest() {
		int randomListIndex = new Random().nextInt(0, clientData.size());
		Integer sendingValue = clientData.remove(randomListIndex);
		return new Request(sendingValue);
	}

	@Override
	void getResponse(Response response) {
		int responseValue = response.getResponseData();
		accumulator.addAndGet(responseValue);
	}

	@Override
	public Callable<Request> send() {
		return () -> sendRequest();
	}

	@Override
	public Runnable receive(Response response) {
		return () -> getResponse(response);
	}
	
	public int getAccumulator() {
		return accumulator.get();
	}
}
