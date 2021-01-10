package aqs;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description:
 * @author:zysaaa
 * @date: 2021/1/10 17:23
 */
public class AQSTest {


    public static void main(String[] args) {
        test01();
    }

    private static void test01() {
        // 都会抛异常
        Lock lock = new ReentrantLock();
        lock.unlock();
        Mutex mutex = new Mutex();
        mutex.unlock();
    }


    private static void test00() {
        // 基于AQS的共享锁
        Semaphore semaphore = new Semaphore(3);

        new Thread(() -> {
            try {
                semaphore.acquire();
                System.err.println("...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }).start();

        new Thread(() -> {
            try {
                semaphore.acquire();
                System.err.println("......");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }).start();

        new Thread(() -> {
            try {
                semaphore.acquire();
                System.err.println("............");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }).start();

        new Thread(() -> {
            try {
                semaphore.acquire();
                System.err.println("222");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }).start();
    }


}
