package com.yonyou.myself.util;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @Author :liyanan
 * @Date : 2021/2/22 14:12
 * @Description : 定义接口注解类信息
 ***/
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DesensitizedAnnotation {
    /**脱敏数据类型（规则）*/
   TypeEnum  type();
   /**判断注解是否生效*/
   String isEffictiveMethod() default "";
}
