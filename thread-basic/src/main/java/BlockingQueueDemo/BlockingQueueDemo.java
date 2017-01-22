package BlockingQueueDemo;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 简单实现一个阻塞队列
 */
public class BlockingQueueDemo {
    private Object notEmpty = new Object();

    private Queue<Object> linkedList = new LinkedList<Object>();

    public Object take() throws InterruptedException {
        synchronized (notEmpty) {
            while (linkedList.size() == 0) {
                notEmpty.wait();
            }
            return linkedList.poll();
        }
    }

    public void offer(Object object) {
        synchronized (notEmpty) {
            while (linkedList.size() == 0) {
                notEmpty.notifyAll();
            }
            linkedList.add(object);
        }
    }
}
