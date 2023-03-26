package ru.clevertec.knyazev.client;

import java.util.Random;

import ru.clevertec.knyazev.collection.SimpleList;
import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;

public class ClientImpl extends Client<Request> {
	
	public ClientImpl(SimpleList<Integer> values) {
		super(values);
	}

	@Override
	Request sendRequest() {
		int randomListIndex = new Random().nextInt(0, values.size());
		Integer sendingValue = values.remove(randomListIndex);
		return new Request(sendingValue);
	}

	@Override
	public void getResponse(Response response) {
		// TODO Auto-generated method stub
	}

	@Override
	public Request call() throws Exception {
		return sendRequest();
	}
}
