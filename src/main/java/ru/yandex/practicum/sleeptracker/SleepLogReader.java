package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.customExceptions.SleepLogException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class SleepLogReader {
    private final Path sleepLog;


    public SleepLogReader(Path sleepLog) {
        this.sleepLog = sleepLog;
    }

    public List<SleepingSession> readLog() throws FileNotFoundException {
        try (BufferedReader bf = new BufferedReader(new FileReader(sleepLog.toFile()))) {
            return bf.lines().map(line -> makeNewSleepingSession(line.split(";")).orElseThrow(() -> new SleepLogException("Не удалось обработать строку" + line))).toList();
        } catch (IOException e) {
            throw new FileNotFoundException("Не удалось найти лог-файл с данными о сне");
        }

    }

    private Optional<SleepingSession> makeNewSleepingSession(String[] data) {
        if (data.length != 3) {
            return Optional.empty();
        }
        Optional<LocalDateTime> asleep = parseDateTime(data[0]);
        Optional<LocalDateTime> getUp = parseDateTime(data[1]);
        Optional<SleepQuality> quality = parseSleepQuality(data[2]);
        return asleep.flatMap(a -> getUp.filter(a::isBefore).flatMap(g -> quality.map(q -> new SleepingSession(a, g, q))));

    }

    private Optional<LocalDateTime> parseDateTime(String p) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(p, formatter);
            return Optional.of(localDateTime);
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    private Optional<SleepQuality> parseSleepQuality(String qualityStr) {
        return switch (qualityStr.trim().toUpperCase()) {
            case "GOOD" -> Optional.of(SleepQuality.GOOD);
            case "NORMAL" -> Optional.of(SleepQuality.NORMAL);
            case "BAD" -> Optional.of(SleepQuality.BAD);
            default -> Optional.empty();
        };


//    private Optional<SleepingSession> makeNewSleepingSession(String[] parts) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
//
//        LocalDateTime asleep = LocalDateTime.parse(parts[0], formatter);
//
//        LocalDateTime getUp = LocalDateTime.parse(parts[1], formatter);
//
//        if (asleep.isAfter(getUp) || asleep.isEqual(getUp)){
//            return Optional.empty();
//        }
//
//        SleepQuality quality;
//
//        switch (parts[2]) {
//            case "GOOD":
//                quality = SleepQuality.GOOD;
//                break;
//            case "NORMAL":
//                quality = SleepQuality.NORMAL;
//                break;
//            case "BAD":
//                quality = SleepQuality.BAD;
//                break;
//            default:
//                return Optional.empty();
//        }
//
//        SleepingSession sleepingSession = new SleepingSession(asleep, getUp, quality);
//
//        return Optional.of(sleepingSession);
//    }
    }
}
