package com.lc.netty.nio.ServerWrite;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @Author Lc
 * @Date 2023/5/6
 * @Description
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8080));

        while (true) {
            selector.select();
            //获取所有的key
            Iterator<SelectionKey> iterator =selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);
                    SelectionKey sckey = sc.register(selector, 0, null);
                    sckey.interestOps(SelectionKey.OP_READ);
                    //1.向客户端发送数据
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < 300000000; i++) {
                        builder.append("a");
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(builder.toString());
                    //2.写出多少字节
                    int write = sc.write(buffer);
                    System.out.println(write);

                    //还有容量可以写
                    if (buffer.hasRemaining()) {
                        //关注可写事件
                        sckey.interestOps(sckey.interestOps() + SelectionKey.OP_WRITE);
                        //将数据放入下次继续读取
                        sckey.attach(buffer);
                    }
                }else if(key.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    SocketChannel sc = (SocketChannel)key.channel();
                    int write = sc.write(buffer);
                    System.out.println(write);
                    if(!buffer.hasRemaining()){
                        key.attach(null);
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);//不需要再监控读取
                }
                }
            }

        }
    }
}
