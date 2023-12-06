package ru.clevertec.knyazev;

import lombok.extern.slf4j.Slf4j;
import ru.clevertec.knyazev.interaction.impl.Client;
import ru.clevertec.knyazev.interaction.impl.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class Main {
    public static void main(String[] args) {

        Client client = new Client(Collections.synchronizedList(getClientList()), new AtomicInteger());
        Server server = new Server(Collections.synchronizedList(new ArrayList<>()));

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++) {
            CompletableFuture.supplyAsync(() -> client.send(), executorService)
                    .thenApply(integerRequest -> server.service(integerRequest))
                    .thenAccept(integerResponse -> client.receive(integerResponse))
                    .whenComplete((v, ex) -> {
                        if (ex == null) {
                            log.info("Client accumulated result: " + client.getAccumulator().get());
                        } else {
                            log.error(ex.getMessage(), ex);
                        }

                    });
        }

        executorService.shutdown();

        log.info("Main end");
    }

    public static List<Integer> getClientList() {
        return IntStream.range(1, 101)
                .collect(ArrayList::new, List::add, List::addAll);
    }
}
