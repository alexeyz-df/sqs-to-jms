package com.devfactory.sqstojms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * Spring boot application to listen SQS and forward to JSM
 */
@SpringBootApplication
@EnableJms
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
