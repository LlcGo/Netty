package com.lc.netty.nio.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.lc.netty.nio.utils.ByteBufferUtil.debugAll;

/**
 * @Author Lc
 * @Date 2023/5/8
 * @Description
 */
public class TestAio {
    public static void main(String[] args) throws IOException {
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(Paths.get("data.text"), StandardOpenOption.READ)){
            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
            //守护线程 主线程结束也结束
            fileChannel.read(byteBuffer, 0, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                //成功的回调
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    attachment.flip();
                    debugAll(attachment);
                }
                //失败的回调
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                   exc.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //阻塞
        System.in.read();
    }
}
