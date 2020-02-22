package com.xzh.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 在过滤器中存储客户端发起请求的时间戳
 * 若需要统计用户访问的时间，需配合postFilter
 */
@Slf4j
@Component
public class PreRequestFilter extends AbstractPreZuulFilter {

    @Override
    protected Object cRun() {
        requestContext.set("startTime", System.currentTimeMillis());
        return success();
    }

    /**
     * 发起请求就执行过滤器，所以优先级最高
     */
    @Override
    public int filterOrder() {
        return 0;
    }
}
