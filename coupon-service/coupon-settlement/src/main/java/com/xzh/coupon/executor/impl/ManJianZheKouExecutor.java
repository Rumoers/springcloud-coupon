package com.xzh.coupon.executor.impl;

import com.alibaba.fastjson.JSON;
import com.xzh.coupon.constant.CouponCategory;
import com.xzh.coupon.constant.RuleFlag;
import com.xzh.coupon.executor.AbstractExecutor;
import com.xzh.coupon.executor.RuleExecutor;
import com.xzh.coupon.vo.GoodsInfo;
import com.xzh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 满减 + 折扣优惠券结算规则执行器
 */
@Slf4j
@Component
public class ManJianZheKouExecutor extends AbstractExecutor implements RuleExecutor {

    /**
     * 规则类型标记
     * @return {@link RuleFlag}
     */
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN_ZHEKOU;
    }

    /**
     * 校验商品类型与优惠券是否匹配
     * 需要注意:
     *  1. 这里实现的满减 + 折扣优惠券的校验
     *  2. 如果想要使用多类优惠券, 则必须要所有的商品类型都包含在内, 即差集为空
     * @param settlement {@link SettlementInfo} 用户传递的计算信息
     */
    @Override
    @SuppressWarnings("all")
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlement) {

        log.debug("Check ManJian And ZheKou Is Match Or Not!");
        List<Integer> goodsType = settlement.getGoodsInfos().stream().map(GoodsInfo::getType).collect(Collectors.toList());
        //存放多张优惠券支持的优惠商品类型
        List<Integer> templateGoodsType = new ArrayList<>();
        //遍历获取多张优惠券支持的商品类型
        settlement.getCouponAndTemplateInfos().forEach(ct -> {
            templateGoodsType.addAll(JSON.parseObject(ct.getTemplateSDK().getRule().getUsage().getGoodsType(), List.class));
        });

        // 如果想要使用多类优惠券, 则必须要所有的商品类型都包含在内
        // 即商品的类型-规定类型的差集为空
        return CollectionUtils.isEmpty(CollectionUtils.subtract(goodsType, templateGoodsType));
    }


    /**
     * 优惠券规则的计算
     * @param settlement {@link SettlementInfo} 包含了选择的优惠券
     * @return {@link SettlementInfo} 修正过的结算信息
     */
    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {

        double goodsSum = retain2Decimals(goodsCostSum(settlement.getGoodsInfos()));

        // 商品类型的校验
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlement, goodsSum);
        if (null != probability) {
            log.debug("ManJian And ZheKou Template Is Not Match To GoodsType!");
            return probability;
        }

        SettlementInfo.CouponAndTemplateInfo manJian = null;
        SettlementInfo.CouponAndTemplateInfo zheKou = null;

        //将优惠券分别填充到满减与折扣优惠券
        for (SettlementInfo.CouponAndTemplateInfo ct : settlement.getCouponAndTemplateInfos()) {
            if (CouponCategory.of(ct.getTemplateSDK().getCategory()) == CouponCategory.MANJIAN) {
                manJian = ct;
            } else {
                zheKou = ct;
            }
        }

        assert null != manJian;
        assert null != zheKou;

        // 当前的折扣优惠券和满减券如果不能共用(一起使用), 清空优惠券, 返回商品原价
        if (!isTemplateCanShared(manJian, zheKou)) {
            log.debug("Current ManJian And ZheKou Can Not Shared!");
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfos(Collections.emptyList());
            return settlement;
        }

        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = new ArrayList<>();
        double manJianBase = (double)manJian.getTemplateSDK().getRule().getDiscount().getBase();
        double manJianQuota = (double)manJian.getTemplateSDK().getRule().getDiscount().getQuota();

        // 最终的价格
        double targetSum = goodsSum;
        // 先计算满减
        if (targetSum >= manJianBase) {
            targetSum -= manJianBase;
            ctInfos.add(manJian);
        }

        // 再计算折扣
        double zheKouQuota = (double)zheKou.getTemplateSDK().getRule().getDiscount().getQuota();
        targetSum *= zheKouQuota * 1.0 / 100;
        ctInfos.add(zheKou);

        settlement.setCouponAndTemplateInfos(ctInfos);
        settlement.setCost(retain2Decimals(Math.max(targetSum, minCost())));

        log.debug("Use ManJian And ZheKou Coupon Make Goods Cost From {} To {}", goodsSum, settlement.getCost());
        return settlement;
    }

    /**
     * 当前的两张优惠券是否可以共用
     * 即校验 TemplateRule 中的 weight 是否满足条件
     * @param manJian
     * @param zheKou
     * @return
     */
    @SuppressWarnings("all")
    private boolean isTemplateCanShared(SettlementInfo.CouponAndTemplateInfo manJian,
                                        SettlementInfo.CouponAndTemplateInfo zheKou) {
        String manjianKey = manJian.getTemplateSDK().getKey() + String.format("%04d", manJian.getTemplateSDK().getId());
        String zheKouKey = zheKou.getTemplateSDK().getKey() + String.format("%04d", zheKou.getTemplateSDK().getId());

        List<String> allSharedKeysForManjian = new ArrayList<>();
        allSharedKeysForManjian.add(manjianKey);
        allSharedKeysForManjian.addAll(JSON.parseObject(manJian.getTemplateSDK().getRule().getWeight(), List.class));

        List<String> allSharedKeysForZhekou = new ArrayList<>();
        allSharedKeysForZhekou.add(zheKouKey);
        allSharedKeysForZhekou.addAll(JSON.parseObject(zheKou.getTemplateSDK().getRule().getWeight(), List.class));

        return CollectionUtils.isSubCollection(
                Arrays.asList(manjianKey, zheKouKey), allSharedKeysForManjian)
                || CollectionUtils.isSubCollection(Arrays.asList(manjianKey, zheKouKey), allSharedKeysForZhekou);
    }
}
