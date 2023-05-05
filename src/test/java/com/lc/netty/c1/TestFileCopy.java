package com.lc.netty.c1;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @Author Lc
 * @Date 2023/5/4
 * @Description
 */
public class TestFileCopy {
    public static void main(String[] args) {
        try(FileChannel from = new FileInputStream("data.text").getChannel();
            FileChannel to = new FileOutputStream("data3.text").getChannel()) {
            //每次只能传2g
            long size = from.size();
            long left = size;
            while (left > 0){
               left -= from.transferTo((size -left),left,to);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
