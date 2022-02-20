package com.siwei.darwin.common.util;


import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorTool implements Closeable {

    private final static ExecutorService executorService = Executors.newSingleThreadExecutor((r) -> {
        Thread thread = new Thread(r);
        thread.setName("ExecutorTool");
        thread.setDaemon(true);
        return thread;
    });

    public static ExecutorService Instance() {
        return executorService;
    }

    public void close() {
        executorService.shutdown();
    }

}
