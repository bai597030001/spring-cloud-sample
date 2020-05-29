package com.example.serviceabisserver.controller;

import com.example.servicepriapi.service.PriService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@CrossOrigin(allowCredentials = "true")
public class AbisController {

    @Resource
    private PriService priService;

    /**
     * 注：当application.yml配置文件中没有指定服务启动端口时，
     * 不能使用@LocalServerPort和@Value("${local.server.port}")的方法获取端口号，
     * 只能使用environment.getProperty("local.server.port")的方式
     */
    @Value("${server.port}")
    private String port ;

    @HystrixCommand(fallbackMethod = "getHystrixFallback")
    @GetMapping("pri")
    public String getPri(@RequestParam(defaultValue = "1", value = "start") int start, @RequestParam(defaultValue = "1", value = "end") int end){
        return priService.getPri(start, end);
    }

    @HystrixCommand(fallbackMethod = "getHystrixFallback")
    @GetMapping("abis")
    public String getAbis(@RequestParam(defaultValue = "1", value = "start") int start, @RequestParam(defaultValue = "1", value = "end") int end){
        return "instance port is " + port + " was used. ";
    }

    @HystrixCommand(fallbackMethod = "getHystrixFallback")
    @GetMapping("timeout")
    public String getTimeoutHystrix(){
        try {
            Thread.sleep(4500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "this api is time out.";
    }

    public String getHystrixFallback(){
        String s = "该次调用触发了hystrix方法保护机制";
        System.out.println(s);
        return s;
    }
    public String getHystrixFallback(int start, int end){
        String s = "该次调用触发了hystrix方法保护机制";
        System.out.println(s);
        return s;
    }
}
