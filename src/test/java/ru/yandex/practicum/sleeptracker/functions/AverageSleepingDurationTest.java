package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepLogReader;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AverageSleepingDurationTest {

    public List<SleepingSession> prepareTestSession(String sleepData) throws IOException {
        Path testPath = Paths.get("testFile.txt");

        Files.writeString(testPath, sleepData, StandardCharsets.UTF_8);

        SleepLogReader sleepLogReader = new SleepLogReader(testPath);

        return sleepLogReader.readLog();
    }

    @Test
    public void shouldReturn1MinuteDurationOfSleeping() throws IOException {
        String sleepData = """
                01.10.25 23:59;02.10.25 00:00;GOOD
                """;

        var testSession = prepareTestSession(sleepData);

        AverageSleepingDuration f = new AverageSleepingDuration();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals("00 ч 01 м", result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }

    @Test
    public void shouldReturn8HoursSleepDuration() throws IOException {
        String sleepData = """
                01.10.25 23:00;02.10.25 07:00;GOOD
                02.10.25 22:00;03.10.25 08:00;GOOD
                04.10.25 00:00;04.10.25 06:00;GOOD
                04.10.25 23:00;05.10.25 07:00;GOOD
                05.10.25 20:00;06.10.25 04:00;GOOD
                """;

        var testSessions = prepareTestSession(sleepData);

        AverageSleepingDuration f = new AverageSleepingDuration();

        SleepAnalysisResult<?> result = f.apply(testSessions);

        Assertions.assertEquals("08 ч 00 м", result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }
}
