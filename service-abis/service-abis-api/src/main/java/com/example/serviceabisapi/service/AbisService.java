package com.example.serviceabisapi.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//TODO feign提供回退策略/回退工厂
@FeignClient(value = "service-abis-server")
public interface AbisService {

    @GetMapping("abis")
    String getAbis(@RequestParam(defaultValue = "1", value = "start") int start, @RequestParam(defaultValue = "1", value = "end") int end);
}
