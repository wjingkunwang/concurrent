package CountDownLatchDemo;

import java.util.concurrent.CountDownLatch;

/**
 *
 protected int tryAcquireShared(int acquires) {
 return (getState() == 0) ? 1 : -1;
 }
 获取锁的代码咋一看想是互斥锁的实现，但是你要注意到不不仅仅是主线程可以获取锁，其他的线程也同样可以，只要getState() == 0
 *
 */
public class CountDownLatchDemo {
    private static int LATCH_SIZE = 5;
    private static CountDownLatch doneSignal;

    public static void main(String[] args) {

        try {
            doneSignal = new CountDownLatch(LATCH_SIZE);

            // 新建5个任务
            for (int i = 0; i < LATCH_SIZE; i++)
                new InnerThread().start();

            System.out.println("main await begin.");
            // "主线程"等待线程池中5个任务的完成
            doneSignal.await();

            System.out.println("main await finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class InnerThread extends Thread {
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " sleep 1000ms.");
                // 将CountDownLatch的数值减1
                doneSignal.countDown();

                /**
                 * 添加如下修改表示其实共享的
                 */
                doneSignal.await();
                System.out.println("11111111111");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
