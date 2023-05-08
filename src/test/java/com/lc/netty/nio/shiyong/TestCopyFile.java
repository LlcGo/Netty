package com.lc.netty.nio.shiyong;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author Lc
 * @Date 2023/5/5
 * @Description
 */
public class TestCopyFile {
    public static void main(String[] args) {
        String file = "D:\\youdao";
        String copyFile = "D:\\youda2";
        try {
            Files.walk(Paths.get(file)).forEach(path -> {
                String replace = path.toString().replace(file, copyFile);
                try {
                    if (Files.isDirectory(path)) {
                         Files.createDirectories(Paths.get(replace));
                    }else {
                        Files.copy(path,Paths.get(replace));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
