package com.xzh.coupon.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 定制HTTP消息转换器
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     *  converters是当前系统中的所有转换器
     *  将java的实体对象转换为http数据的输出流
     *  MappingJackson2HttpMessageConverter会将java数据输出为json字符串数据格式
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //清空所有转换器并添加一个Java对象转Json格式的转换器
        converters.clear();
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
