package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/*
Текст из ТЗ:

Временем логирования считаем интервал от начала первой сессии сна в файле до окончания последней.
При этом считаем, что пользователь носит часы не снимая — то есть не было сессий сна, которые не попали бы в файл.

В файле отсутствуют данные с 12 по 29 число, но по ТЗ считаем, что пользователь не снимал часы. Ну значит он 17 дней не спал...
 */

public class SleeplessNightsCount implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sleepingSessions) {
        LocalDate firstData;
        LocalDate lastData;

        if (sleepingSessions.getFirst().getAsleepTime().toLocalTime().isBefore(LocalTime.NOON)) {
            firstData = sleepingSessions.getFirst().getAsleepTime().toLocalDate();
        } else {
            firstData = sleepingSessions.getFirst().getAsleepTime().toLocalDate().plusDays(1);
        }

        if (sleepingSessions.getLast().getGetUpTime().toLocalTime().isBefore(LocalTime.NOON)) {
            lastData = sleepingSessions.getLast().getGetUpTime().toLocalDate().minusDays(1);
        } else {
            lastData = sleepingSessions.getLast().getGetUpTime().toLocalDate();
        }


        long nightsAmount = ChronoUnit.DAYS.between(firstData, lastData) + 1;

        Predicate<SleepingSession> isNightSleep = s ->
                s.getAsleepTime().toLocalDate().isBefore(s.getGetUpTime().toLocalDate())
                        || s.getGetUpTime().toLocalTime().isBefore(LocalTime.of(6, 0));
        // Проверяет, попадает ли данная сессия сна в ночную

        Function<SleepingSession, LocalDate> nightsDate = s -> {
            LocalDate d = s.getAsleepTime().toLocalDate();

            LocalTime t = s.getAsleepTime().toLocalTime();

            if (t.isBefore(LocalTime.NOON)) {
                return d.minusDays(1);
            } else {
                return d;
            }
        };
        // Переменная-функция определяет дату ночи для подсчёта сонных ночей

        long sleepNights = sleepingSessions.stream().filter(isNightSleep).map(nightsDate).distinct().count();

        long sleeplessNights = nightsAmount - sleepNights;

        return new SleepAnalysisResult<>("Всего бессонных ночей за данный период", sleeplessNights);
    }
}
