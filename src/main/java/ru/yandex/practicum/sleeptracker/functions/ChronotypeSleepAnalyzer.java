package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;
import ru.yandex.practicum.sleeptracker.UserChronotype;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ChronotypeSleepAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sleepingSessions) {
        Predicate<SleepingSession> isNightSession = s ->
                s.getAsleepTime().toLocalDate().isBefore(s.getGetUpTime().toLocalDate())
                        || s.getAsleepTime().toLocalTime().isBefore(LocalTime.of(6, 0));

        Predicate<SleepingSession> isOwlType = s -> (s.getAsleepTime().toLocalTime().isAfter(LocalTime.of(23, 0))
                || s.getAsleepTime().toLocalTime().isBefore(LocalTime.of(6, 0)))
                && (s.getGetUpTime().toLocalTime().isAfter(LocalTime.of(9, 0)));

        Predicate<SleepingSession> isLarkType = s -> (s.getAsleepTime().toLocalTime().isBefore(LocalTime.of(22, 0))
                && s.getAsleepTime().toLocalTime().isAfter(LocalTime.of(6, 0))
                && s.getGetUpTime().toLocalTime().isBefore(LocalTime.of(7, 0)));

        List<SleepingSession> onlyNightSessions = sleepingSessions.stream().filter(isNightSession).toList();

        long owlType = onlyNightSessions.stream().filter(isOwlType).count();

        long larkType = onlyNightSessions.stream().filter(isLarkType).count();

        long pigeonType = onlyNightSessions.size() - owlType - larkType;

        if (owlType > larkType && owlType > pigeonType) {
            return new SleepAnalysisResult<>("Определён хронотип сна", UserChronotype.OWL);
        } else if (larkType > owlType && larkType > pigeonType) {
            return new SleepAnalysisResult<>("Определён хронотип сна", UserChronotype.LARK);
        } else {
            return new SleepAnalysisResult<>("Определён хронотип сна", UserChronotype.PIGEON);
        }
    }
}
