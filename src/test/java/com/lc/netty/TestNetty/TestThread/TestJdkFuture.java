package com.lc.netty.TestNetty.TestThread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Author Lc
 * @Date 2023/5/11
 * @Description
 */
@Slf4j
public class TestJdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行计算");
                Thread.sleep(10000);
                return 50;
            }
        });

        log.debug("等待结果");
        log.debug("等待的结果是 {}",future.get());
    }
}
