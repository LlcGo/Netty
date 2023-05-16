package com.lc.netty.TestNetty.TestThread;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @Author Lc
 * @Date 2023/5/11
 * @Description
 */
@Slf4j
public class TestNettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop eventLoop = new NioEventLoopGroup().next();

        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread( () -> {
            log.debug("开始计算");
            try {
                Thread.sleep(1000);
                promise.setSuccess(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
        }).start();

        log.debug("等待结果");
        log.debug("结果是,{}",promise.get());
    }
}
