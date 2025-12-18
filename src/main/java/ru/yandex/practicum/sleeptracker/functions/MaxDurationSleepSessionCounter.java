package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class MaxDurationSleepSessionCounter implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sleepingSessions) {
        Duration duration = sleepingSessions.stream()
                .map(s -> Duration.between(s.getAsleepTime(), s.getGetUpTime()))
                .max(Comparator.naturalOrder()).orElse(Duration.ZERO);

        return new SleepAnalysisResult<>("Найдена максимальная длительность сессии сна", duration);
    }
}
