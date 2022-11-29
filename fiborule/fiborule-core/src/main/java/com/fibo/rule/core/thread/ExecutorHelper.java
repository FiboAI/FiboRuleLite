package com.fibo.rule.core.thread;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.fibo.rule.core.property.FiboRuleConfig;
import com.fibo.rule.core.property.FiboRuleConfigGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;


/**
 *<p>线程池工具类</p>
 *
 *@author JPX
 *@since 2022/11/28 17:24
 */
public class ExecutorHelper {

    private final Logger LOG = LoggerFactory.getLogger(ExecutorHelper.class);

    /**
     * 此处使用Map缓存线程池信息
     * key - 线程池构建者的Class全类名
     * value - 线程池对象
     */
    private final Map<String, ExecutorService> executorServiceMap;

    private ExecutorHelper() {
        executorServiceMap = MapUtil.newConcurrentHashMap();
    }

    /**
     * 使用静态内部类实现单例模式
     */
    private static class Holder {
        static final ExecutorHelper INSTANCE = new ExecutorHelper();
    }

    public static ExecutorHelper loadInstance() {
        return Holder.INSTANCE;
    }

    /**
     *
     * <p>
     *
     * @param pool 需要关闭的线程组.
     */
    public void shutdownAwaitTermination(ExecutorService pool) {
        shutdownAwaitTermination(pool, 60L);
    }

    /**
     * <p>
     * 关闭ExecutorService的线程管理者
     * <p>
     *
     * @param pool    需要关闭的管理者
     * @param timeout 等待时间
     */
    public void shutdownAwaitTermination(ExecutorService pool,
                                         long timeout) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
                    LOG.error("Pool did not terminate.");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public ExecutorService buildAllExecutor() {
        String clazz = "com.fibo.rule.core.thread.FiboAllExecutorBuilder";
        ExecutorService executorServiceFromCache = executorServiceMap.get(clazz);
        if(ObjectUtil.isNotNull(executorServiceFromCache)) {
            return executorServiceFromCache;
        } else {
            FiboRuleConfig fiboRuleConfig = FiboRuleConfigGetter.get();
            if (ObjectUtil.isNull(fiboRuleConfig)){
                fiboRuleConfig = new FiboRuleConfig();
            }
            ExecutorService executorService = buildDefaultExecutor(
                    fiboRuleConfig.getAllMaxWorkers(),
                    fiboRuleConfig.getAllMaxWorkers(),
                    fiboRuleConfig.getAllQueueLimit(),
                    "fibo-all-thead-");
            executorServiceMap.put(clazz, executorService);
            return executorService;
        }
    }

    private ExecutorService buildDefaultExecutor(int corePoolSize, int maximumPoolSize, int queueCapacity, String threadName) {
        return TtlExecutors.getTtlExecutorService(new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                new ThreadFactory() {
                    private final AtomicLong number = new AtomicLong();

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread newThread = Executors.defaultThreadFactory().newThread(r);
                        newThread.setName(threadName + number.getAndIncrement());
                        newThread.setDaemon(false);
                        return newThread;
                    }
                },
                new ThreadPoolExecutor.AbortPolicy()));
    }

    public void clearExecutorServiceMap(){
        if (MapUtil.isNotEmpty(executorServiceMap)){
            executorServiceMap.clear();
        }
    }
}
