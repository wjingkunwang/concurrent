package ThreadNotity;

/**
 * Created by wjk on 17/1/23.
 */
public class ThreadWait {
    public static void main(String[] args) {

        MyThread mt = new MyThread();
        mt.start();
        try {
            Thread.currentThread().sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("can you gethere?");
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mt.resumeThread();
    }


    static class MyThread extends Thread {

        public boolean stop = false;

        public void run() {
            while (true) {
                synchronized (this) {
                    System.out.println("running....");
                    while (stop)
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        }

        public void suspendThread() {
            this.stop = true;
        }

        public void resumeThread() {
            synchronized (this) {
                this.stop = false;
                notify();
            }
        }
    }
}
