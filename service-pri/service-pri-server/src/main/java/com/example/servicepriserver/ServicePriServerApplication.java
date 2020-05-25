package com.example.servicepriserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.serviceabisapi")
@EnableDiscoveryClient
public class ServicePriServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicePriServerApplication.class, args);
    }
}
