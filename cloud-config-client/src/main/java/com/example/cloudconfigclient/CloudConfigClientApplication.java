package com.example.cloudconfigclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@SpringBootApplication
public class CloudConfigClientApplication {

    @Value("${test.word}")
    private String configData;

    @Bean
    @ConfigurationProperties(prefix = "user")
    public User getUser() {
        return new User();
    }

    @Autowired
    private Environment environment;

    @GetMapping(value = "showWord")
    public String getConfigData(){
        System.out.println(configData);
        System.out.println("environment property: " +  environment.getProperty("test.world"));
        return configData;
        //return environment.getProperty("test.world");
    }

    @GetMapping(value = "showUser")
    public String getConfigUserData(){
        System.out.println();
        return getUser().toString();
    }

    public static void main(String[] args) {
        SpringApplication.run(CloudConfigClientApplication.class, args);
    }

}
