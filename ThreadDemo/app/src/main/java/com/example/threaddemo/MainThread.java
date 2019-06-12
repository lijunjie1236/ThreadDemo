package com.example.threaddemo;

/**
 * 主线程
 */
public class MainThread implements Runnable {

    private Bussies bussies;

    public MainThread(Bussies bussies) {
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
        for (int i=0;i< Bussies.repeatCount;i++){
            bussies.main();
        }

    }
}
