package com.lc.netty.documentNetty.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author Lc
 * @Date 2023/5/11
 * @Description 丢弃任何进入的数据
 */
public class DiscardService {
    private int port;

    public DiscardService(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,work).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                             ch.pipeline().addLast(new DiscardServiceHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            //绑定端口
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
             work.shutdownGracefully();
             boss.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int port;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }else {
            port = 8080;
        }
        new DiscardService(port).run();
    }
}
