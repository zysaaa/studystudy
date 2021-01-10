import java.util.concurrent.*;

/**
 * @description: future,callable,futuretask.....
 *
 * Future,Runnable是顶层接口，RunnableFuture继承实现这两个接口，FutureTask实现RunnableFuture.
 * 而FutureTask内部其实是一个callable。
 *
 * @author:zysaaa
 * @date: 2021/1/10 15:21
 */
public class FutureCall {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test00();
    }

    private static void test00() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Integer result = 1;
        Future<Integer> submit = executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("...");
            }
        }, result);
        // 其实毫无意义  -- 最终用到了RunnableAdapter，这个类其实就是将runnable-run完，把result返回回来
        //  /**
        //     * @throws RejectedExecutionException {@inheritDoc}
        //     * @throws NullPointerException       {@inheritDoc}
        //     */
        //    public <T> Future<T> submit(Runnable task, T result) {
        //        if (task == null) throw new NullPointerException();
        //        RunnableFuture<T> ftask = newTaskFor(task, result);
        //        execute(ftask);
        //        return ftask;
        //    }
        System.out.println(submit.get());
    }


    private static void test01() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(3000);
                return 1;
            }
        });
        Future<?> submit = executorService.submit(futureTask);
        // 输出null
        // 因为这不是正确用法，当你自己构造一个FutureTask传入executorService
        // 是被当做一个runnable的，并不会返回任何东西.
        // RunnableFuture<Void> ftask = newTaskFor(task, null);
        System.out.println(submit.get());
        // 这才是正确用法
        System.out.println(futureTask.get());
    }

    private static void test02() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> submit = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    System.out.println("开始执行。");
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // mayInterruptIfRunning会去中断 Thread.sleep(3000); 这个操作。
        System.out.println(submit.cancel(false));
        System.out.println(submit.cancel(true));
    }

}
