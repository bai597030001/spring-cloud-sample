package com.example.cloudzuul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 自定义Zuul回退机制处理器(提供一个回退机制当路由后面的服务发生故障时)
 * 用于定制FallBack返回的body、状态码、消息头Header
 */
@Component
public class CommonZuulFallbackProvider implements FallbackProvider {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * getRoute方法的返回值就是要监听的挂掉的微服务名字，这里只能是serviceId(该名称一定要是注册进入 eureka 微服务中的那个 serviceId 名称)，不能是url，
     * 指定为“xxx”，那么在xxx服务不存在时，就会去执行后面代码的逻辑，设置Code，body什么的自定义内容返回给调用者。
     * @return
     */
    @Override
    public String getRoute() {
        //微服务配了路由的话，就用配置的名称
        //return "xxx";
        //如果要为所有路由提供默认回退，可以创建FallbackProvider类型的bean并使getRoute方法返回*或null
        //return "*";
        return null;
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        if (cause != null) {
            String reason =cause.getMessage();
            logger.info("Excption {}", reason);
        }
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() {
                return 200;
            }

            @Override
            public String getStatusText(){
                return "OK";
            }

            @Override
            public void close() {}

            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream("The service is unavailable.".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
