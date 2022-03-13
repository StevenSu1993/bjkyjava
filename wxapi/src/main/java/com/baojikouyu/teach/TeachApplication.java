package com.baojikouyu.teach;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan(basePackages = "com.baojikouyu.teach.dao")
@SpringBootApplication
@EnableCaching
public class TeachApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(TeachApplication.class, args);

//        while (true) {
//            //当动态配置刷新时，会更新到 Enviroment中，因此这里每隔一秒中从Enviroment中获取配置
//            String password = context.getEnvironment().getProperty("spring.datasource.data-password");
//            String username = context.getEnvironment().getProperty("spring.datasource.data-username");
//            System.out.println("在nacos中能拿到 username 的值为 ：" + username + "  password的值为: " + password);
//            TimeUnit.SECONDS.sleep(1);
//        }
    }

}
