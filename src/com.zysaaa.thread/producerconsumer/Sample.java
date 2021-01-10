package producerconsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @description: 注意while和if ==> 虚假唤醒
 * @author:zysaaa
 * @date: 2021/1/10 19:36
 */
public class Sample {

    private static List<String> list = new ArrayList<>(10);

    private static Object lock = new Object();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Producer(list).produce();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Producer(list).produce();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Consumer(list).consume();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Consumer(list).consume();
            }
        }).start();
    }

    static class Producer {

        private List<String> list;

        public Producer(List<String> list) {
            this.list = list;
        }

        public void produce() {
            while (true) {
                synchronized (list) {
                    System.err.println(list.size());
                    while (list.size() == 10) {
                        try {
                            System.out.println("满了，不进行生产");
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String s = UUID.randomUUID().toString();
                    list.add(s);
                    System.out.println("生产了一个：" + s);
                    list.notify();
                }
            }
        }
    }

    static class Consumer {
        private List<String> list;

        public Consumer(List<String> list) {
            this.list = list;
        }
        public void consume() {
            while (true) {
                synchronized (list) {
                    System.err.println(list.size());
                    while (list.size() == 0) {

                        try {
                            System.out.println("空了，不进行消费");
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String s = list.remove(0);
                    System.err.println("消费了：" + s);
                    list.notify();
                }
            }
        }
    }

}
