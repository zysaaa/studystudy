package base;

/**
 * @description: https://blog.csdn.net/about4years/article/details/80235261
 * @author:zysaaa
 * @date: 2021/1/20 22:23
 */
public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        test01();
    }


    private static void test01() throws InterruptedException {

        Thread thread = new Thread(() -> {
            System.out.println("I am thread-1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        thread.start();
        thread.join();
        System.out.println("end");

    }
}
