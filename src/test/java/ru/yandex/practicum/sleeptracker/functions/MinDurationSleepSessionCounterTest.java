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
import java.time.Duration;
import java.util.List;

public class MinDurationSleepSessionCounterTest {
    public List<SleepingSession> prepareTestSession(String sleepData) throws IOException {
        Path testPath = Paths.get("testFile.txt");

        Files.writeString(testPath, sleepData, StandardCharsets.UTF_8);

        SleepLogReader sleepLogReader = new SleepLogReader(testPath);

        return sleepLogReader.readLog();
    }

    @Test
    public void shouldReturn2MinDuration() throws IOException {
        String sleepData = """
                30.09.25 23:59;01.10.25 00:00;GOOD
                01.10.25 23:59;02.10.25 00:01;GOOD
                """;

        var testSession = prepareTestSession(sleepData);

        MinDurationSleepSessionCounter f = new MinDurationSleepSessionCounter();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(Duration.ofMinutes(1), result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }

    @Test
    public void shouldReturn8HoursDuration() throws IOException {
        String sleepData = """
                30.09.25 20:00;01.10.25 04:00;GOOD
                01.10.25 23:00;02.10.25 09:00;GOOD
                """;

        var testSession = prepareTestSession(sleepData);

        MinDurationSleepSessionCounter f = new MinDurationSleepSessionCounter();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(Duration.ofHours(8), result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }

}
