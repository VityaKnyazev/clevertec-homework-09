package ru.clevertec.knyazev.interaction.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.clevertec.knyazev.data.Data;
import ru.clevertec.knyazev.data.impl.IntegerRequest;
import ru.clevertec.knyazev.data.impl.IntegerResponse;
import ru.clevertec.knyazev.interaction.DataTransfer;
import ru.clevertec.knyazev.interaction.exception.ServerException;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents simple Server that can receive integer request
 * and send integer response
 */
@AllArgsConstructor
@Getter
public class Server implements DataTransfer {

    private final Lock lock = new ReentrantLock(true);

    private List<Integer> serverData;

    /**
     *
     * Send response with integer data
     *
     * @return integer response data
     */
    @SuppressWarnings("unchecked")
    @Override
    public IntegerResponse send() {
        return IntegerResponse.builder()
                .data(serverData.size())
                .build();
    }

    /**
     *
     * Get request with integer data
     *
     * @param data receiving integer request data
     * @param <T> integer request value
     * @throws ServerException if can't cast input data to integer request or
     *                         thread that called method was interrupted
     */
    @Override
    public <T> void receive(Data<T> data) throws ServerException {

        IntegerRequest request;

        try {
            request = (IntegerRequest) data;

            Thread.sleep(new Random().nextInt(100, 1001));
            serverData.add(request.getData());
        } catch (ClassCastException | InterruptedException e) {
            throw new ServerException(e);
        }
    }

    /**
     *
     * Service incoming request and send response, both with
     * integer data
     *
     * @param data request that contains integer data
     * @return response that contains integer data
     */
    public IntegerResponse service(IntegerRequest data) {
        try {
            lock.lock();
            receive(data);
            return send();
        } finally {
            lock.unlock();
        }
    }
}
