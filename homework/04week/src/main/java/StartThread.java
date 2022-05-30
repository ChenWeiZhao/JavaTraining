/*
 * <p>文件名称: StartThread</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/5/30 20:50 </p>
 * <p>完成日期: </p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 *
 * @version 1.0
 * @author chenwz
 */

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 2.（必做）思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？
 */
public class StartThread {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池， 异步执行 下面方法
        //0.不启用线程
        //int result = normalStart();
        //1.callable
        //int result = callableStart();
        //2.线程池
        //int result = threadPoolStart();
        //3.futureTask
        //int result = futureTaskStart();
        //4.futureTaskThreadPool
        //int result = futureTaskThreadPoolStart();
        //5.countdownLatch
        //int result = futureTaskThreadPoolStart();
        //6.countdownLatchThreadPool
        //int result = countdownLatchThreadPool();
        //7.CyclicBarrier
        //int result = cyclicBarrier();
        //8.joinStart
        int result = joinStart();

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        // 然后退出main线程
    }

    /**
     * 0.没启动线程
     */
    private static int normalStart() {
        return BaseFunction.sum();
    }

    /**
     * 1.callable
     */
    private static int callableStart() throws Exception {
        Callable<Integer> sum = BaseFunction::sum;
        return sum.call();
    }

    /**
     * 2.线程池
     */
    public static int threadPoolStart() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> submitFuture = executorService.submit(BaseFunction::sum);
        Integer result = submitFuture.get();
        executorService.shutdown();
        return result;
    }

    /**
     * 3.futureTask
     */
    public static int futureTaskStart() throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(BaseFunction::sum);
        new Thread(futureTask).start();
        return futureTask.get();
    }

    /**
     * 4.futureTaskThreadPool
     */
    public static int futureTaskThreadPoolStart() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<Integer> futureTask = new FutureTask<>(BaseFunction::sum);
        executorService.execute(futureTask);
        int result = futureTask.get();
        executorService.shutdown();
        return result;
    }

    private static int sum;

    /**
     * 5.CountdownLatch
     */
    public static int countdownLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Runnable runnable = () -> {
            sum = BaseFunction.sum();
            countDownLatch.countDown();
        };
        new Thread(runnable).start();
        countDownLatch.await();
        return sum;
    }

    /**
     * 6.CountdownLatch
     */
    public static int countdownLatchThreadPool() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            sum = BaseFunction.sum();
            countDownLatch.countDown();
        });
        countDownLatch.await();
        executorService.shutdown();
        return sum;
    }

    /**
     * 7.cyclicBarrier
     */
    private static int cyclicBarrier() throws BrokenBarrierException, InterruptedException {
        // 因为需要主线程等待结果出来后再继续，所以这里用2，让主线程也要await
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            sum = BaseFunction.sum();
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        cyclicBarrier.await();
        executorService.shutdown();
        return sum;
    }

    /**
     * 8. join
     */
    private static int joinStart() throws InterruptedException {
        Runnable runnable = () -> sum = BaseFunction.sum();
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return sum;
    }


}
