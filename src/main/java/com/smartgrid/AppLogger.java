package com.smartgrid;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppLogger {
    private static final List<String> logs = Collections.synchronizedList(new ArrayList<>());
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void info(String message) {
        addLog("INFO", message);
    }

    public static void warn(String message) {
        addLog("WARNING", message);
    }

    public static void alert(String message) {
        addLog("ALERT", message);
    }

    private static void addLog(String level, String message) {
        String time = LocalTime.now().format(dtf);
        String formatted = "[" + time + "] [" + level + "] " + message;
        System.out.println(formatted);
        logs.add(formatted);
        
        // Retain last 100 log lines
        if (logs.size() > 100) {
            logs.remove(0);
        }
    }

    public static List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    public static void clear() {
        logs.clear();
    }
}