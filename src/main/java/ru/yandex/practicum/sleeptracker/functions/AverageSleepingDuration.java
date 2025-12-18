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
        return new SleepAnalysisResult<>("Средняя продолжительность сна", Duration.ofMinutes(Math.round(avgMinutes)));
    }
}
