package base;

/**
 * @description:
 * @author:zysaaa
 * @date: 2021/1/22 22:16
 */
public class InterruptTest {


    public static void main(String[] args) throws InterruptedException {
        //test01();
        test02();
    }


    private static void test01() {
        Thread.currentThread().interrupt();
        System.out.println(Thread.interrupted());
        System.out.println(Thread.interrupted());
        Thread.currentThread().interrupt();
        System.out.println(Thread.currentThread().isInterrupted());
        System.out.println(Thread.interrupted());
        System.out.println(Thread.interrupted());
        // true,false,true,true,false
    }


    private static void test02() throws InterruptedException {

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                //then its interrupt status will be cleared and it
                //     * will receive an {@link InterruptedException}.
                System.out.println(Thread.currentThread().isInterrupted());  // PRINT FALSE
                Thread.currentThread().interrupt();
            }
            Thread.currentThread().interrupt();
            while (true) {}  // 注意，必须有这段，否则线程not alive，thread.isInterrupted()就会返回false
        });

        thread.start();
        thread.interrupt();
        Thread.sleep(200);
        System.out.println(thread.isInterrupted());
    }
}
