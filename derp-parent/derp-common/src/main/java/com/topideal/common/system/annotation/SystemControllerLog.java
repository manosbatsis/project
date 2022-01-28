package com.topideal.common.system.annotation;

import java.lang.annotation.*;

/**
 *自定义注解 拦截Controller
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface SystemControllerLog {
    //功能模块
     String model() default "";
    //埋点
    String point() default "";
    //功能描述
    String keyword()  default "";

}
