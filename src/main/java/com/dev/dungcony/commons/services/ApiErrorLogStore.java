package com.dev.dungcony.commons.services;

import com.dev.dungcony.commons.dtos.ApiErrorLogEntry;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Component
public class ApiErrorLogStore {
    private static final int MAX_LOG_SIZE = 200;
    private final Deque<ApiErrorLogEntry> logs = new ArrayDeque<>();

    public synchronized void add(ApiErrorLogEntry entry) {
        logs.addLast(entry);
        while (logs.size() > MAX_LOG_SIZE) {
            logs.removeFirst();
        }
    }

    public synchronized List<ApiErrorLogEntry> getAll() {
        return new ArrayList<>(logs);
    }
}
