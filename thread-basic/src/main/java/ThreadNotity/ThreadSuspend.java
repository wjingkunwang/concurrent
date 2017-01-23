package ThreadNotity;

/**
 * Thread.suspend很容易死锁。如果目标线程挂起来，他将给监听器上锁用以保护重要的系统资源，
 * 其他线程将不能访问该资源直到目标线程恢复工作。
 * 如果线程在恢复一个企图给监听器加锁的线程前调用了resume方法，则导致死锁。
 *
 *
 *
 *
 * 下面的例子会产生死锁，println为同步方法，PrintStream为static修饰的成员变量，
 * 而suspend()方法挂起线程但并不释放锁，在线程mt被挂起后主线程调用System.out.println
 * 同样需要获取System类out对象的同步锁才能打印“can you get here?”，
 * 主线程一直在等待同步锁而mt线程不释放锁，这就导致了死锁的产生。
 */
public class ThreadSuspend {
    public static void main(String[] args) {

        Thread mt = new MyThread();

        mt.start();

        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        mt.suspend();
        System.out.println("canyou get here?");
        mt.resume();

    }

    static class MyThread extends Thread {

        public void run() {

            while (true) {

                System.out.println("running....");

            }

        }

    }

}
