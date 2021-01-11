package aqs;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: CountDownLatchTest
 * @author:zysaaa
 * @date: 2021/1/11 21:41
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        test02();
    }

    private static void test01() {
        // 注意，booleanLatch的对state的操作。
        BooleanLatch booleanLatch = new BooleanLatch();
        try {
            booleanLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("...");
    }

    private static void test02() {
        ReentrantLock reentrantLock = new ReentrantLock();

        //condition 注意，signalAll之后所有wait卡在condition的线程重新回到AQS队列重新竞争锁。
        Condition condition = reentrantLock.newCondition();

        new Thread(() -> {
            reentrantLock.lock();
            System.out.println("现场1拿到锁");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("现场1被唤醒");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("现场1释放锁");
            reentrantLock.unlock();
        }).start();

        new Thread(() -> {
            reentrantLock.lock();
            System.out.println("现场2拿到锁");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("现场2被唤醒");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("现场2释放锁");
            reentrantLock.unlock();
        }).start();

        new Thread(() -> {
            reentrantLock.lock();
            System.out.println("现场3拿到锁");
            // 以下二者是不一样的行为，上者最终只会有一个线程出来，剩下的一直带着condition条件队列里。
            condition.signal();
            condition.signalAll();
            System.out.println("现场3 notify");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reentrantLock.unlock();
        }).start();
    }
}
