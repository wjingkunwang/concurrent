package AbstractQueuedSynchronizerDemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wjk on 17/1/20.
 */
public class MutexTest {
    static boolean empty = true;
    static Mutex mut = new Mutex();//互斥锁

    static class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                //加锁
                mut.lock();
                try {
                    //条件不满足，继续等待
                    if (empty) {
                        continue;
                    }
                    //条件满足，完成工作
                    System.out.println("从盘子里拿一个苹果！");
                    break;
                } finally {
                    mut.unlock();
                }
            }
        }
    }

    static class Produce implements Runnable {
        @Override
        public void run() {
            //加锁
            mut.lock();
            try {
                //该变条件
                System.out.println("向盘子里放入一个苹果！");
                empty = false;
            } finally {
                mut.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService consumerService = Executors.newFixedThreadPool(10);
        consumerService.submit(new Consumer());


//        Thread consumerThread=new Thread(new Consumer());
//        consumerThread.start();
        ExecutorService produceService = Executors.newFixedThreadPool(10);
        produceService.submit(new Produce());

//        Thread produceThread=new Thread(new Produce());
//        produceThread.start();

    }
}