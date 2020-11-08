package cn.oasys.web.common.intercepter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;


@Configuration
public class IntercepterConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new recordInterceptor()).addPathPatterns("/**").excludePathPatterns(Arrays.asList("/captcha","/logins","/static/**"
        ,"/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
                "/**/*.jpeg", "/**/*.gif", "/**/fonts/*", "/**/*.svg","/**/*.map","/**/*.map","/**/*.html","/**/*.ico"));
    }
}
