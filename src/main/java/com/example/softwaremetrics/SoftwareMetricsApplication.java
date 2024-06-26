package com.example.softwaremetrics;

import com.example.softwaremetrics.Common.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class SoftwareMetricsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftwareMetricsApplication.class, args);
        init();
    }

    private static void init() {
        File file = new File(Constant.UPLOAD_PATH);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            System.out.println("创建文件夹" + mkdirs + (mkdirs ? "成功" : "失败"));
        }
    }
}
