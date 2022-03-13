package com.baojikouyu.teach.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CloudConfig {

    @Bean
    @LoadBalanced //必须要加负载均衡器，不然调用不同。因为要给客户端指定负载均衡器
    public RestTemplate restTemplate (){
        return new RestTemplate();
    }

}
