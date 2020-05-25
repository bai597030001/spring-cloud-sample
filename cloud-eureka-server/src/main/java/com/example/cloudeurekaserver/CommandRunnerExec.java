package com.example.cloudeurekaserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandRunnerExec implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("111");
        Thread.sleep(1000);
    }
}
