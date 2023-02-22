package ru.job4j.todo.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public class TimeConfig {

    public static LocalDateTime userZoneConfig(LocalDateTime time, ZoneId from, ZoneId to) {
        return time.atZone(from).withZoneSameInstant(to).toLocalDateTime();
    }
}
