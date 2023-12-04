package ru.clevertec.knyazev;

import ru.clevertec.knyazev.interaction.impl.Client;
import ru.clevertec.knyazev.interaction.impl.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        Client client = new Client(Collections.synchronizedList(getClientList()), new AtomicInteger());
        Server server = new Server(Collections.synchronizedList(new ArrayList<>()));

        ExecutorService clientExecutorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++) {
            CompletableFuture.supplyAsync(() -> client.send(), clientExecutorService)
                    .thenApply(integerRequest -> server.service(integerRequest))
                    .thenAccept(integerResponse -> client.receive(integerResponse))
                    .whenComplete((v, ex) -> {
                        if (ex == null) {
                            System.out.println("Server result list size: " + server.getServerData().size());
                            System.out.println("Client result: " + client.getAccumulator().get());
                            System.out.println("Client result list size: " + client.getClientData().size());
                        } else {
                            ex.printStackTrace();
                        }

                    });
        }

        clientExecutorService.shutdown();

        try {
            clientExecutorService.awaitTermination(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Main end");

    }

    public static List<Integer> getClientList() {
        List<Integer> clientList = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            clientList.add(i);
        }

        return clientList;
    }
}
