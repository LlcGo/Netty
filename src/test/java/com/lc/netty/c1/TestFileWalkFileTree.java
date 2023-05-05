package com.lc.netty.c1;

import sun.reflect.generics.visitor.Visitor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Lc
 * @Date 2023/5/4
 * @Description
 */
public class TestFileWalkFileTree {
    public static void main(String[] args) throws IOException {
        AtomicInteger atomicInteger = new AtomicInteger();
        AtomicInteger atomicInteger1 = new AtomicInteger();
        //递归遍历所有文件
        Files.walkFileTree(Paths.get("C:\\Users\\Lc\\Pictures\\Screenshots"),new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                atomicInteger.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                atomicInteger1.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.out.println("目录有" + atomicInteger + "个");
        System.out.println("文件有" + atomicInteger1 + "个");
    }
}
