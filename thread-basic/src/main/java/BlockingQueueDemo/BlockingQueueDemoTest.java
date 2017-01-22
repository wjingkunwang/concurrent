package BlockingQueueDemo;

/**
 * Created by wjk on 17/1/22.
 */
public class BlockingQueueDemoTest {
    public static void main(String[] args) throws InterruptedException {
        final  BlockingQueueDemo queueDemo = new BlockingQueueDemo();

        queueDemo.offer(1);

        try {
            System.out.println(queueDemo.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
