/*
* Project: SmartGrid Load Shedding Optimizer
* Class: Main.java
* Description: Entry point for the Spring Boot framework that initializes the backend web application engine and embedded server.
* @Author: Areeb Bhuiyan
* @Version: September 3, 2026
* @Citation: Spring Boot Reference Documentation - Core Applications
* (https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.spring-application)
*/
package com.smartgrid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}