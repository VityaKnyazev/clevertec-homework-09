package ru.clevertec.knyazev.interaction.impl;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.clevertec.knyazev.data.impl.IntegerRequest;
import ru.clevertec.knyazev.data.impl.IntegerResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientTest {

    @RepeatedTest(value = 50)
    public void checkSendShouldReturnAllClientListData() {
        List<Integer> clientList = IntStream.range(1, 101)
                .collect(ArrayList::new, List::add, (l1, l2) -> l1.addAll(l2));

        Client client = new Client(Collections.synchronizedList(clientList), new AtomicInteger());

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        List<Future<IntegerRequest>> futureIntegerRequests = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Future<IntegerRequest> futureIntegerRequest = executorService.submit(() -> client.send());
            futureIntegerRequests.add(futureIntegerRequest);
        }

        List<Integer> actualValues = futureIntegerRequests.stream().map(integerRequestFuture -> {
            try {
                return integerRequestFuture.get().getData();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        executorService.shutdown();


        assertThat(actualValues).isNotEmpty()
                .hasSize(100)
                .has(new Condition<>(l -> l.stream().mapToInt(Integer::intValue).sum() == 5050,
                        "el sum equals to 5050"));
    }

    @RepeatedTest(value = 50)
    public void checkReceiveShouldCalculateAllReceivedData() {
        Client client = new Client(Collections.synchronizedList(new ArrayList<>()), new AtomicInteger());

        List<IntegerResponse> inputResponses = IntStream.range(1, 101)
                .mapToObj(intVal -> IntegerResponse.builder()
                        .data(intVal)
                        .build())
                .collect(Collectors.toList());

        ExecutorService es = Executors.newFixedThreadPool(5);

        inputResponses.parallelStream().forEach(response -> es.execute(() -> client.receive(response)));

        es.shutdown();
        try {
            es.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertThat(client.getAccumulator().get()).isEqualTo(5050);
    }
}
