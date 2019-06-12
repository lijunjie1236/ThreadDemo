package com.example.threaddemo.ThreadPool;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 优化线程池 ThreadPoolExecutor
 * 通常核心线程数可以设为CPU数量+1，而最大线程数可以设为CPU的数量*2+1。
 * shutdown() 方法在终止前允许执行以前提交的任务。
 * shutdownNow() 方法则是阻止正在任务队列中等待任务的启动并试图停止当前正在执行的任务。
 */
public class MyThreadPool extends ThreadPoolExecutor {

    private static final String TAG = MyThreadPool.class.getSimpleName();

    public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        int mSupprtCore = Runtime.getRuntime().availableProcessors() + 1;
        if (corePoolSize >= mSupprtCore) {
            Log.e(TAG, "support corePoolSize：" + mSupprtCore);
        }
        int mSupportMax = Runtime.getRuntime().availableProcessors() * 2 + 1;
        if (maximumPoolSize > mSupportMax) {
            Log.e(TAG, "support corePoolSize：" + mSupportMax);
        }

    }


    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        Log.d(TAG, t.getName()+"beforeExecute: 开始执行任务！");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        Log.d(TAG, "beforeExecute: 任务执行结束！");
    }

    @Override
    protected void terminated() {
        super.terminated();
        Log.d(TAG, "terminated: 线程池关闭！");
    }

}
