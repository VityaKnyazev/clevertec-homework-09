package ru.clevertec.knyazev.client;

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

public class ClientConcurrentImplTest {
	private SimpleList<Integer> clientList;

	private ClientConcurrentImpl clientImpl;

	@BeforeEach
	public void setUp() {
		clientList = new ConcurrentList<>();
		IntStream.range(1, 100 + 1).forEach(el -> clientList.add(el));

		clientImpl = new ClientConcurrentImpl(clientList);
	}

	@Test
	public void checkSendShouldReturnRequestValuesFromIntegerList() throws InterruptedException, ExecutionException {
		List<Integer> resultList = new ArrayList<>();

		ExecutorService es = Executors.newFixedThreadPool(5);

		for (int i = 1; i <= 100; i++) {
			Future<Request> request = es.submit(clientImpl.send());
			Request requestVal = request.get();
			resultList.add(requestVal.getRequestData());
		}

		es.shutdown();
		es.awaitTermination(10, TimeUnit.SECONDS);

		assertAll(() -> assertThat(clientList.size()).isEqualTo(0), () -> assertThat(resultList.size()).isEqualTo(100),
				() -> assertThat(resultList.stream().distinct().toList().size()).isEqualTo(100));
	}

	@Test
	public void checkReceiveShouldAccumulateIntegerValuesFromResponse() throws InterruptedException {
		List<Response> actualResponse = new ArrayList<>();
		
		for (int i = 1; i <= 100; i++) {
			actualResponse.add(new Response(i));
		}
		
		ExecutorService es = Executors.newFixedThreadPool(5);
		
		actualResponse.parallelStream().forEach(response -> es.execute(clientImpl.receive(response)));

		es.shutdown();		
		es.awaitTermination(10, TimeUnit.SECONDS);
		
		assertThat(clientImpl.getAccumulator()).isEqualTo(5050);
	}

}
