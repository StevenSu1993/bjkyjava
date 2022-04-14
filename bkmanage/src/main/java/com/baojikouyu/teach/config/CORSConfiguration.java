package com.baojikouyu.teach.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CORSConfiguration implements WebMvcConfigurer {

/*
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")//项目中的所有接口都支持跨域
                        .allowedOrigins("*")//所有地址都可以访问，也可以配置具体地址
                        .allowCredentials(true)
                        .maxAge(3600)// 跨域允许时间
                        .allowedMethods("*");//"GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"
            }
        };
    }
*/

    // 跨域问题 在shiro 中做了跨域的处理
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")//项目中的所有接口都支持跨域
                .allowedOrigins("*")//所有地址都可以访问，也可以配置具体地址
                .allowCredentials(true)
//                .maxAge(3600)// 跨域允许时间
                .allowedMethods("*");//"GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"
    }
}
