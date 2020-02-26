package com.xzh.coupon.executor.impl;

import com.xzh.coupon.constant.RuleFlag;
import com.xzh.coupon.executor.AbstractExecutor;
import com.xzh.coupon.executor.RuleExecutor;
import com.xzh.coupon.vo.CouponTemplateSDK;
import com.xzh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 立减优惠券结算规则执行器
 */
@Slf4j
@Component
public class LiJianExecutor extends AbstractExecutor implements RuleExecutor {

    /**
     * 规则类型标记
     * @return {@link RuleFlag}
     */
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.LIJIAN;
    }

    /**
     * 优惠券规则的计算
     * @param settlement {@link SettlementInfo} 包含了选择的优惠券
     * @return {@link SettlementInfo} 修正过的结算信息
     */
    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {
        double goodsSum = retain2Decimals(goodsCostSum(settlement.getGoodsInfos()));
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlement, goodsSum);
        if (null != probability) {
            log.debug("LiJian Template Is Not Match To GoodsType!");
            return probability;
        }

        // 立减优惠券直接使用, 没有门槛
        CouponTemplateSDK templateSDK = settlement.getCouponAndTemplateInfos().get(0).getTemplateSDK();
        double quota = (double)templateSDK.getRule().getDiscount().getQuota();

        // 计算使用优惠券之后的价格 - 结算
        settlement.setCost(retain2Decimals(goodsSum - quota) > minCost() ? retain2Decimals(goodsSum - quota) : minCost());
        log.debug("Use LiJian Coupon Make Goods Cost From {} To {}", goodsSum, settlement.getCost());
        return settlement;
    }
}
