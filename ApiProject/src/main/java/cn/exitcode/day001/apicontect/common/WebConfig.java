package cn.exitcode.day001.apicontect.common;

import cn.exitcode.day001.apicontect.common.JwtToken.AdminInterceptor;
import cn.exitcode.day001.apicontect.common.JwtToken.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminIntercept;
    @Autowired
    private UserInterceptor userIntercept;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminIntercept)
                .addPathPatterns("/**/Admin/**")
                .excludePathPatterns("/**/Admin/login");
        registry.addInterceptor(userIntercept)
                .addPathPatterns("/**/User/**")
                .excludePathPatterns("/**/User/login", "/**/User/reg", "/**/User/isUser", "/**/User/regcode");
    }
}
