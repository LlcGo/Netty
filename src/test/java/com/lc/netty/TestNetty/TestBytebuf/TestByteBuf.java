package com.lc.netty.TestNetty.TestBytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;


/**
 * @Author Lc
 * @Date 2023/5/11
 * @Description
 */
public class TestByteBuf {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        Utils.log(buffer);
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            s.append("a");
        }
        buffer.writeBytes(s.toString().getBytes());
        Utils.log(buffer);
    }

}
