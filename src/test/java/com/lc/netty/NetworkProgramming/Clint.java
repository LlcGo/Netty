package com.lc.netty.NetworkProgramming;

import com.google.gson.internal.bind.util.ISO8601Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

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
        System.out.println("wait....");
    }
}
