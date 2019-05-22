package com.xwdz.http.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class EasyThreadPools {

    private static ExecutorService sDefaultExecutorService;

    private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors() * 2 + 1;

    private static AtomicInteger sAtomicInteger = new AtomicInteger();

    static {

        final ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                final Thread thread = new Thread(r);
                thread.setName("EasyNetwork#" + sAtomicInteger.getAndDecrement());
                return thread;
            }
        };


        sDefaultExecutorService = new ThreadPoolExecutor(CORE_SIZE, Integer.MAX_VALUE, 30,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(),
                threadFactory);
    }

    public static void setDefaultExecutorService(ExecutorService defaultExecutorService) {
        sDefaultExecutorService = defaultExecutorService;
    }


    public static void executor(Runnable runnable) {
        sDefaultExecutorService.execute(runnable);
    }
}
