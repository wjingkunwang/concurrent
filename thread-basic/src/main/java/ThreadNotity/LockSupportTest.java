package ThreadNotity;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 问题4： LockSupport.park()和unpark()，与object.wait()和notify()的区别？
 答：
 1.  面向的主体不一样。LockSuport主要是针对Thread进进行阻塞处理，可以指定阻塞队列的目标对象，每次可以指定具体的线程唤醒。Object.wait()是以对象为纬度，阻塞当前的线程和唤醒单个(随机)或者所有线程。
 2.  实现机制不同。虽然LockSuport可以指定monitor的object对象，但和object.wait()，两者的阻塞队列并不交叉。可以看下测试例子。object.notifyAll()不能唤醒LockSupport的阻塞Thread.
 *
 *
 * 能响应interrupt事件，但不会抛出InterruptedException异常。
 * 针对LockSupport对Thread.interrupte支持，也先看一下javadoc中的描述：
 */
public class LockSupportTest {

    private static LockSupportTest blocker = new LockSupportTest();

    public static void main(String args[]) throws Exception {
        lockSupportTest();
//        parkTest();
//        interruptParkTest();
//        interruptSleepTest();
//        interruptWaitTest();
    }

    /**
     * LockSupport.park对象后，尝试获取Thread.blocker对象，调用其single唤醒
     *
     * @throws Exception
     */
    private static void lockSupportTest() throws Exception {
        Thread t = doTest(new TestCallBack() {

            @Override
            public void callback() throws Exception {
                // 尝试sleep 5s
                System.out.println("blocker");
                LockSupport.park(blocker);
                System.out.println("wakeup now!");
            }

            @Override
            public String getName() {
                return "lockSupportTest";
            }

        });
        t.start(); // 启动读取线程

        Thread.sleep(150);
        synchronized (blocker) {
            Field field = Thread.class.getDeclaredField("parkBlocker");
            field.setAccessible(true);
            Object fBlocker = field.get(t);
            System.out.println(blocker == fBlocker);
            Thread.sleep(100);
            System.out.println("notifyAll");
            blocker.notifyAll();
        }
    }

    /**
     * 尝试去中断一个object.wait()，会抛出对应的InterruptedException异常
     *
     * @throws InterruptedException
     */
    private static void interruptWaitTest() throws InterruptedException {
        final Object obj = new Object();
        Thread t = doTest(new TestCallBack() {

            @Override
            public void callback() throws Exception {
                // 尝试sleep 5s
                obj.wait();
                System.out.println("wakeup now!");
            }

            @Override
            public String getName() {
                return "interruptWaitTest";
            }

        });
        t.start(); // 启动读取线程
        Thread.sleep(2000);
        t.interrupt(); // 检查下在park时，是否响应中断
    }

    /**
     * 尝试去中断一个Thread.sleep()，会抛出对应的InterruptedException异常
     *
     * @throws InterruptedException
     */
    private static void interruptSleepTest() throws InterruptedException {
        Thread t = doTest(new TestCallBack() {

            @Override
            public void callback() throws Exception {
                // 尝试sleep 5s
                Thread.sleep(5000);
                System.out.println("wakeup now!");
            }

            @Override
            public String getName() {
                return "interruptSleepTest";
            }

        });
        t.start(); // 启动读取线程
        Thread.sleep(2000);
        t.interrupt(); // 检查下在park时，是否响应中断
    }

    /**
     * 尝试去中断一个LockSupport.park()，会有响应但不会抛出InterruptedException异常
     *
     * @throws InterruptedException
     */
    private static void interruptParkTest() throws InterruptedException {
        Thread t = doTest(new TestCallBack() {

            @Override
            public void callback() {
                // 尝试去park 自己线程
                LockSupport.parkNanos(blocker, TimeUnit.SECONDS.toNanos(5));
                System.out.println("wakeup now!");
            }

            @Override
            public String getName() {
                return "interruptParkTest";
            }

        });
        t.start(); // 启动读取线程
        Thread.sleep(2000);
        t.interrupt(); // 检查下在park时，是否响应中断
    }

    /**
     * 尝试去中断一个LockSupport.unPark()，会有响应
     *
     * @throws InterruptedException
     */
    private static void parkTest() throws InterruptedException {
        Thread t = doTest(new TestCallBack() {

            @Override
            public void callback() {
                // 尝试去park 自己线程
                LockSupport.park(blocker);
                System.out.println("wakeup now!");
            }

            @Override
            public String getName() {
                return "parkTest";
            }

        });

        t.start(); // 启动读取线程
        Thread.sleep(2000);
        LockSupport.unpark(t);
        t.interrupt();
    }

    public static Thread doTest(final TestCallBack call) {
        return new Thread() {

            @Override
            public void run() {
                File file = new File("/dev/urandom"); // 读取linux黑洞
                try {
                    FileInputStream in = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    while (in.read(bytes, 0, 1024) > 0) {
                        if (Thread.interrupted()) {
                            throw new InterruptedException("");
                        }
                        System.out.println(bytes[0]);
                        Thread.sleep(100);
                        long start = System.currentTimeMillis();
                        call.callback();
                        System.out.println(call.getName() + " callback finish cost : "
                                + (System.currentTimeMillis() - start));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
    }
}

interface TestCallBack {

    public void callback() throws Exception;

    public String getName();
}