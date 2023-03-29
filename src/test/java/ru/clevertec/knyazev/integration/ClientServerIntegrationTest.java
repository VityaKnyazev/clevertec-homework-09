package ru.clevertec.knyazev.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.client.ClientConcurrentImpl;
import ru.clevertec.knyazev.collection.ConcurrentList;
import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;
import ru.clevertec.knyazev.server.ServerConcurrentImpl;

public class ClientServerIntegrationTest {
	ConcurrentList<Integer> clientList;
	ConcurrentList<Integer> serverList;

	ClientConcurrentImpl client;
	ServerConcurrentImpl server;
	
	@BeforeEach
	public void setUp() {
		clientList = new ConcurrentList<>();
		serverList = new ConcurrentList<>();

		client = new ClientConcurrentImpl(clientList);
		server = new ServerConcurrentImpl(serverList);
	}
	
	@Test
	public void checkClientServerIntegrationShouldReduceServerListSize() throws InterruptedException, ExecutionException {
		for (int i = 1; i <= 100; i++) {
			clientList.add(i);
		}

		ExecutorService clientExecutorService = Executors.newFixedThreadPool(5);
		ExecutorService serverExecutorService = Executors.newFixedThreadPool(3);
		
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

		assertAll(
				() -> assertThat(clientList.size()).isEqualTo(0),
				() -> assertThat(serverList.size()).isEqualTo(100),
				() -> assertThat(client.getAccumulator()).isEqualTo(5050)
				);
	}
}
