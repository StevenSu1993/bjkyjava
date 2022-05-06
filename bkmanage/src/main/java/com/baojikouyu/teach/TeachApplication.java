package com.baojikouyu.teach;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableCaching //redis缓存
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients //使用feignClient
public class TeachApplication {

/*
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection securityCollection = new SecurityCollection();
                securityCollection.addPattern("/*");
                securityConstraint.addCollection(securityCollection);
                context.addConstraint(securityConstraint);
            }
        };
        factory.addAdditionalTomcatConnectors(httpConnector());
        return factory;
    }

    @Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //Connector监听的http的端口号
        connector.setPort(8090);
        connector.setSecure(false);
        //监听到http的端口号后转向到的https的端口号
        connector.setRedirectPort(8091);
        return connector;
    }*/


    public static void main(String[] args) {
        final ConfigurableApplicationContext run = SpringApplication.run(TeachApplication.class, args);

  /*      ShiroFilterFactoryBean shiroFilter = (ShiroFilterFactoryBean) run.getBean(ShiroFilterFactoryBean.class);
        System.out.println(shiroFilter);
        InvalidRequestFilter bean1 = run.getBean(InvalidRequestFilter.class);
        System.out.println(bean1);
        final LogConfig bean = run.getBean(LogConfig.class);
        System.out.println(bean);*/

    }
}
