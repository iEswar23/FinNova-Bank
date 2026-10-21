package com.eswar.finnova.util;

import android.os.Handler;
import android.os.Looper;

/** Posts work to the main (UI) thread after a delay, to simulate a network call. */
public class MainThreadTaskRunner implements TaskRunner {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void run(Runnable task, long delayMillis) {
        handler.postDelayed(task, delayMillis);
    }
}
