package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepLogReader;
import ru.yandex.practicum.sleeptracker.SleepingSession;
import ru.yandex.practicum.sleeptracker.UserChronotype;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ChronotypeSleepAnalyzerTest {
    public List<SleepingSession> prepareTestSession(String sleepData) throws IOException {
        Path testPath = Paths.get("testFile.txt");

        Files.writeString(testPath, sleepData, StandardCharsets.UTF_8);

        SleepLogReader sleepLogReader = new SleepLogReader(testPath);

        return sleepLogReader.readLog();
    }

    @Test
    public void shouldReturnOwlWhenSleep23_01_09_01AndReturnPigeonWhenAsleepBefore23_01OrGetUpBefore09_01() throws IOException {
        ChronotypeSleepAnalyzer f = new ChronotypeSleepAnalyzer();
        Path path = Paths.get("testFile.txt");

        var testSession1 = prepareTestSession("""
                01.10.25 23:01;02.10.25 09:01;GOOD
                """);
        SleepAnalysisResult<?> result1 = f.apply(testSession1);
        Assertions.assertEquals(UserChronotype.OWL, result1.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));

        var testSession2 = prepareTestSession("""
                01.10.25 23:00;02.10.25 09:01;GOOD
                """);
        SleepAnalysisResult<?> result2 = f.apply(testSession2);
        Assertions.assertEquals(UserChronotype.PIGEON, result2.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));

        var testSession3 = prepareTestSession("""
                01.10.25 23:01;02.10.25 09:00;GOOD
                """);
        SleepAnalysisResult<?> result3 = f.apply(testSession3);
        Assertions.assertEquals(UserChronotype.PIGEON, result3.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));

    }

    @Test
    public void shouldReturnLarkWhenASleepBefore22GetUpBefore7AndReturnPigeonWhenAsleepAt22OrGetUpAt7() throws IOException {
        ChronotypeSleepAnalyzer f = new ChronotypeSleepAnalyzer();
        Path path = Paths.get("testFile.txt");

        var testSession1 = prepareTestSession("""
                01.10.25 21:59;02.10.25 06:59;GOOD
                """);
        SleepAnalysisResult<?> result1 = f.apply(testSession1);
        Assertions.assertEquals(UserChronotype.LARK, result1.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));

        var testSession2 = prepareTestSession("""
                01.10.25 22:00;02.10.25 06:59;GOOD
                """);
        SleepAnalysisResult<?> result2 = f.apply(testSession2);
        Assertions.assertEquals(UserChronotype.PIGEON, result2.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));

        var testSession3 = prepareTestSession("""
                01.10.25 21:59;02.10.25 07:00;GOOD
                """);
        SleepAnalysisResult<?> result3 = f.apply(testSession3);
        Assertions.assertEquals(UserChronotype.PIGEON, result3.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));

    }

    @Test
    public void shouldReturnLark() throws IOException {
        ChronotypeSleepAnalyzer f = new ChronotypeSleepAnalyzer();
        Path path = Paths.get("testFile.txt");

        var testSession1 = prepareTestSession("""
                01.10.25 21:30;02.10.25 06:00;GOOD
                02.10.25 15:30;02.10.25 17:00;GOOD
                02.10.25 21:30;03.10.25 06:00;GOOD
                03.10.25 15:30;03.10.25 17:00;GOOD
                03.10.25 21:30;04.10.25 06:00;GOOD
                04.10.25 15:30;04.10.25 17:00;GOOD
                04.10.25 21:30;05.10.25 06:00;GOOD
                05.10.25 15:30;05.10.25 17:00;GOOD
                05.10.25 21:30;06.10.25 06:00;GOOD
                06.10.25 15:30;06.10.25 17:00;GOOD
                07.10.25 15:30;07.10.25 17:00;GOOD
                """);
        SleepAnalysisResult<?> result1 = f.apply(testSession1);
        Assertions.assertEquals(UserChronotype.LARK, result1.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));

        var testSession2 = prepareTestSession("""
                01.10.25 21:30;02.10.25 06:00;GOOD
                02.10.25 23:30;03.10.25 09:30;GOOD
                03.10.25 21:30;04.10.25 06:00;GOOD
                04.10.25 23:30;05.10.25 09:30;GOOD
                05.10.25 15:30;05.10.25 17:00;GOOD
                05.10.25 21:30;06.10.25 06:00;GOOD
                """);
        SleepAnalysisResult<?> result2 = f.apply(testSession2);
        Assertions.assertEquals(UserChronotype.LARK, result2.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));
    }

    @Test
    public void shouldReturnOwl() throws IOException {
        ChronotypeSleepAnalyzer f = new ChronotypeSleepAnalyzer();
        Path path = Paths.get("testFile.txt");

        var testSession1 = prepareTestSession("""
                01.10.25 23:30;02.10.25 09:30;GOOD
                02.10.25 15:30;02.10.25 17:00;GOOD
                02.10.25 23:30;03.10.25 09:30;GOOD
                03.10.25 15:30;03.10.25 17:00;GOOD
                03.10.25 23:30;04.10.25 09:30;GOOD
                04.10.25 15:30;04.10.25 17:00;GOOD
                05.10.25 23:30;06.10.25 09:30;GOOD
                05.10.25 15:30;05.10.25 17:00;GOOD
                """);
        SleepAnalysisResult<?> result1 = f.apply(testSession1);
        Assertions.assertEquals(UserChronotype.OWL, result1.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));

        var testSession2 = prepareTestSession("""
                01.10.25 23:30;02.10.25 09:30;GOOD
                02.10.25 23:30;03.10.25 09:30;GOOD
                03.10.25 21:30;04.10.25 06:00;GOOD
                04.10.25 23:30;05.10.25 09:30;GOOD
                05.10.25 15:30;05.10.25 17:00;GOOD
                05.10.25 21:30;06.10.25 06:00;GOOD
                """);
        SleepAnalysisResult<?> result2 = f.apply(testSession2);
        Assertions.assertEquals(UserChronotype.OWL, result2.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));
    }

    @Test
    public void shouldReturnPigeon() throws IOException {
        ChronotypeSleepAnalyzer f = new ChronotypeSleepAnalyzer();
        Path path = Paths.get("testFile.txt");

        var testSession1 = prepareTestSession("""
                01.10.25 23:30;02.10.25 09:30;GOOD
                02.10.25 21:30;03.10.25 06:30;GOOD
                """);
        SleepAnalysisResult<?> result1 = f.apply(testSession1);
        Assertions.assertEquals(UserChronotype.PIGEON, result1.getValue());
        Assertions.assertTrue(Files.deleteIfExists(path));
    }
}
