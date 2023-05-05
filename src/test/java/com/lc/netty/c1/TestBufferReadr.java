package com.lc.netty.c1;

import java.nio.ByteBuffer;

import static com.lc.netty.utils.ByteBufferUtil.debugAll;

/**
 * @Author Lc
 * @Date 2023/5/4
 * @Description
 */
public class TestBufferReadr {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(10);
        buffer.put(new byte[]{'a','b','c','d'});
        buffer.flip();
//        buffer.get(new byte[4]);
//        debugAll(buffer);
//        buffer.rewind();
//        debugAll(buffer)
        buffer.get();
        buffer.get();
        debugAll(buffer);
        buffer.mark();
        buffer.get();
        debugAll(buffer);
        buffer.reset();
        debugAll(buffer);
    }
}
