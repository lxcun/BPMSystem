package com.atw.bpmsystem.Common.filter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.http.HttpServletResponse;
//
//@Configuration
//@EnableWebMvc
//@ComponentScan
//public class WebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
//
//    private ApplicationContext applicationContext;
//
//    public WebConfig() {
//        super();
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
//        registry.addResourceHandler("/templates/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/");
//
//        super.addResourceHandlers(registry);
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//
//        this.applicationContext = applicationContext;
//    }
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        //拦截规则：除了login，其他都拦截判断
//        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/registerUser");
//        super.addInterceptors(registry);
//    }
//}