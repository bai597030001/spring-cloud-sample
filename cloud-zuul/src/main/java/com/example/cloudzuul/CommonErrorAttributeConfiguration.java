package com.example.cloudzuul;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CommonErrorAttributeConfiguration extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, boolean includeStackTrace) {
        Map<String, Object> result = super.getErrorAttributes(requestAttributes, includeStackTrace);
        //不是状态码429就不用自定义返回处理;
        if(!result.get("status").equals(429)){
            return result;
        }
        //修改返回状态码为200
        requestAttributes.setAttribute("javax.servlet.error.status_code",200,RequestAttributes.SCOPE_REQUEST);
        //自定义消息体 需要返回一个map，如果是实体类，可以转为map再返回
        Map<String,Object> map = new HashMap<>();
        map.put("code","429");
        map.put("message","请勿频繁请求");
        return map;
    }

}
