package com.xzh.coupon.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * 用于定于pre类型的过滤器
 */

public abstract class AbstractPreZuulFilter extends AbstractZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
}
