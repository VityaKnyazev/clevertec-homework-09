package ru.clevertec.knyazev.data;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Represents data object wrap to store data in
 */
@SuperBuilder
@Getter
public abstract class Data<T> {
    private T data;
}
