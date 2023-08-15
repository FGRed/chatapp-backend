package net.chatapp.conf.mvc;

import net.chatapp.interceptor.BeforeRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new BeforeRequestInterceptor());
        //WebMvcConfigurer.super.addInterceptors(registry);
    }
}
