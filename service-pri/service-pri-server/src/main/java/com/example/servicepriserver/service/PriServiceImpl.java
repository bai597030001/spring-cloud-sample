package com.example.servicepriserver.service;

import com.example.servicepriapi.service.PriService;
import org.springframework.stereotype.Service;

@Service
public class PriServiceImpl implements PriService {
    @Override
    public String getPri(int start, int end) {
        return "pri service imple getPri()";
    }
}
