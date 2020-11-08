package cn.oasys.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class OasysWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(OasysWebApplication.class,args);
    }
}
