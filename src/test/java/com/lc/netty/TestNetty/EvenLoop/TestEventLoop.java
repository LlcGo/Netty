package com.lc.netty.TestNetty.EvenLoop;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author Lc
 * @Date 2023/5/9
 * @Description
 */
@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        //1.使用俩个线程
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);

        //2.默认轮询机制
        System.out.println(eventLoopGroup.next());
        System.out.println(eventLoopGroup.next());
        System.out.println(eventLoopGroup.next());

        //其中一个线程执行一个普通任务
        eventLoopGroup.next().submit(() -> {
            log.debug("ok");
        });

        //其中一个线程执行延迟任务
        eventLoopGroup.next().scheduleAtFixedRate(()->{
            log.debug("测试");
            //0第一次执行任务的时间 1每隔多少秒执行 时间
        },0,1, TimeUnit.SECONDS);

        log.debug("main");
    }
}
