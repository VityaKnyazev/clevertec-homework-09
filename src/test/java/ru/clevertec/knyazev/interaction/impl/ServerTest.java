package ru.clevertec.knyazev.interaction.impl;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.knyazev.data.impl.IntegerRequest;
import ru.clevertec.knyazev.data.impl.IntegerResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerTest {

    private Server server;

    @BeforeEach
    public void setUp() {
        server = new Server(Collections.synchronizedList(new ArrayList<>()));
    }

    @Test
    public void checkServiceShouldProcessRequestAndSendResponse() {
        List<IntegerRequest> requestData = IntStream.range(1, 101)
                .mapToObj(intVal -> IntegerRequest.builder()
                        .data(intVal)
                        .build())
                .collect(ArrayList::new, List::add, List::addAll);

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        List<Future<IntegerResponse>> futureIntegerResponses = requestData.stream()
                .map(integerRequest -> executorService.submit(() -> server.service(integerRequest)))
                .toList();

        List<Integer> actualValues = futureIntegerResponses.stream().map(integerResponseFuture -> {
            try {
                return integerResponseFuture.get().getData();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        executorService.shutdown();

        assertThat(actualValues).isNotEmpty()
                .hasSize(100)
                .has(new Condition<>(l -> l.stream().mapToInt(Integer::intValue).sum() == 5050,
                        "response server data size sum equals to 5050"));
    }
}
