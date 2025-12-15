package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.function.Function;

public class SleeplessNightsCount implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sleepingSessions) {
        LocalDate firstData = sleepingSessions.getFirst().getAsleepTime().toLocalDate();

        LocalDate lastData = sleepingSessions.getLast().getAsleepTime().toLocalDate();

        Period nightsAmount = Period.between(firstData, lastData);


        long sleeplessNights = sleepingSessions.stream()
                .filter(s ->
                        s.getAsleepTime().toLocalDate().isBefore(s.getGetUpTime().toLocalDate())
                                || s.getGetUpTime().toLocalTime().isBefore(LocalTime.of(6, 0)))
                .count();


        return null;
    }
}
