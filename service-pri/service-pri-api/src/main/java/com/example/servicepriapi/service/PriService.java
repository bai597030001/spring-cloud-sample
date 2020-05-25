package com.example.servicepriapi.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "service-pri-server")
public interface PriService {

    @GetMapping("pri")
    String getPri(@RequestParam(defaultValue = "1", value = "start") int start, @RequestParam(defaultValue = "1", value = "end") int end);

}
