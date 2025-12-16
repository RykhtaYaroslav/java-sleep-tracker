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

public class SleeplessNightsCountTest {
    public List<SleepingSession> prepareTestSession(String sleepData) throws IOException {
        Path testPath = Paths.get("testFile.txt");

        Files.writeString(testPath, sleepData, StandardCharsets.UTF_8);

        SleepLogReader sleepLogReader = new SleepLogReader(testPath);

        return sleepLogReader.readLog();
    }

    @Test
    public void shouldReturnZeroNightWhenAsleepAndGetUpAtDifferentDate() throws IOException {
        String sleepData = """
                30.09.25 23:59;01.10.25 00:01;GOOD
                """;

        var testSession = prepareTestSession(sleepData);

        SleeplessNightsCount f = new SleeplessNightsCount();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(0L, result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }

    @Test
    public void shouldReturnOneNightWhenWasNoSessionsFrom0To6() throws IOException {
        String sleepData = """
                30.09.25 23:59;01.10.25 00:01;GOOD
                01.10.25 18:00;01.10.25 23:59;GOOD
                """;

        var testSession = prepareTestSession(sleepData);

        SleeplessNightsCount f = new SleeplessNightsCount();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(1L, result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }

    @Test
    public void shouldReturnOneWhenOnlyOneSleepNotAtNight() throws IOException {
        String sleepData = """
                01.10.25 06:00;01.10.25 11:00;GOOD
                """;

        var testSession = prepareTestSession(sleepData);

        SleeplessNightsCount f = new SleeplessNightsCount();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(1L, result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }

    @Test
    public void shouldReturnZeroWhenAsleepBefore6() throws IOException {
        String sleepData = """
                01.10.25 05:59;01.10.25 06:00;GOOD
                """;

        var testSession = prepareTestSession(sleepData);

        SleeplessNightsCount f = new SleeplessNightsCount();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(0L, result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }
}
