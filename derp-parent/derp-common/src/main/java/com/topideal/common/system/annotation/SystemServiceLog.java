package com.topideal.common.system.annotation;

import java.lang.annotation.*;

/**
 *自定义注解 拦截service
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface SystemServiceLog {
    //功能模块
    String model() default "";
    //埋点
    String point() default "";
    //关键字
    String keyword() default "";

}