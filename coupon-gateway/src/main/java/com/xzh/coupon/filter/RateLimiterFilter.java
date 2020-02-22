package com.xzh.coupon.filter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 限流过滤器
 * 例如一小时允许访问多少次
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class RateLimiterFilter extends AbstractPreZuulFilter {

    /** 每秒可以获取到两个令牌 */
    RateLimiter rateLimiter = RateLimiter.create(2);

    @Override
    protected Object cRun() {
        HttpServletRequest request = requestContext.getRequest();
        //尝试去过去令牌
        if (rateLimiter.tryAcquire()) {
            log.info("get rate token success");
            return success();
        } else {
            log.error("rate limit: {}", request.getRequestURI());
            return fail(402, "error: rate limit");
        }
    }

    /**
     * 优先级低于TokenFilter的优先级
     * 通过验证才能进行限流
     * @return
     */
    @Override
    public int filterOrder() {
        return 2;
    }
}
