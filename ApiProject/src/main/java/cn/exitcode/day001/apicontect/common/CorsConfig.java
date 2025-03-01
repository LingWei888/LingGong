package cn.exitcode.day001.apicontect.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                //是否发送Cookie
                .allowCredentials(true)                //放行哪些原始域
                .allowedOrigins("http://localhost:8083", "http://localhost:8080", "http://localhost:8081","http://admin.exitcode.cn", "http://www.exitcode.cn", "http://user.exitcode.cn","http://*.exitcode.cn")
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"})
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}