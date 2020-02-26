package com.xzh.coupon.feign.hystrix;

import com.xzh.coupon.exception.CouponException;
import com.xzh.coupon.feign.SettlementClient;
import com.xzh.coupon.vo.CommonResponse;
import com.xzh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 结算微服务熔断策略实现
 */
@Slf4j
@Component
public class SettlementClientHystrix implements SettlementClient {

    /**
     * 优惠券规则计算
     * @param settlement {@link SettlementInfo}
     */
    @Override
    public CommonResponse<SettlementInfo> computeRule(SettlementInfo settlement) throws CouponException {

        log.error("[coupon-settlement] computeRule" + "request error");

        settlement.setEmploy(false);
        settlement.setCost(-1.0);

        return new CommonResponse<>(-1, "[coupon-settlement] request error", settlement);
    }
}
