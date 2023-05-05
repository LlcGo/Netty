package com.lc.netty.c1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.lc.netty.c1.ByteBufferUtil.debugAll;

/**
 * @Author Lc
 * @Date 2023/5/4
 * @Description
 */
public class TestScatterRead {
    public static void main(String[] args) {
        try(FileChannel channel = new RandomAccessFile("data2.text","r").getChannel()) {
            ByteBuffer buffer1 = ByteBuffer.allocateDirect(3);
            ByteBuffer buffer2 = ByteBuffer.allocateDirect(3);
            ByteBuffer buffer3 = ByteBuffer.allocateDirect(5);
            channel.read(new ByteBuffer[]{buffer1,buffer2,buffer3});
            buffer1.flip();
            buffer2.flip();
            buffer3.flip();
            debugAll(buffer1);
            debugAll(buffer2);
            debugAll(buffer3);
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
