package future;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: https://ifeve.com/%e7%ba%bf%e7%a8%8b%e6%b1%a0%e4%bd%bf%e7%94%a8futuretask%e6%97%b6%e5%80%99%e9%9c%80%e8%a6%81%e6%b3%a8%e6%84%8f%e7%9a%84%e4%b8%80%e7%82%b9%e4%ba%8b/
 * @author:zysaaa
 * @date: 2021/1/31 20:04
 */
public class FutureTest {

    // (1)线程池单个线程，线程池队列元素个数为1
    private final static ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES,
        new ArrayBlockingQueue<Runnable>(1),new ThreadPoolExecutor.DiscardPolicy());

    public static void main(String[] args) throws Exception {

        //(2)添加任务one
        Future futureOne = executorService.submit(new Runnable() {
            @Override
            public void run() {

                System.out.println("start runable one");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //(3)添加任务two
        Future futureTwo = executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("start runable two");
            }
        });

        //(4)添加任务three
        Future futureThree=null;
        try {
            futureThree = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("start runable three");
                }
            });
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }


        System.out.println("task one " + futureOne.get());//(5)等待任务one执行完毕
        System.out.println("task two " + futureTwo.get());//(6)等待任务two执行完毕
        System.out.println("task three " + (futureThree==null?null:futureThree.get()));// (7)等待任务three执行完毕


        executorService.shutdown();//(8)关闭线程池，阻塞直到所有任务执行完毕
    }
}
