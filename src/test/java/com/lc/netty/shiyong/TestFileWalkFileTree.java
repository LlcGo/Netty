package com.lc.netty.shiyong;

import sun.reflect.generics.visitor.Visitor;

import java.io.File;
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

    //递归删除文件
    public static void main(String[] args) throws IOException {
        Files.walkFileTree(Paths.get("D:\\youda2"),new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("进入======》"+ dir);
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                System.out.println("退出<=====" + dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    //查找jar包
    public static void m2(String[] args) throws IOException {
        //查看有多少个jar包
        AtomicInteger atomicInteger = new AtomicInteger();
        Files.walkFileTree(Paths.get("C:\\Program Files\\Java\\jdk1.8.0_144"),new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".jar")) {
                    atomicInteger.incrementAndGet();
                    System.out.println(file);
                }
                return super.visitFile(file, attrs);
            }
        });
        System.out.println("jar count:" + atomicInteger);
    }

    //遍历所有文件个数
    public static void ma1(String[] args) throws IOException {
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
