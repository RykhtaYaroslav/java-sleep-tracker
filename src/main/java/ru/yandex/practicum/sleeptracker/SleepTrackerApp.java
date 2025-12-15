package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    private final List<SleepingSession> sleepingSessionsList;

    private final List<Function<List<SleepingSession>, SleepAnalysisResult<?>>> functions;

    public SleepTrackerApp(List<SleepingSession> sleepingSessionsList) {
        this.sleepingSessionsList = sleepingSessionsList;
        this.functions = List.of(new SleepSessionCounter(), new MinDurationSleepSessionCounter(),
                new MaxDurationSleepSessionCounter(), new AverageSleepingDuration(), new BadQualitySleepingSessionsCount());
    }

    public static void main(String[] args) {

        String filePath = "src/main/resources/sleep_log.txt";

        SleepTrackerApp tracker = null;
        try {
            Path path = Paths.get(filePath);

            SleepLogReader sleepLogReader = new SleepLogReader(path);

            tracker = new SleepTrackerApp(sleepLogReader.readLog());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        List<SleepingSession> list = tracker.sleepingSessionsList;

        tracker.functions.stream().map(f -> f.apply(list)).forEach(System.out::println);


    }
}