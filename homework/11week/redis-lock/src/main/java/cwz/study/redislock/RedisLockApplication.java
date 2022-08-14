package cwz.study.redislock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisLockApplication {
    /**
     * 锁
     */
    private final static String LOCK = "redis_lock";
    /**
     * 过期次数
     */
    private final static int EXPIRE = 3;
    /**
     * 库存
     */
    private static int stock = 10;

    public static void lockTest() {
        System.out.println("lock test :: start sleep 10");

        if (!RedisLock.getInstance().lock(LOCK, EXPIRE)) {
            System.out.println("获取锁失败");
            return;
        }
        try {
            Thread.sleep(10000);
            stock = stock - 1;
            System.out.printf("库存减一 amout: %d\n", stock);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RedisLock.getInstance().release(LOCK);
        System.out.println("lock test :: end");

    }


    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(RedisLockApplication.class, args);

        Thread thread1 = new Thread(RedisLockApplication::lockTest);
        Thread thread2 = new Thread(RedisLockApplication::lockTest);

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        Thread thread3 = new Thread((RedisLockApplication::lockTest));
        thread3.start();
        thread3.join();
    }

}