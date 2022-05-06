package com.baojikouyu.teach.config;

import com.baojikouyu.teach.filter.JWTFilter;
import com.baojikouyu.teach.shiro.CustomAuthenticationCache;
import com.baojikouyu.teach.shiro.CustomAuthorizationCache;
import com.baojikouyu.teach.shiro.MyRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.InvalidRequestFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ShiroConfig {

    @Bean("securityManager")
    public DefaultWebSecurityManager getManager(@Lazy MyRealm realm, @Lazy RedisTemplate redisTemplate) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自己的realm
/*        realm.setCachingEnabled(true);
        realm.setAuthorizationCachingEnabled(true);
        realm.setCacheManager(customCacheManager);
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthenticationCacheName("authentication_cache");
        realm.setAuthorizationCacheName("authorization_cache");*/
        realm.setAuthenticationCache(new CustomAuthenticationCache<>(redisTemplate));
        realm.setAuthorizationCache(new CustomAuthorizationCache<>(redisTemplate));
        manager.setRealm(realm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        //设置redis 缓存器 如果想只实现权限控制可以用这个方法， 如果想实现权限和登录都缓存的话这个不行
//        manager.setCacheManager(customCacheManager);

        return manager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean factory(@Lazy DefaultWebSecurityManager securityManager) {
//    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager,InvalidRequestFilter invalidRequestFilter) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JWTFilter());
        factoryBean.setFilters(filterMap);

        factoryBean.setSecurityManager(securityManager);
        factoryBean.setUnauthorizedUrl("/401");

        final Map<String, Filter> filters = factoryBean.getFilters();
/*        filters.remove("invalidRequest");
        filters.put("invalidRequest", invalidRequestFilter);*/
        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = new HashMap<>();
        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/**", "jwt");
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/401", "anon");
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Lazy DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    //路径中有中文的时候，shiro 是会拦截的, 不安全
    // @Bean
    public InvalidRequestFilter invalidRequestFilter() {
        InvalidRequestFilter invalidRequestFilter = new InvalidRequestFilter();
        invalidRequestFilter.setBlockNonAscii(false);
        invalidRequestFilter.setBlockSemicolon(false);
        return invalidRequestFilter;
    }

}
