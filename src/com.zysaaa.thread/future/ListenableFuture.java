package future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @description: 简易实现，可以参考 Spring：ListenableFuture
 * @author:zysaaa
 * @date: 2021/1/31 20:27
 */
public class ListenableFuture<V> extends FutureTask<V> {


    private ResponseCallBack<V> responseCallBack;

    public ListenableFuture(Callable<V> callable, ResponseCallBack<V> responseCallBack) {
        super(callable);
        this.responseCallBack = responseCallBack;
    }

    @Override
    protected void done() {

        try {
            V v = get();
            responseCallBack.onSuccessResponse(v);
        } catch (Exception e) {
            responseCallBack.onFailResponse(e);
        }

    }

    interface ResponseCallBack<V> {
        void onSuccessResponse(V result);

        void onFailResponse(Throwable e);
    }

    public static void main(String[] args) throws InterruptedException {
        ListenableFuture task = new ListenableFuture(new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(5000); // 模拟耗时操作
                System.out.println(1 / 0);
                return "success";
            }
        }, new ResponseCallBack() {
            @Override
            public void onSuccessResponse(Object result) {
                System.out.println("调用成功，返回结果是:" + result);
            }

            @Override
            public void onFailResponse(Throwable e) {
                e.printStackTrace();
                System.out.println("调用失败了...");
            }
        });

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(task);

        Thread.sleep(10000);
    }

}
