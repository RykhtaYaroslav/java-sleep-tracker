package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AverageSleepingDuration implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sleepingSessions) {
        double avgMinutes = sleepingSessions.stream().mapToLong(s -> Duration.between(s.getAsleepTime(), s.getGetUpTime()).toMinutes()).average().orElse(0);
        long h = (long) avgMinutes / 60;
        long m = Math.round(avgMinutes - h * 60);
        String avgDuration = String.format("%02d ч %02d м", h, m);
        return new SleepAnalysisResult<>("Средняя продолжительность сна", avgDuration);
    }
}
