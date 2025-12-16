package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.customExceptions.SleepLogException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepLogReaderTest {
    @Test
    public void shouldThrowSleepLogExceptionWhenLogIsEmpty() throws IOException {
        Path test;

        String name = "testFile " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy_HH.mm")) + ".txt";

        try(FileWriter fr = new FileWriter(name, StandardCharsets.UTF_8, true)) {
            test = Paths.get(name);

            fr.write("\n");
        }
        SleepLogReader sleepLogReader = new SleepLogReader(test);

        Assertions.assertThrows(SleepLogException.class, sleepLogReader::readLog);

        Assertions.assertTrue(Files.deleteIfExists(test));
    }

    @Test
    public void shouldReturnListWithCorrectSize() throws IOException {
        String name1 = "testFile1 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy_HH.mm")) + ".txt";

        String name2 = "testFile2 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy_HH.mm")) + ".txt";

        Path test1 = Paths.get(name1);

        Path test2 = Paths.get(name2);

        String sleepData1 = """
            01.10.25 23:15;02.10.25 07:30;GOOD
            02.10.25 23:50;03.10.25 06:40;NORMAL
            03.10.25 14:10;03.10.25 15:00;NORMAL
            03.10.25 23:40;04.10.25 08:00;BAD
            05.10.25 00:10;05.10.25 06:20;GOOD
            05.10.25 13:30;05.10.25 14:15;NORMAL
            06.10.25 22:30;07.10.25 05:50;GOOD
            07.10.25 23:45;08.10.25 06:30;GOOD
            08.10.25 23:50;09.10.25 07:10;GOOD
            10.10.25 13:00;10.10.25 14:30;NORMAL
            10.10.25 23:55;11.10.25 06:10;GOOD
            11.10.25 23:10;12.10.25 07:00;BAD
            30.10.25 23:50;31.10.25 06:30;GOOD
            """;

        String sleepData2 = """
            01.10.25 23:15;02.10.25 07:30;GOOD
            """;

        Files.writeString(test1, sleepData1, StandardCharsets.UTF_8);

        Files.writeString(test2, sleepData2, StandardCharsets.UTF_8);

        SleepLogReader sleepLogReader1 = new SleepLogReader(test1);

        SleepLogReader sleepLogReader2 = new SleepLogReader(test2);

        var testSessions1 = sleepLogReader1.readLog();

        var testSessions2 = sleepLogReader2.readLog();

        Assertions.assertEquals(13, testSessions1.size());
        Assertions.assertEquals(1, testSessions2.size());
        Assertions.assertTrue(Files.deleteIfExists(test1));
        Assertions.assertTrue(Files.deleteIfExists(test2));
    }

    @Test
    public void shouldThrowSleepLogExceptionWhenIncorrectDataInFile() throws IOException {
        String name1 = "testFile1 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy_HH.mm")) + ".txt";

        String name2 = "testFile2 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy_HH.mm")) + ".txt";

        Path test1 = Paths.get(name1);

        Path test2 = Paths.get(name2);

        String sleepData1 = """
            01.10.25 23:15;02.10.25 07:30;ХОРОШО
            """;

        String sleepData2 = """
            01.13.25 23:15;02.10.25 07:30;GOOD
            """;

        Files.writeString(test1, sleepData1, StandardCharsets.UTF_8);

        Files.writeString(test2, sleepData2, StandardCharsets.UTF_8);

        SleepLogReader sleepLogReader1 = new SleepLogReader(test1);

        SleepLogReader sleepLogReader2 = new SleepLogReader(test2);

        Assertions.assertThrows(SleepLogException.class, sleepLogReader1::readLog);
        Assertions.assertThrows(SleepLogException.class, sleepLogReader2::readLog);
        Assertions.assertTrue(Files.deleteIfExists(test1));
        Assertions.assertTrue(Files.deleteIfExists(test2));
    }


}
