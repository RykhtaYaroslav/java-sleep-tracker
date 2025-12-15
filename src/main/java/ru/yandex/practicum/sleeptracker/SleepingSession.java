package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;

public class SleepingSession {
    private final LocalDateTime asleepTime;
    private final LocalDateTime getUpTime;
    private final SleepQuality sleepQuality;

    public SleepingSession(LocalDateTime asleepTime, LocalDateTime getUpTime, SleepQuality sleepQuality) {
        this.asleepTime = asleepTime;
        this.getUpTime = getUpTime;
        this.sleepQuality = sleepQuality;
    }

    public LocalDateTime getAsleepTime() {
        return asleepTime;
    }

    public LocalDateTime getGetUpTime() {
        return getUpTime;
    }

    public SleepQuality getSleepQuality() {
        return sleepQuality;
    }
}
