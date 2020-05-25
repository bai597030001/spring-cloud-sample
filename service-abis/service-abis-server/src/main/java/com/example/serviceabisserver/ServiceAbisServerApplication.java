package com.example.serviceabisserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.example.servicepriapi")
@EnableDiscoveryClient
@EnableHystrix
@SpringBootApplication
public class ServiceAbisServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAbisServerApplication.class, args);
    }

}
