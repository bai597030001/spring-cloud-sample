package com.example.serviceabisapi.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "service-abis-server")
public interface AbisService {

    @GetMapping("a")
    String getAbis(@RequestParam(defaultValue = "1", value = "start") int start, @RequestParam(defaultValue = "1", value = "end") int end);
}
