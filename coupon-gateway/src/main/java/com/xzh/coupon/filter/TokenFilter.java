package com.xzh.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 校验请求中传递的token
 * 在功能执行之前
 * 所以使用preFilter
 */
@Slf4j
@Component
public class TokenFilter extends AbstractPreZuulFilter {

    @Override
    protected Object cRun() {
        //获取request
        HttpServletRequest request = requestContext.getRequest();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        String token = request.getParameter("token");
        if (null == token) {
            log.error("error: token is empty");
            return fail(401, "error: token is empty");
        }
        return success();
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
