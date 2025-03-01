package cn.exitcode.day001.apicontect;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ApiContectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiContectApplication.class, args);
    }

}
