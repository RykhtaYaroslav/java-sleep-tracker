package ru.yandex.practicum.sleeptracker.customExceptions;

public class SleepLogException extends RuntimeException {
    public SleepLogException(String message) {
        super(message);
    }
}
