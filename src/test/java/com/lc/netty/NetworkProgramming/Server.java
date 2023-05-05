package com.lc.netty.NetworkProgramming;



import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import static com.lc.netty.utils.ByteBufferUtil.debugAll;

/**
 * @Author Lc
 * @Date 2023/5/5
 * @Description
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        //nio阻塞线程
        //缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //监听端口
        ssc.bind(new InetSocketAddress(8080));
        //可能有多个channel
        ArrayList<SocketChannel> socketChannelArrayList = new ArrayList<>();
        //一只监听
        while (true){
            log.debug("connecting");
            //开启连接 返回的是收到的数据
            SocketChannel channel = ssc.accept(); //阻塞方法
            log.debug("connected。。。");
            //可能会很多所以都加入到集合中
            socketChannelArrayList.add(channel);
            socketChannelArrayList.forEach(socketChannel -> {
                try {
                    //读入缓冲区
                    log.debug("before read....");
                    socketChannel.read(buffer);//阻塞
                    //开始读模式
                    buffer.flip();
                    debugAll(buffer);
                    buffer.clear();
                    log.debug("after read...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
