package com.xzh.coupon.feign;

import com.xzh.coupon.exception.CouponException;
import com.xzh.coupon.feign.hystrix.SettlementClientHystrix;
import com.xzh.coupon.vo.CommonResponse;
import com.xzh.coupon.vo.SettlementInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 优惠券结算微服务 Feign 接口定义
 */
@FeignClient(value = "coupon-settlement", fallback = SettlementClientHystrix.class)
public interface SettlementClient {

    @RequestMapping(value = "/settlement/settlement/compute", method = RequestMethod.POST)
    CommonResponse<SettlementInfo> computeRule(@RequestBody SettlementInfo settlement) throws CouponException;
}
