package com.eswar.finnova.util;

/** Runs a task after a delay. Lets us fake network latency in the app and run instantly in tests. */
public interface TaskRunner {
    void run(Runnable task, long delayMillis);

    /** Runs immediately on the calling thread. Use in unit tests. */
    TaskRunner IMMEDIATE = (task, delay) -> task.run();
}
