package com.lc.Netty.protocol;

import com.lc.Netty.Message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @Author Lc
 * @Date 2023/5/18
 * @Description
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        //1. 4字节的魔数
        out.writeBytes(new byte[]{1,2,3,4});
        //2  1字节的版本
        out.writeByte(1);
        //3. 1字节序列化方式 jdk 0, json 1
        out.writeByte(0);
        //4. 1字节的指令类型
        out.writeByte(message.getMessageType());
        //5. 4 个字节 请求序号
        out.writeInt(message.getSequenceId());
        //无意义,对齐填充 目的达到2的几次方
        out.writeByte(0xff);
        //6 获取内容的字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(message);
        byte[] bytes = bos.toByteArray();
        //7.长度
        out.writeInt(bytes.length);
        //8写入内容
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //一个字节直接byte 多个字节用int
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        //这个接收只是用来填充无意义不需要接收
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message = (Message) ois.readObject();
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", message);
        out.add(message);
    }
}
