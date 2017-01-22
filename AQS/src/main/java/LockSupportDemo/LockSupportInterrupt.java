package LockSupportDemo;

import java.util.concurrent.locks.LockSupport;

/**
 * 线程如果因为调用park而阻塞的话，能够响应中断请求(中断状态被设置成true)，但是不会抛出InterruptedException
 */
public class LockSupportInterrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            private int count = 0;
            @Override
            public void run()
            {
                long start = System.currentTimeMillis();
                long end = 0;

                while ((end - start) <= 1000)
                {
                    count++;
                    end = System.currentTimeMillis();
                }

                System.out.println("after 1 second.count=" + count);

                //等待或许许可
                LockSupport.park();
                System.out.println("thread over." + Thread.currentThread().isInterrupted());

            }
        });

        t.start();
        Thread.sleep(2000);
        // 中断线程
        t.interrupt();
        System.out.println("main over");
    }
}