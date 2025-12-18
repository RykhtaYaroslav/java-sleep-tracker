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

public class BadQualitySleepingSessionsCountTest {
    public List<SleepingSession> prepareTestSession(String sleepData) throws IOException {
        Path testPath = Paths.get("testFile.txt");

        Files.writeString(testPath, sleepData, StandardCharsets.UTF_8);

        SleepLogReader sleepLogReader = new SleepLogReader(testPath);

        return sleepLogReader.readLog();
    }

    @Test
    public void shouldReturnZeroBadSessions() throws IOException {
        String sleepData = """
                01.10.25 23:59;02.10.25 00:00;GOOD
                """;

        var testSession = prepareTestSession(sleepData);

        BadQualitySleepingSessionsCount f = new BadQualitySleepingSessionsCount();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(0L, result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }

    @Test
    public void shouldReturn3BadSessions() throws IOException {
        String sleepData = """
                01.10.25 23:00;02.10.25 07:00;BAD
                02.10.25 23:00;03.10.25 07:00;GOOD
                03.10.25 23:00;04.10.25 07:00;NORMAL
                04.10.25 23:00;05.10.25 07:00;BAD
                05.10.25 23:00;06.10.25 07:00;BAD
                """;

        var testSession = prepareTestSession(sleepData);

        BadQualitySleepingSessionsCount f = new BadQualitySleepingSessionsCount();

        SleepAnalysisResult<?> result = f.apply(testSession);

        Assertions.assertEquals(3L, result.getValue());
        Assertions.assertTrue(Files.deleteIfExists(Paths.get("testFile.txt")));
    }


}
