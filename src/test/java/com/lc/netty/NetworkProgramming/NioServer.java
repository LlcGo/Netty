package com.lc.netty.NetworkProgramming;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import static com.lc.netty.utils.ByteBufferUtil.debugAll;

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
                final SelectionKey readkey = sc.register(selector, 0, null);
                readkey.interestOps(SelectionKey.OP_READ);
                log.debug("连接成功的是：{}"+ sc);
            } else if(ckey.isReadable()){//如果是读
                SocketChannel readChannel = (SocketChannel)ckey.channel();  //拿到事件触发的channel
                ByteBuffer buffer = ByteBuffer.allocate(16);
                readChannel.read(buffer);
                buffer.flip();
                debugAll(buffer);
            }


//            ckey.cancel();
        }
    }
}
