package org.faceview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FaceviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaceviewApplication.class, args);
    }
}
