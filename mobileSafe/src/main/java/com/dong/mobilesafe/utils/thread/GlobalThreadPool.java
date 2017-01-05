package com.dong.mobilesafe.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/4/1 0001.
 */
public class GlobalThreadPool {
    private static GlobalThreadPool instance = null;
    private ExecutorService threadPool = Executors.newCachedThreadPool(new GlobalThreadFactory());
    private GlobalThreadPool() {

    }

    private static class GlobalThreadFactory implements ThreadFactory {
        private AtomicInteger threatIndex = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.NORM_PRIORITY-2);
            thread.setName("global_thread_"+threatIndex.getAndAdd(1));
            return thread;
        }
    }


    public static GlobalThreadPool getInstance() {
        if (instance == null) {
            synchronized (GlobalThreadPool.class) {
                if (instance == null) {
                    instance = new GlobalThreadPool();
                }
            }
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }






}
