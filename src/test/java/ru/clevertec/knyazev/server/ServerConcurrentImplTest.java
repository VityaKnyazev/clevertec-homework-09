package ru.clevertec.knyazev.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.collection.ConcurrentList;
import ru.clevertec.knyazev.collection.SimpleList;
import ru.clevertec.knyazev.data.Request;
import ru.clevertec.knyazev.data.Response;

public class ServerConcurrentImplTest {
	private SimpleList<Integer> serverList;

	private ServerConcurrentImpl serverImpl;
	
	@BeforeEach
	public void setUp() {
		serverList = new ConcurrentList<>();
		
		serverImpl = new ServerConcurrentImpl(serverList);		
	}
	
	@Test
	public void checkSendShouldReturnServerListSize() throws InterruptedException, ExecutionException {
		IntStream.range(1, 100 + 1).forEach(el -> serverList.add(el));
		List<Integer> resultList = new ArrayList<>();
		
		
		ExecutorService es = Executors.newFixedThreadPool(5);

		for (int i = 1; i <= 10; i++) {
			Future<Response> response = es.submit(serverImpl.send());
			Response responseVal = response.get();
			resultList.add(responseVal.getResponseData());
		}

		es.shutdown();
		es.awaitTermination(10, TimeUnit.SECONDS);
		
		assertAll( 
				() -> assertThat(resultList.size()).isEqualTo(10),
				() -> assertThat(resultList.stream().allMatch(val -> val == 100)).isTrue()
				);
	}
	
	@Test
	public void checkRecieveShouldAddRequestValuesToServerList() throws InterruptedException {
		List<Integer> actualRequestList = new ArrayList<>();
		IntStream.range(1, 100 + 1).forEach(el -> actualRequestList.add(el));
		
		ExecutorService es = Executors.newFixedThreadPool(5);
		
		actualRequestList.parallelStream().forEach(el -> es.execute(serverImpl.receive(new Request(el))));
		
		es.shutdown();
		es.awaitTermination(120, TimeUnit.SECONDS);
		
		assertThat(serverList.size()).isEqualTo(100);	
	}
	
	@Test
	public void checkDoRequestOnResponseShouldReturnServerListSizeOnResponse() throws InterruptedException, ExecutionException {
		List<Integer> resultList = new ArrayList<>();
		
		ExecutorService es = Executors.newFixedThreadPool(5);
		
		for (int i = 1; i <= 100; i++) {
			Future<Response> response = es.submit(serverImpl.doResponseOnRequest(new Request(i)));
			Response responseVal = response.get();
			resultList.add(responseVal.getResponseData());
		}
		
		es.shutdown();
		es.awaitTermination(120, TimeUnit.SECONDS);
		
		assertAll( 
				() -> assertThat(resultList.size()).isEqualTo(100),
				() -> assertThat(resultList.stream().distinct().toList().size()).isEqualTo(100)
		);
	}

}
