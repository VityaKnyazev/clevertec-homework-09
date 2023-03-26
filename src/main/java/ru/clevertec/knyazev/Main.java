package ru.clevertec.knyazev;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import ru.clevertec.knyazev.client.ClientImpl;
import ru.clevertec.knyazev.collection.ConcurrentList;
import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;
import ru.clevertec.knyazev.server.ServerImpl;

public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ConcurrentList<Integer> clientList = new ConcurrentList<>();
		ConcurrentList<Integer> serverList = new ConcurrentList<>();

		ClientImpl client = new ClientImpl(clientList);
		ServerImpl server = new ServerImpl(serverList);

		for (int i = 1; i <= 100; i++) {
			clientList.add(i);
		}

		ExecutorService clientExecutorService = Executors.newFixedThreadPool(8);
		ExecutorService serverExecutorService = Executors.newFixedThreadPool(8);
		
		while (clientList.size() > 0) {
			Future<Request> clientRequest = clientExecutorService.submit(client.send());
			Request clientRequestVal = clientRequest.get();
					
			Future<Response> serverResponse = serverExecutorService.submit(server.doRequestOnResponse(clientRequestVal));
			Response serverResponseVal = serverResponse.get();
			
			clientExecutorService.execute(client.receive(serverResponseVal));		
		}
		
		clientExecutorService.shutdown();
		serverExecutorService.shutdown();
		
		clientExecutorService.awaitTermination(2, TimeUnit.MINUTES);
		serverExecutorService.awaitTermination(2, TimeUnit.MINUTES);

		System.out.println("Client list size check: " + clientList.size());
		System.out.println("Server list size check: " + serverList.size());
		System.out.println("Client atomic val: " + client.getAccumulator());

	}

}
