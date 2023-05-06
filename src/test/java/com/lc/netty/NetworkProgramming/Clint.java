package com.lc.netty.NetworkProgramming;

import com.google.gson.internal.bind.util.ISO8601Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @Author Lc
 * @Date 2023/5/5
 * @Description
 */
public class Clint {
    public static void main(String[] args) throws IOException {
        //创建客户端网络
        SocketChannel clint = SocketChannel.open();
        clint.connect(new InetSocketAddress("localhost",8080));
        SocketAddress localAddress = clint.getLocalAddress();
        clint.write(Charset.defaultCharset().encode("asdasdsaddsadsdsdasdsafagd\n6666666666666666666666666666666666666666666\n"));
        System.in.read();//阻塞方法 等待控制台输入
    }
}
