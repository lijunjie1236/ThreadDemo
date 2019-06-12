package com.example.threaddemo;


/**
 * 1.开启一个子线程和主线程同一时候运行，子线程先输出10次后接着主线程输出20次，如此重复5次
 */
public class Bussies {

    //flage为true时打印主线程，为false时打印子线程
    private volatile boolean flag = false;


    public static final int repeatCount = 5;


    public void main() {
        synchronized (this) {
            while (!flag) {
                try {
                    this.wait(); //false时等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < 20; i++) {
                System.out.println(Thread.currentThread().getName() + "执行了" + (i + 1) + "次");
            }
            flag = false;
            this.notifyAll();

        }
    }

    public void sub() {
        synchronized (this) {
            while (flag) {
                try {
                    this.wait(); //true时等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "执行了" + (i + 1) + "次");
            }
            flag = true;
            this.notifyAll();

        }
    }

}
