package com.xwdz.http.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class EasyThreadPools {

    private static ExecutorService sDefaultExecutorService;

    private static AtomicInteger sAtomicInteger = new AtomicInteger();

    static {

        final ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                final Thread thread = new Thread(r);
                thread.setName("EasyNetwork#" + sAtomicInteger.getAndIncrement());
                return thread;
            }
        };
        sDefaultExecutorService = Executors.newCachedThreadPool(threadFactory);
    }

    public static void setDefaultExecutorService(ExecutorService defaultExecutorService) {
        sDefaultExecutorService = defaultExecutorService;
    }


    public static void executor(Runnable runnable) {
        sDefaultExecutorService.execute(runnable);
    }
}
