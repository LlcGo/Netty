package com.lc.netty.nio.MulitThreads;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lc.netty.nio.utils.ByteBufferUtil.debugAll;

/**
 * @Author Lc
 * @Date 2023/5/6
 * @Description
 */
@Slf4j
public class Boss {
//    public static void main(String[] args) throws IOException {
//        Thread.currentThread().setName("boss");
//        ServerSocketChannel ssc = ServerSocketChannel.open();
//        ssc.configureBlocking(false);
//
//        Selector boss = Selector.open();
//        SelectionKey bossKey = ssc.register(boss, 0, null);
//        bossKey.interestOps(SelectionKey.OP_ACCEPT);
//
//        ssc.bind(new InetSocketAddress(8080));
//        Worker[] workers = new Worker[2];
//        for (int i = 0; i < workers.length; i++) {
//            Worker worker = new Worker("work-" + i);
//        }
//
////        Worker worker1 = new Worker("work-1");
////        System.out.println(worker == worker1);
////        System.out.println(worker.thread == worker1.thread);
//          AtomicInteger index = new AtomicInteger();
//        while (true){
//            boss.select();
//             Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
//            while (iterator.hasNext()) {
//                SelectionKey key = iterator.next();
//                iterator.remove();
//                if(key.isAcceptable()){
//                    SocketChannel sc = ssc.accept();
//                    sc.configureBlocking(false);
//                    log.debug("conected...{}",sc.getRemoteAddress());
//                    //关联selector
//                    log.debug("before register...{}",sc.getRemoteAddress());
//                    //round robin 轮询算法
//                    workers[index.getAndIncrement() % workers.length].register(sc);//初始化selector 启动启动work0
//
//                    log.debug("after register...{}",sc.getRemoteAddress());
//                }
//            }
//        }
//
//    }
//
//   static class Worker implements Runnable{
//        private Thread thread;
//        private Selector selector;
//        private String name;
//        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
//        private boolean isCreate = false;
//
//        public Worker(String name) {
//            this.name = name;
//        }
//        //初始化线程和selector
//        public void register(SocketChannel sc) throws IOException {
//            if(!isCreate){
//                selector = Selector.open();
////                thread = Work1Thread.creatsingleton(this,name);
//                thread = new Thread(this,name);
//                thread.start();
//                isCreate = true;
//            }
//            queue.add(()->{
//                try {
//                    sc.register(selector,SelectionKey.OP_READ,null);
//                } catch (ClosedChannelException e) {
//                    e.printStackTrace();
//                }
//            });
//            selector.wakeup();//唤醒
//        }
//
//        @Override
//        public void run() {
//            while (true){
//                try {
//                    selector.select();
//                    Runnable poll = queue.poll();
//                    if(poll != null){
//                        poll.run();
//                    }
//                     Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
//                    iter.remove();
//                    while (iter.hasNext()){
//                        SelectionKey key = iter.next();
//                        if(key.isReadable()){
//                            ByteBuffer buffer = ByteBuffer.allocate(16);
//                            SocketChannel sc = (SocketChannel)key.channel();
//                            log.debug("read...{}",sc.getRemoteAddress());
//                            sc.configureBlocking(false);
//                            sc.read(buffer);
//                            debugAll(buffer);
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }
public static void main(String[] args) throws IOException {
    Thread.currentThread().setName("boss");
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.configureBlocking(false);
    Selector boss = Selector.open();
    SelectionKey bossKey = ssc.register(boss, 0, null);
    bossKey.interestOps(SelectionKey.OP_ACCEPT);
    ssc.bind(new InetSocketAddress(8080));
    // 1. 创建固定数量的 worker 并初始化
    Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
    for (int i = 0; i < workers.length; i++) {
        workers[i] = new Worker("worker-" + i);
    }
    AtomicInteger index = new AtomicInteger();
    while(true) {
        boss.select();
        Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
        while (iter.hasNext()) {
            SelectionKey key = iter.next();
            iter.remove();
            if (key.isAcceptable()) {
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                log.debug("connected...{}", sc.getRemoteAddress());
                // 2. 关联 selector
                log.debug("before register...{}", sc.getRemoteAddress());
                // round robin 轮询
                workers[index.getAndIncrement() % workers.length].register(sc); // boss 调用 初始化 selector , 启动 worker-0
                log.debug("after register...{}", sc.getRemoteAddress());
            }
        }
    }
}
    static class Worker implements Runnable{
        private Thread thread;
        private Selector selector;
        private String name;
        private volatile boolean start = false; // 还未初始化
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
        public Worker(String name) {
            this.name = name;
        }

        // 初始化线程，和 selector
        public void register(SocketChannel sc) throws IOException {
            if(!start) {
                selector = Selector.open();
                thread = new Thread(this, name);
                thread.start();
                start = true;
            }
            selector.wakeup(); // 唤醒 select 方法 boss
            sc.register(selector, SelectionKey.OP_READ, null); // boss
        }

        @Override
        public void run() {
            while(true) {
                try {
                    selector.select(); // worker-0  阻塞
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            log.debug("read...{}", channel.getRemoteAddress());
                            channel.read(buffer);
                            buffer.flip();
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

