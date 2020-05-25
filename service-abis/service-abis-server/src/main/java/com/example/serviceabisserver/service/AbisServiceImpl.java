package com.example.serviceabisserver.service;

import com.example.serviceabisapi.service.AbisService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

@Service
public class AbisServiceImpl implements AbisService {

    @Override
    public String getAbis(int start, int end) {
        return "abis service getAbis()";
    }
}
