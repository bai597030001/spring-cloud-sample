package com.example.servicepriserver.controller;

import com.example.serviceabisapi.service.AbisService;
import com.example.servicepriapi.service.PriService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@CrossOrigin(allowCredentials = "true")
public class PriController {

    @Resource
    private PriService priService;

    @Resource
    private AbisService abisService;

    /**
     * 注：当application.yml配置文件中没有指定服务启动端口时，
     * 不能使用@LocalServerPort和@Value("${local.server.port}")的方法获取端口号，
     * 只能使用environment.getProperty("local.server.port")的方式
     */
    @Value("${server.port}")
    private String port ;

    @GetMapping("pri")
    public String getPri(@RequestParam(defaultValue = "1", value = "start") int start, @RequestParam(defaultValue = "1", value = "end") int end){
        return "instance port is " + port + " was used. " + priService.getPri(start, end);
    }

    @GetMapping("abis")
    public String getA(@RequestParam(defaultValue = "1", value = "start") int start, @RequestParam(defaultValue = "1", value = "end") int end){
        return "instance port is " + port + " was used. " + abisService.getAbis(start, end);
    }

}
