package com.nyx.musicPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 音乐平台主启动类
 * 
 * @author nyx
 * @since 2025-06
 */
@SpringBootApplication
public class MusicPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicPlatformApplication.class, args);
        System.out.println("启动成功！");
    }

}