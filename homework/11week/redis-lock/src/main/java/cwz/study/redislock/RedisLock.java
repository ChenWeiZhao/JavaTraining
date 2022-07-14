/*
 * <p>文件名称: RedisLock</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/7/14 23:14 </p>
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
package cwz.study.redislock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * Redis 分布式锁
 */
public class RedisLock {
    private enum EnumSingleton {
        /**
         * 懒汉枚举单例
         */
        INSTANCE;
        private RedisLock instance;

        EnumSingleton() {
            instance = new RedisLock();
        }

        public RedisLock getInstance() {
            return instance;
        }
    }

    public static RedisLock getInstance() {
        return EnumSingleton.INSTANCE.getInstance();
    }

    private JedisPool jedisPool = new JedisPool();

    /**
     * 加锁
     *
     * @param lockValue 锁
     * @param seconds   过期次数
     * @return 获取锁
     */
    public boolean lock(String lockValue, int seconds) {
        try (Jedis jedisResource = jedisPool.getResource()) {
            return "OK".equals(jedisResource.set(lockValue, lockValue, "NX", "EX", seconds));
        }
    }

    /**
     * 释放锁
     * Redis Eval 命令使用 Lua 解释器执行脚本。
     *
     * @param lock 锁值
     * @return 释放锁
     */
    public boolean release(String lock) {
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " + "return redis.call('del',KEYS[1]) else return 0 end";
        try (Jedis jedisResource = jedisPool.getResource()) {
            return jedisResource.eval(luaScript, Collections.singletonList(lock), Collections.singletonList(lock)).equals(1L);
        }
    }
}
