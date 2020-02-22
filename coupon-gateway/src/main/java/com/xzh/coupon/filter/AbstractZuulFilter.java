package com.xzh.coupon.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.RequestDispatcher;

/**
 * 通用的抽象过滤器类
 */
//自定义抽象过滤器类需继承ZuulFilter
public abstract class AbstractZuulFilter extends ZuulFilter {

    /**
     * 用于在过滤器之间传递消息，
     * 数据保存在每个请求的 ThreadLocal中  线程安全
     * 扩展了 ConcurrentHashMap
     */
    RequestContext requestContext;
    /**
     * 用于定义是否会向下执行其他过滤器
     * 如token发生错误，下面的过滤器就不去执行
     */
    private static final String NEXT = "next";

    /**
     * 该方法返回true时说明run方法会被调用
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();

        return (boolean) requestContext.getOrDefault(NEXT, true);
    }

    protected abstract Object cRun();

    //过滤器最主要的实现功能  当shouldFilter为true才执行
    @Override
    public Object run() throws ZuulException {
        requestContext = RequestContext.getCurrentContext();
        //执行各过滤器自定义的cRun方法
        return cRun();
    }

    //通过
    Object success() {
        requestContext.set(NEXT, true);
        return null;
    }

    //当前过滤器不通过
    Object fail(int code, String msg) {

        //将next设为false  不会往下执行
        requestContext.set(NEXT, false);
        //设为false 不需要去执行其他的程序逻辑
        requestContext.setSendZuulResponse(false);

        //设置相应信息
        requestContext.getResponse().setContentType("text/html;charset=UTF-8");
        requestContext.setResponseStatusCode(code);
        requestContext.setResponseBody(String.format("{\"result\": \"%s!\"}", msg));
        return null;
    }
}
