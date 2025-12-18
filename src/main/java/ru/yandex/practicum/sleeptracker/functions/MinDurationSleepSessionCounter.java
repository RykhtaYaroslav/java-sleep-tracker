package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class MinDurationSleepSessionCounter implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sleepingSessions) {
        Duration duration = sleepingSessions.stream()
                .map(s -> Duration.between(s.getAsleepTime(), s.getGetUpTime()))
                .min(Comparator.naturalOrder()).orElse(Duration.ZERO);

        return new SleepAnalysisResult<>("Найдена минимальная длительность сессии сна", duration);
    }
}
