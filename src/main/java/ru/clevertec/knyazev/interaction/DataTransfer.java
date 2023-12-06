package ru.clevertec.knyazev.interaction;

import ru.clevertec.knyazev.data.Data;

import java.util.function.Function;

/**
 *
 * Represents object that can send and receive data objects
 *
 */
public interface DataTransfer {

    /**
     *
     * Send object data as a result
     *
     * @return data object
     */
    <T> Data<T> send();

    /**
     *
     * get object data to working with
     *
     * @param data receiving object data
     */
    <T> void receive(Data<T> data);
}
