package com.xzh.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AccessLogFilter extends AbstractPostZuulFilter {

    @Override
    protected Object cRun() {
        HttpServletRequest request = requestContext.getRequest();
        Long startTime = (Long) requestContext.get("startTime");
        String uri = request.getRequestURI();
        long duration = System.currentTimeMillis() - startTime;
        // 从网关通过的请求都会打印日志记录: uri + duration
        log.info("uri: {}, duration: {}", uri, duration);
        return success();
    }

    /**
     * SEND_RESPONSE_FILTER_ORDER=1000，若优先级超过1000 过滤器不执行
     * SEND_RESPONSE_FILTER_ORDER-1 表示此过滤器最后执行
     * 返回响应结果之前要执行这个Filter
     */
    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }
}
