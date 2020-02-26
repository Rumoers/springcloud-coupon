package com.xzh.coupon.executor;

import com.alibaba.fastjson.JSON;
import com.xzh.coupon.vo.GoodsInfo;
import com.xzh.coupon.vo.SettlementInfo;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规则执行器抽象类, 定义通用方法
 */
public abstract class AbstractExecutor {

    /**
     * 校验商品类型与优惠券是否匹配
     * 需要注意:
     *  1. 这里实现的单品类优惠券的校验, 多品类优惠券重载此方法
     *  2. 商品只需要有一个优惠券要求的商品类型去匹配就可以
     * @param settlement
     * @return
     */
    @SuppressWarnings("all")
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlement) {
        //获取商品类型
        List<Integer> goodsType = settlement.getGoodsInfos().
                stream().map(GoodsInfo::getType).
                collect(Collectors.toList());
        //获取模版规则中规定的商品类型
        List<Integer> templateGoodsType = JSON.parseObject(
                settlement.getCouponAndTemplateInfos().get(0).getTemplateSDK()
                .getRule().getUsage().getGoodsType(), List.class);
        // 存在交集即可
        return CollectionUtils.isNotEmpty(CollectionUtils.intersection(goodsType, templateGoodsType));
    }

    /**
     * 处理商品类型与优惠券限制不匹配的情况
     * @param settlement {@link SettlementInfo} 用户传递的结算信息
     * @param goodsSum  商品总价
     * @return  {@link SettlementInfo} 已经修改过的结算信息
     */
    protected SettlementInfo processGoodsTypeNotSatisfy(SettlementInfo settlement, double goodsSum) {

        boolean isGoodsTypeSatisfy = isGoodsTypeSatisfy(settlement);
        // 当商品类型不满足时, 直接返回总价, 并清空优惠券
        if (!isGoodsTypeSatisfy) {
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfos(Collections.emptyList());
            return settlement;
        }
        return null;
    }

    /**
     * 商品总价
     * @param goodsInfos
     * @return
     */
    protected double goodsCostSum(List<GoodsInfo> goodsInfos) {
        //获取单价*数量
        return goodsInfos.stream().mapToDouble(
                g -> g.getPrice() * g.getCount()).sum();
    }

    /**
     * 保留两位小数
     * @param value
     * @return
     */
    protected double retain2Decimals(double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 最小支付费用
     * @return
     */
    protected double minCost() {
        return 0.1;
    }
}
