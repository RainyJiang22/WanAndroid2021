package com.base.wanandroid.utils;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class HandlerFactory {
    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    private static final Handler QUICK;
    private static final Handler LAST;
    private static final Handler DB;

    static {
        HandlerThread thread = new HandlerThread("quick-handler");
        thread.start();
        QUICK = new Handler(thread.getLooper());

        thread = new HandlerThread("last-handler");
        thread.start();
        LAST = new Handler(thread.getLooper());

        thread = new HandlerThread("db-handler");
        thread.start();
        DB = new Handler(thread.getLooper());
    }

    public static Handler main() {
        return MAIN;
    }

    public static Handler quick() {
        return QUICK;
    }

    public static Handler last() {
        return LAST;
    }

    public static Handler db() {
        return DB;
    }
}
