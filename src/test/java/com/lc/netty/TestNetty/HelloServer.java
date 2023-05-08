package com.lc.netty.TestNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Author Lc
 * @Date 2023/5/8
 * @Description
 */
public class HelloServer {
    public static void main(String[] args) {
        //1.启动器初始化
        new ServerBootstrap()
                //2.添加一个有selector的东西并且循环
                .group(new NioEventLoopGroup())
                //3.添加channel
                .channel(NioServerSocketChannel.class)
                //4.添加work 处理器 管理下面其他的处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //5.将bytebuf转换为string输出
                        ch.pipeline().addLast(new StringDecoder());
                        //6.添加自定义handler功能
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //7.输出打印的信息
                                System.out.println(msg);
                            }
                        });
                    }
                })
                //绑定的端口
                .bind(8080);
    }
}
