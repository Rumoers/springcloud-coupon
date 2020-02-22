package com.xzh.coupon.advice;

import com.xzh.coupon.annotation.IgnoreResponseAdvice;
import com.xzh.coupon.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应(在返回响应结果之前做处理)
 * advice：对responBody进行增强
 */
//拦截所有controller的返回，然后对返回进行一些处理
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要对响应进行处理
     *  方法中的两个判断对应{@code @IgnoreResponseAdvice}注解的ElementType.TYPE, ElementType.METHOD
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果当前方法 . 所在的类 . 标识了 @IgnoreResponseAdvice 注解, 不需要处理
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        // 如果当前方法 . 标识了 @IgnoreResponseAdvice 注解, 不需要处理
        if (methodParameter.getMethod().getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        // 为true时, 对响应进行处理, 执行 beforeBodyWrite 方法
        return true;
    }

    /**
     * 在写入响应流之前处理(响应返回之前的处理)
     * o  controller 返回的响应
     */
    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        // 定义最终的返回对象
        CommonResponse<Object> response = new CommonResponse<>(0, "");
        // 如果 o 是 null, response 不需要设置 data
        if (null == o) {
            return response;
        } else if (o instanceof CommonResponse) {
            // 如果 o 已经是 CommonResponse, 不需要再次处理
            return (CommonResponse)o;
        } else {
            // 否则, 把响应对象作为 CommonResponse 的 data 部分
            response.setData(o);
        }
        return response;
    }
}
