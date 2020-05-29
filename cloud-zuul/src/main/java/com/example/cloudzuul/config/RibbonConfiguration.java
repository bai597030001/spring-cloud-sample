package com.example.cloudzuul.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfiguration {

    //IRule就是所有规则的标准
    @Bean
    public IRule ribbonRule() {

        //自定义
        //return new MyRoundRobinRule();// 我自定义为每台机器5次

        // Ribbon随机
        // return new RandomRule();

        // Ribbon默认的轮询
        return new RoundRobinRule();

        //会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，还有并发的连接数量超过阈值的服务。然后对剩余的服务列表按照轮询策略进行访问。
        //return new AvailabilityFilteringRule();

        //根据平均响应时间计算所有服务的权重，响应时间越快服务权重越大被选中的概率越高。刚启动时如果统计信息不足，会使用 RoundRobinRule策略。等统计信息足够，会切换到WeightedResponseTimeRule。
        //return new WeightedResponseTimeRule();

        //先按照 RoundRobinRule策略获取服务，如果获取服务失败则在指定时间内会进行重试获取可用的服务。
        //return new RetryRule();

        //会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务,然后选取一个并发量最小的服务。
        //return new BestAvailableRule();

        //复合判断Server所在区域的性能和Server的可用性来选择服务器。
        //return new ZoneAvoidanceRule();
    }

}
