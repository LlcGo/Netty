package com.lc.netty.MulitThreads;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

import static com.lc.netty.utils.ByteBufferUtil.debugAll;

/**
 * @Author Lc
 * @Date 2023/5/6
 * @Description
 */
@Slf4j
public class Boss {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);

        ssc.bind(new InetSocketAddress(8080));
        Worker worker = new Worker("work-0");
//        Worker worker1 = new Worker("work-1");
//        System.out.println(worker == worker1);
//        System.out.println(worker.thread == worker1.thread);
        while (true){
            boss.select();
            final Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    final SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.debug("conected...{}",sc.getRemoteAddress());
                    //关联selector
                    log.debug("before register...{}",sc.getRemoteAddress());
                    sc.register(worker.selector,SelectionKey.OP_READ,null);
                    log.debug("after register...{}",sc.getRemoteAddress());
                }
            }
        }

    }

   static class Worker implements Runnable{
        private Thread thread;
        private Selector selector;
        private String name;
//        private boolean isCreate = false;

        public Worker(String name) {
            this.name = name;
        }
        //初始化线程和selector
        public void register() throws IOException {
//            if(!isCreate){
                thread = Work1Thread.creatsingleton(this,name);
//                thread = new Thread(this,name);
                thread.start();
                selector = Selector.open();
//                isCreate = true;
//            }


        }

        @Override
        public void run() {
            while (true){
                try {
                    selector.select();
                    final Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    iter.remove();
                    while (iter.hasNext()){
                        SelectionKey key = iter.next();
                        if(key.isReadable()){
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel sc = (SocketChannel)key.channel();
                            log.debug("read...{}",sc.getRemoteAddress());
                            sc.configureBlocking(false);
                            sc.read(buffer);
                            debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
