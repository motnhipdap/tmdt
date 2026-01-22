package com.dev.dungcony;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DungconyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DungconyApplication.class, args);
    }

}
