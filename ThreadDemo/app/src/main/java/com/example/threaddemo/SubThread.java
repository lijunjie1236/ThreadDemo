package com.example.threaddemo;

/**
 * 子线程
 */
public class SubThread implements Runnable {

    private Bussies bussies;

    public SubThread(Bussies bussies) {
        this.bussies = bussies;
    }

    public Bussies getBussies() {
        return bussies;
    }

    public void setBussies(Bussies bussies) {
        this.bussies = bussies;
    }

    @Override
    public void run() {
        for (int i = 0; i < Bussies.repeatCount; i++) {
            bussies.sub();
        }

    }
}
