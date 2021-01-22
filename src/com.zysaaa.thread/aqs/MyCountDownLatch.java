package aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @description: MyCountDownLatch,一次性countdown
 * @author:zysaaa
 * @date: 2021/1/22 21:56
 */
public class MyCountDownLatch {

    private class Sync extends AbstractQueuedSynchronizer {


        @Override
        protected int tryAcquireShared(int arg) {
            if (getState() == 0) {
                return -1;
            } else {
                return 1;
            }
        }


        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1);
            return true;
        }
    }


    private Sync sync = new Sync();

    public void await() {
        sync.acquireShared(1);
    }

    public void countdown() {
        sync.releaseShared(1);
    }

    public static void main(String[] args) throws InterruptedException {

        MyCountDownLatch myCountDownLatch = new MyCountDownLatch();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                myCountDownLatch.countdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        new Thread(() -> {
            try {
                Thread.sleep(3000);
                myCountDownLatch.countdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(100);
        myCountDownLatch.await();
        System.out.println("end");

    }

}
