package ru.clevertec.knyazev;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import ru.clevertec.knyazev.client.ClientConcurrentImpl;
import ru.clevertec.knyazev.collection.ConcurrentList;
import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;
import ru.clevertec.knyazev.server.ServerConcurrentImpl;

public class App {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ConcurrentList<Integer> clientList = new ConcurrentList<>();
		ConcurrentList<Integer> serverList = new ConcurrentList<>();

		ClientConcurrentImpl client = new ClientConcurrentImpl(clientList);
		ServerConcurrentImpl server = new ServerConcurrentImpl(serverList);

		for (int i = 1; i <= 100; i++) {
			clientList.add(i);
		}

		ExecutorService clientExecutorService = Executors.newFixedThreadPool(5);
		ExecutorService serverExecutorService = Executors.newFixedThreadPool(2);
		
		while (clientList.size() > 0) {
			Future<Request> clientRequest = clientExecutorService.submit(client.send());
			Request clientRequestVal = clientRequest.get();
					
			Future<Response> serverResponse = serverExecutorService.submit(server.doResponseOnRequest(clientRequestVal));
			Response serverResponseVal = serverResponse.get();
			
			clientExecutorService.execute(client.receive(serverResponseVal));		
		}
		
		clientExecutorService.shutdown();
		serverExecutorService.shutdown();
		
//		clientExecutorService.awaitTermination(2, TimeUnit.MINUTES);
//		serverExecutorService.awaitTermination(2, TimeUnit.MINUTES);

		System.out.println("Client list size check: " + clientList.size());
		System.out.println("Server list size check: " + serverList.size());
		System.out.println("Client atomic val: " + client.getAccumulator());

	}

}
