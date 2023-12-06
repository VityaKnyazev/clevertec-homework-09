package ru.clevertec.knyazev.integration;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.knyazev.interaction.impl.Client;
import ru.clevertec.knyazev.interaction.impl.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AppTest {

    private Client client;
    private Server server;

    @BeforeEach
    public void setUp() {
        List<Integer> clientList = IntStream.range(1, 101)
                .collect(ArrayList::new, List::add, List::addAll);

        client = new Client(Collections.synchronizedList(clientList), new AtomicInteger());
        server = new Server(Collections.synchronizedList(new ArrayList<>()));
    }

    @Test
    public void checkClientServerIntegration() {

        List<CompletableFuture> appCompletionStages = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            CompletableFuture<Void> completableFutureRes = CompletableFuture.supplyAsync(() -> client.send())
                    .thenApply(integerRequest -> server.service(integerRequest))
                    .thenAccept(integerResponse -> client.receive(integerResponse));

            appCompletionStages.add(completableFutureRes);
        }

        appCompletionStages.forEach(cf -> cf.join());

        assertAll(
                () -> assertThat(client.getClientData()).isEmpty(),
                () -> assertThat(client.getAccumulator().get()).isEqualTo(5050),
                () -> assertThat(server.getServerData()).hasSize(100)
                        .has(new Condition<>(dl -> dl.stream().mapToInt(Integer::intValue).sum() == 5050,
                                "server data size sum equals to 5050"))
        );
    }
}
