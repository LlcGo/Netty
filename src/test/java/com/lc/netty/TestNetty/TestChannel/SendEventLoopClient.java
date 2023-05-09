package com.lc.netty.TestNetty.TestChannel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @Author Lc
 * @Date 2023/5/9
 * @Description
 */
@Slf4j
public class SendEventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group  = new NioEventLoopGroup();
        ChannelFuture future = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler((new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })).connect(new InetSocketAddress("localhost", 8080));
        Channel channel = future.sync().channel();
        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String s = scanner.nextLine();
                if ("q".equals(s)) {
                    channel.close();
//                    log.debug("处理关闭之后的事情");
                    break;
                }
                channel.writeAndFlush(s);
            }
        },"input").start();
        //同步关闭
//        ChannelFuture closeFuture = channel.closeFuture();
//        closeFuture.sync();
//        log.debug("处理关闭之后的事情");
        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener((ChannelFutureListener) channelFuture -> {
            log.debug("处理操作");
            group.shutdownGracefully();
        });
    }
}
