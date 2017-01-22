package LockDemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * http://www.liuinsect.com/2014/01/27/how_to_understand_condition/
 */
public class LockConditionDemo {


    public static void main(String[] args) {
        final ReentrantLock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("1--->我需要一个信号");
                try {
                    condition.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("1--->我拿到一个信号");
                    lock.unlock();
                }
            }
        });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("2--->我拿到锁了");
                try {
                    condition.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("2--->我发了个信号");
                    lock.unlock();
                }
            }
        });

        thread.start();
        thread1.start();
    }
}
