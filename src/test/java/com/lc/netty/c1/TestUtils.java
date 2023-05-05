package com.lc.netty.c1;
import java.nio.ByteBuffer;

import static com.lc.netty.utils.ByteBufferUtil.debugAll;

/**
 * @Author Lc
 * @Date 2023/5/4
 * @Description
 */
public class TestUtils {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte)1);
//        debugAll(buffer);
        buffer.put((byte)2);
//        buffer.put((byte)2);
//        buffer.put((byte)2);
//        debugAll(buffer);
//        System.out.println("启动读模式");
        buffer.compact();
//        byte b = buffer.get();
        System.out.println("之后");
        debugAll(buffer);

    }
}
