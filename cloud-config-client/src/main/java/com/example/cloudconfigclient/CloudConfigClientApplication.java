package com.example.cloudconfigclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@SpringBootApplication
public class CloudConfigClientApplication {

    @Value("${test.word}")
    private String configData;

    @GetMapping(value = "showWord")
    public String getConfigData(){
        return configData;
    }

    public static void main(String[] args) {
        SpringApplication.run(CloudConfigClientApplication.class, args);
    }
}
