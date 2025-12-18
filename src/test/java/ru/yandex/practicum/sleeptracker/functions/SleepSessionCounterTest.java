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

public class SleepSessionCounterTest {
    public List<SleepingSession> prepareTestSession(String sleepData) throws IOException {
        Path testPath = Paths.get("testFile.txt");

        Files.writeString(testPath, sleepData, StandardCharsets.UTF_8);

        SleepLogReader sleepLogReader = new SleepLogReader(testPath);

        return sleepLogReader.readLog();
    }

    @Test
    public void shouldReturnZeroWhenEmptyData() throws IOException {
        String sleepData = """
                """;

        var testSession = prepareTestSession(sleepData);

        SleepSessionCounter f = new SleepSessionCounter();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(0, result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }

    @Test
    public void shouldReturnOne() throws IOException {
        String sleepData = """
                30.09.25 23:59;01.10.25 00:01;GOOD
                """;

        var testSession = prepareTestSession(sleepData);

        SleepSessionCounter f = new SleepSessionCounter();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(1, result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }

    @Test
    public void shouldReturnFive() throws IOException {
        String sleepData = """
                01.10.25 23:00;02.10.25 07:00;BAD
                02.10.25 23:00;03.10.25 07:00;GOOD
                03.10.25 23:00;04.10.25 07:00;NORMAL
                04.10.25 23:00;05.10.25 07:00;BAD
                05.10.25 23:00;06.10.25 07:00;BAD
                """;

        var testSession = prepareTestSession(sleepData);

        SleepSessionCounter f = new SleepSessionCounter();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(5, result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }
}
