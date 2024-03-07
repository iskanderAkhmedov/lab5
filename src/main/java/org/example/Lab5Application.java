package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example")
public class Lab5Application {
    public static void main(String[] args) {
        SpringApplication.run(Lab5Application.class, args);
    }
}