package com.example.threaddemo.ThreadPool;

public abstract class PriorityRunnable implements Comparable<PriorityRunnable> ,Runnable{

    private int priority;

    public PriorityRunnable(int priority) {
        if (priority < 0)
            throw new IllegalArgumentException();
        this.priority = priority;
    }

    //如果 > 0 则表示 o1 > o2 降序; 如果 < 0 则 o1 < o2 升序;
    @Override
    public int compareTo(PriorityRunnable another) {
        int my = this.getPriority();
        int other = another.getPriority();
        return my < other ? 1 : my > other ? -1 : 0;
    }

    @Override
    public void run() {
        doSth();
    }

    public abstract void doSth();

    public int getPriority() {
        return priority;
    }
}
