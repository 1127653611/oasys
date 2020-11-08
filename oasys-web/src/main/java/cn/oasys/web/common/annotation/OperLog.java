package cn.oasys.web.common.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OperLog {
    String operModul() default ""; // 操作模块

    String operDesc() default "";  // 操作说明
}
