package com.example.cloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HystrixFallBackController {

    @GetMapping("/fallback")
    public Map<String, Object> fallback(int pageNum, int pageSize) {
        System.out.println("请求被熔断.");
        Map<String, Object> map = new HashMap<>();
        map.put("code", 429);
        map.put("message", "请求被熔断.");
        map.put("data", null);
        return map;
    }
}
