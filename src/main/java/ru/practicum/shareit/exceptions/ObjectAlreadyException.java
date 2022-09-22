package ru.practicum.shareit.exceptions;

public class ObjectAlreadyException extends RuntimeException {
    public ObjectAlreadyException(String message) {
        super(message);
    }
}
