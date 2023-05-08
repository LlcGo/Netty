package com.lc.netty.nio.c1;

import java.nio.ByteBuffer;

/**
 * @Author Lc
 * @Date 2023/5/4
 * @Description
 */
public class TestByteBufferALLocate {
    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(16).getClass());
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
    }
}
