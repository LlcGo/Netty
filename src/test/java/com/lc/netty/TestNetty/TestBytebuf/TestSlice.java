package com.lc.netty.TestNetty.TestBytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @Author Lc
 * @Date 2023/5/16
 * @Description
 */
public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a','b','c','d','e','f','g','h'});
        Utils.log(buf);

        //检测是否拷贝了 //从0下标开始获取4个
        ByteBuf buf1 = buf.slice(0, 4);
        //从下标4开始获取4个
        ByteBuf buf2 = buf.slice(4, 4);
        Utils.log(buf1);
        Utils.log(buf2);

        buf1.setByte(0,'e');
        Utils.log(buf);
    }
}
