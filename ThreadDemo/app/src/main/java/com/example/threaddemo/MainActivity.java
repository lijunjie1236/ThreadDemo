package com.example.threaddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.threaddemo.ThreadPool.MyThreadPool;
import com.example.threaddemo.ThreadPool.PriorityRunnable;

import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 多线程学习
 *
 *  线程的状态
    New（新生）：线程对象刚刚被创建出来；

 ·  Runnable（可运行）：在线程对象上调用start方法后，相应线程便会进入Runnable状态，若被线程调度程序调度，这个线程便会成为当前运行（Running）的线程；

 ·  Blocked（被阻塞）：若一段代码被线程A”上锁“，此时线程B尝试执行这段代码，线程B就会进入Blocked状态；

 ·  Waiting（等待）：当线程等待另一个线程通知线程调度器一个条件时，它本身就会进入Waiting状态；

 ·  Time Waiting（计时等待）：计时等待与等待的区别是，线程只等待一定的时间，若超时则不再等待；

 ·  Terminated（被终止）：线程的run方法执行完毕或者由于一个未捕获的异常导致run方法意外终止会进入Terminated状态。
 ---------------------
   sleep():让当前线程进入休眠状态（但线程不会释放已获取的锁），这个休眠状态其实就是Time Waiting状态，从休眠状态“苏醒”后，线程会进入到Runnable状态。
   join():这是一个实例方法，在当前线程中对一个线程对象调用join方法会导致当前线程停止运行，等那个线程运行完毕后再接着运行当前线程。也就是说，把当前线程还没执行的部分“接到”另一个线程后面去，
          另一个线程运行完毕后，当前线程再接着运行。无参数的join表示当前线程一直等到另一个线程运行完毕，
          这种情况下当前线程会处于Wating状态；带参数的表示当前线程只等待指定的时间，这种情况下当前线程会处于Time Waiting状态.
   yield():这是一个静态方法，作用是让当前线程“让步”，目的是为了让优先级不低于当前线程的线程有机会运行，这个方法不会释放锁。
   interrupt():每个线程都有一个中断状态标识，这个方法的作用就是将相应线程的中断状态标记为true，这样相应的线程调用isInterrupted方法就会返回true。
             通过使用这个方法，能够终止那些通过调用可中断方法进入阻塞状态的线程。常见的可中断方法有sleep、wait、join，这些方法的内部实现会时不时的检查当前线程的中断状态，若为true会立刻抛出一个InterruptedException异常，从而终止当前线程。
   wait():wait方法是Object类中定义的实例方法。在指定对象上调用wait方法能够让当前线程进入阻塞状态（前提时当前线程持有该对象的内部锁（monitor））,此时当前线程会释放已经获取的那个对象的内部锁，
          这样一来其他线程就可以获取这个对象的内部锁了。当其他线程获取了这个对象的内部锁，进行了一些操作后可以调用notify方法来唤醒正在等待该对象的线程。

   notify/notifyAll:方法也是Object类中定义的实例方法。它俩的作用是唤醒正在等待相应对象的线程，区别在于前者唤醒一个等待该对象的线程，而后者唤醒所有等待该对象的线程


 volatile关键字在Java中有什么作用？(可见性)
 当一个共享变量被volatile修饰时，它会保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值。
 当我们使用volatile关键字去修饰变量的时候，所以线程都会直接读取该变量并且不缓存它。这就确保了线程读取到的变量是同内存中是一致的
 */
public class MainActivity extends AppCompatActivity {
    Thread main,sub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bussies bussies=new Bussies();
        MainThread mainThread=new MainThread(bussies);
        SubThread subThread=new SubThread(bussies);
        main=new Thread(mainThread,"MainThread");
        sub=new Thread(subThread,"SubThread");
    }

    int k = 0;
    public void test(View view) {
        //i++ 是线程不安全
        /**
         * 每个线程都有自己的工作内存，每个线程需要对共享变量操作时必须先把共享变量从主内存 load 到自己的工作内存，等完成对共享变量的操作时再 save 到主内存。
         * 问题就出在这了，如果一个线程运算完后还没刷到主内存，此时这个共享变量的值被另外一个线程从主内存读取到了，这个时候读取的数据就是脏数据了，它会覆盖其他线程计算完的值。
         * 可参考drawable/memorymodel.mepg 内存模型
         */
         for(int i=0;i<20;i++){
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     System.out.println(Thread.currentThread().getName()+"_"+(++k));
                 }
             }).start();
         }
    }

    /**
     * 1.开启一个子线程和主线程同一时候运行，子线程先输出10次后接着主线程输出20次，如此重复5次
     */
    public void method1(View view) {
        main.start();
        sub.start();
    }


    public void method2(View view) {

    }


    /**
     * 优先级线程池
     *
     * @param view
     */
    public void method3(View view) {
        MyThreadPool executor = new MyThreadPool(3, 3, 0, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
        for (int i = 0; i < 30; i++) {
        final int priority = i;
        PriorityRunnable runnable = new PriorityRunnable(priority) {
            @Override
            public void doSth() {
                String threadName = Thread.currentThread().getName();
                Log.v("TAG", "线程：" + threadName + ",正在执行优先级为：" + priority + "的任务");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        executor.execute(runnable);

        //刚开始时，系统有 3 个空闲线程，所以无须使用任务队列，而是直接运行前三个任务，而后面再提交任务时由于当前没有空闲线程所以加入任务队列中进行等待，
        // 此时，由于我们的任务队列实现是由 PriorityBlockingQueue 实现的，所以进行等待的任务会经过优先级判断，优先级高的放在队列前面先处理。
        // 从效果图中也可以看到后面的任务是先执行优先级高的任务，然后依次递减。

    }

}
}
