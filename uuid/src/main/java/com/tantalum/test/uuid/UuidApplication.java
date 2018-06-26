package com.tantalum.test.uuid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:uuid.properties")
public class UuidApplication {

    public static void main(String[] args) {
        SpringApplication.run(UuidApplication.class, args);
    }
}

