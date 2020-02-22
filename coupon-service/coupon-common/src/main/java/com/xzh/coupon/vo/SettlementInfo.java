package com.xzh.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 结算信息对象定义
 * 包含:
 *  1. userId
 *  2. 商品信息(列表)
 *  3. 优惠券列表
 *  4. 结算结果金额
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementInfo {

    /** 用户id */
    private Long userId;

    /** 商品列表 */
    private List<GoodsInfo> goodsInfos;

    /** 优惠券列表 */
    private List<CouponAndTemplateInfo> couponAndTemplateInfos;

    /** 是否使结算生效, 即核销。 true-核销;false-结算 */
    private Boolean employ;

    /** 结算结果金额 */
    private Double cost;

    /**
     * 优惠券和模板信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponAndTemplateInfo {

        /** Coupon的主键 */
        private Integer id;

        /** 优惠券对应的模板对象 */
        private CouponTemplateSDK templateSDK;
    }
}
