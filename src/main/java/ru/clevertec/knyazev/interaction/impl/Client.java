package ru.clevertec.knyazev.interaction.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.clevertec.knyazev.data.Data;
import ru.clevertec.knyazev.data.impl.IntegerRequest;
import ru.clevertec.knyazev.data.impl.IntegerResponse;
import ru.clevertec.knyazev.interaction.DataTransfer;
import ru.clevertec.knyazev.interaction.exception.ClientException;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;

@AllArgsConstructor
@Getter
public class Client implements DataTransfer {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private List<Integer> clientData;
    private AtomicInteger accumulator;

    /**
     *
     * Send data to service
     *
     * @return integer request with sending data
     * @throws ClientException when sending data is empty or
     *         thread sleeping was interrupted
     */
    @SuppressWarnings("unchecked")
    @Override
    public IntegerRequest send() {

        if (clientData.isEmpty()) {
            throw new ClientException();
        }

        try {

            Integer sendingValue;

            try {
                lock.writeLock().lock();
                int randomListIndex = new Random().nextInt(clientData.size());
                sendingValue = clientData.remove(randomListIndex);
            } finally {
                lock.writeLock().unlock();
            }

            Thread.sleep(new Random().nextInt(100, 501));

            return IntegerRequest.builder()
                    .data(sendingValue)
                    .build();
        } catch (InterruptedException e) {
            throw new ClientException(e);
        }
    }

    /**
     *
     * Receive data response from service
     *
     * @param data receiving object data
     * @param <T> data type
     */
    @Override
    public <T> void receive(Data<T> data) {
        IntegerResponse response = (IntegerResponse) data;
        accumulator.getAndAdd(response.getData());
    }
}
