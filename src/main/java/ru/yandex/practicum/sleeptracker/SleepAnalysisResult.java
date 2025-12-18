package ru.yandex.practicum.sleeptracker;

import java.time.Duration;

public class SleepAnalysisResult<T> {
    private final String description;
    private final T value;

    public SleepAnalysisResult(String description, T value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value instanceof Duration) {

            return description + ": " + String.format("%02d:%02d", ((Duration) value).toHours(), ((Duration) value).toMinutesPart());
        }
        return description + ": " + value;
    }
}
