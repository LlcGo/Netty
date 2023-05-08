package com.lc.netty.nio.NetworkProgramming;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

import static com.lc.netty.nio.utils.ByteBufferUtil.debugAll;

/**
 * @Author Lc
 * @Date 2023/5/5
 * @Description
 */
@Slf4j
public class NioServer {
    public static void main(String[] args) throws IOException {
        //1.获取selector 管理多个channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//设置为非阻塞
        //2.给服务器端注册selector
        SelectionKey sscKey = ssc.register(selector, 0, null);
        //3给注册key设置关注accept
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        //4绑定接口
        ssc.bind(new InetSocketAddress(8080));
        //给每一个发送过来的channel注册一个key
        log.debug("sscKey:{}",sscKey);
        //5.设置select 当没有连接过来的时候阻塞 /事件未处理就不会阻塞 如果不处理那就要设置取消获取的key.cancel();
        selector.select();
        //6.获取selector的keys
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        //7.要对数据进行删除要用迭代器
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey ckey = iterator.next();
            //8.要删除key不然会报错，因为他不会帮我们删除
            iterator.remove();
            log.debug("获取的key：{}"+ ckey);
            if (ckey.isAcceptable()) { //如果是连接
                ServerSocketChannel channel = (ServerSocketChannel)ckey.channel();
                //这里的就是上面设置的key
                SocketChannel sc = channel.accept();
                //设置为非阻塞
                sc.configureBlocking(false);
                ByteBuffer buffer = ByteBuffer.allocate(16);
                SelectionKey readkey = sc.register(selector, 0, buffer);
                readkey.interestOps(SelectionKey.OP_READ);
                log.debug("连接成功的是：{}"+ sc);
            } else if(ckey.isReadable()){//如果是读
                try {
                    SocketChannel readChannel = (SocketChannel)ckey.channel();  //拿到事件触发的channel
                    ByteBuffer buffer = (ByteBuffer)ckey.attachment();
                    int read = readChannel.read(buffer);
                    if(read == -1){
                        ckey.cancel(); //如果客户端正常退出读取的时候返回-1
                    }else {
                        split(buffer);
                        //如果最后读满了 当前指针位置等与最后限制的位置需要扩容
                        if (buffer.position() == buffer.limit()){
                              //扩容为原来的两倍
                            ByteBuffer newBuffer  = ByteBuffer.allocate(buffer.capacity() * 2);
                            buffer.flip();
                            newBuffer.put(buffer);
                            //扩容后再放入附属品
                            ckey.attach(newBuffer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    ckey.cancel(); //客户端强制注销 取消key
                }
            }


//            ckey.cancel();
        }
    }

    private static void split(ByteBuffer buffer){
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
            if(buffer.get(i) == '\n'){
                int length = i + 1 - buffer.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                for(int j = 0; j < length; j++){
                    target.put(buffer.get());
                }
                debugAll(target);
            }
        }
        buffer.compact();
    }
}
