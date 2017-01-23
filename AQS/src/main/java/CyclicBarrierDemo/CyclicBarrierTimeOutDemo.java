package CyclicBarrierDemo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by wjk on 17/1/23.
 */
public class CyclicBarrierTimeOutDemo {
    private static int SIZE = 5;
    private static CyclicBarrier cb;

    public static void main(String[] args) {

        cb = new CyclicBarrier(SIZE, new Runnable() {
            public void run() {
                System.out.println("CyclicBarrier's parties is: " + cb.getParties());
            }
        });


        // 新建5个任务
        for (int i = 0; i < SIZE - 1; i++)
            new CyclicBarrierTimeOutDemo.InnerThread().start();

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread thread = new CyclicBarrierTimeOutDemo.InnerThread();
        thread.start();
    }

    static class InnerThread extends Thread {
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " wait for CyclicBarrier.");

                // 将cb的参与者数量加1
                cb.await(1000, TimeUnit.MILLISECONDS);
                // cb的参与者数量等于5时，才继续往后执行
                System.out.println(Thread.currentThread().getName() + " continued.");
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
