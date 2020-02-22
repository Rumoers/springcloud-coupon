package com.xzh.coupon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略统一响应注解定义
 * 用于某些响应不需要使用同一响应格式
 */
//可定义在类或者方法上
@Target({ElementType.TYPE, ElementType.METHOD})
//运行时的注解
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {
}
