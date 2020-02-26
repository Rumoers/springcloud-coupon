package com.xzh.coupon.executor;

import com.xzh.coupon.constant.RuleFlag;
import com.xzh.coupon.vo.SettlementInfo;

/**
 * 优惠券模板规则处理器接口定义
 */
public interface RuleExecutor {

    /**
     * 规则类型标记
     * 用于定位到是哪种优惠
     * @return {@link RuleFlag}  规则枚举
     */
    RuleFlag ruleConfig();

    /**
     * 优惠券规则的计算
     * @param settlement {@link SettlementInfo} 包含了选择的优惠券
     * @return  {@link SettlementInfo} 修正过的结算信息
     */
    SettlementInfo computeRule(SettlementInfo settlement);

}
