package base;

/**
 * @description:
 * @author:zysaaa
 * @date: 2021/1/31 17:42
 */
public class VolatileTest {



    private static int num =0 ;
    private static boolean ready = false ;


    public static void main(String[] args) throws InterruptedException {
        WriteThread writeThread = new WriteThread();


        ReadThread readThread = new ReadThread();

        readThread.start();
        Thread.sleep(1000);
        writeThread.start();

        Thread.sleep(10);
        readThread.interrupt();


    }


    private static class WriteThread extends Thread {
        @Override
        public void run() {
            num = 2;
            ready = true;
        }
    }


    private static class ReadThread extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (ready) {
                    System.out.println(num + num);
                }
            }
        }
    }


}
