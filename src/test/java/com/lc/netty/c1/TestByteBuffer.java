package com.lc.netty.c1;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author Lc
 * @Date 2023/5/4
 * @Description
 */
public class TestByteBuffer {
    public static void main(String[] args) {
        try (FileChannel fileChannel = new FileInputStream("data.text").getChannel()) {
            //获取10字节的缓存空间
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                int len = fileChannel.read(buffer);
                if(len == -1){
                    break;
                }
                buffer.flip();//切换读模式
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    System.out.println((char) b);
                }
                buffer.clear();//切换写模式
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
