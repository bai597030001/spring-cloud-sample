package com.example.cloudeurekaserver;

import com.netflix.discovery.EurekaClient;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InstanceMaintainer {

    @Resource
    private EurekaClient eurekaClient;

    @EventListener
    public void listenEvent(EurekaInstanceCanceledEvent event){
        System.out.println(event.toString());
    }
}
