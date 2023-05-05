package com.lc.netty.shiyong;

import java.nio.ByteBuffer;

import static com.lc.netty.utils.ByteBufferUtil.debugAll;

/**
 * @Author Lc
 * @Date 2023/5/4
 * @Description
 */
public class TestByteBufferExam {
    public static void main(String[] args) {
         ByteBuffer source = ByteBuffer.allocate(32);
         source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
         split(source);
         source.put("w are you?\n".getBytes());
         split(source);
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
