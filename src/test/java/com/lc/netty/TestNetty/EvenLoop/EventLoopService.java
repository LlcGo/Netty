package com.lc.netty.TestNetty.EvenLoop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @Author Lc
 * @Date 2023/5/9
 * @Description
 */
@Slf4j
public class EventLoopService {
    public static void main(String[] args) {
        //细分
        DefaultEventLoopGroup group = new DefaultEventLoopGroup();
        new ServerBootstrap()
                //1.第一个值boss 只负责 accept事件  第二个是work 负责读写
                .group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel hc) throws Exception {
                        hc.pipeline().addLast("handler1",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                              ByteBuf byteBuf = (ByteBuf)msg;
                              log.debug("收到的数据是:{}",byteBuf.toString(Charset.defaultCharset()));
                              //传消息给下一个eventLoop
                              ctx.fireChannelRead(msg);
                            }

                        }).addLast(group,"handler2",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf)msg;
                                log.debug("收到的数据是:{}",byteBuf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                }).bind(8080);
    }
}
