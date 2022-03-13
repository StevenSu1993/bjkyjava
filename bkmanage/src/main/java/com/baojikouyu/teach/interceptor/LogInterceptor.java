package com.baojikouyu.teach.interceptor;

import com.baojikouyu.teach.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j //这个注解可以在方法上直接使用log  依赖spring-boot-starter-we
public class LogInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        spring boot 2.0对静态资源也进行了拦截，当拦截器拦截到请求之后，但controller里并没有对应的请求时，该请求会被当成是对静态资源的请求。此时的handler就是 ResourceHttpRequestHandler，就会抛出上述错误。
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Log methodAnnotation = method.getMethodAnnotation(Log.class);
            if (methodAnnotation != null) {
                long start = System.currentTimeMillis();
                threadLocal.set(start);
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Log methodAnnotation = handlerMethod.getMethodAnnotation(Log.class);
            if (methodAnnotation != null) {
                String requestURI = request.getRequestURI();
                String globalMethonName = method.getDeclaringClass().getName() + "#" + method.getName();
                String desc = methodAnnotation.desc();
                long end = System.currentTimeMillis();
                Long start = threadLocal.get();
                long dur = end - start;
                threadLocal.remove(); //这里一定要remove 不会回内存溢出
                log.info("请求路径: {}, 请求方法 {}, 描述信息 {}, 总耗时 {},", requestURI, globalMethonName, desc, dur);
            }

        }
    }
}
