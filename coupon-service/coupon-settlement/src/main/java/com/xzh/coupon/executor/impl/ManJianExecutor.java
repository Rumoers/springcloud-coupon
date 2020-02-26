package com.xzh.coupon.executor.impl;

import com.xzh.coupon.constant.RuleFlag;
import com.xzh.coupon.executor.AbstractExecutor;
import com.xzh.coupon.executor.RuleExecutor;
import com.xzh.coupon.vo.CouponTemplateSDK;
import com.xzh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 满减优惠券结算规则执行器
 */
@Slf4j
@Component
public class ManJianExecutor extends AbstractExecutor implements RuleExecutor {

    /**
     * 规则类型标记
     * @return {@link RuleFlag}
     */
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN;
    }

    /**
     * 优惠券规则的计算
     * @param settlement {@link SettlementInfo} 包含了选择的优惠券
     * @return {@link SettlementInfo} 修正过的结算信息
     */
    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {

        double goodsSum = retain2Decimals(goodsCostSum(settlement.getGoodsInfos()));
        //判断是否可用
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlement, goodsSum);
        if (null != probability) {
            log.debug("ManJian Template Is Not Match To GoodsType!");
            return probability;
        }

        // 判断满减是否符合折扣标准
        CouponTemplateSDK templateSDK = settlement.getCouponAndTemplateInfos().get(0).getTemplateSDK();
        double base = (double) templateSDK.getRule().getDiscount().getBase();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        /**
         * 总价必须满足满减规定金额
         * 如果不符合标准, 则直接返回商品总价
         */
        if (goodsSum < base) {
            log.debug("Current Goods Cost Sum < ManJian Coupon Base!");
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfos(Collections.emptyList());
            return settlement;
        }

        // 计算使用优惠券之后的价格 - 结算
        settlement.setCost(retain2Decimals(Math.max((goodsSum - quota), minCost())));
        log.debug("Use ManJian Coupon Make Goods Cost From {} To {}", goodsSum, settlement.getCost());
        return settlement;
    }
}
