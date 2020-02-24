package com.xzh.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 优惠券分类
 */
@Getter
@AllArgsConstructor
public enum CouponCategory {

    MANJIAN("满减券", "001"),
    ZHEKOU("折扣券", "002"),
    LIJIAN("立减券", "003");

    /** 优惠券描述(分类) */
    //用于返回给用户展示
    private String description;

    /** 优惠券分类编码 */
    //主要用于存储
    private String code;

    /**
     * 根据code 返回分类枚举
     * @param code
     * @return
     */
    public static CouponCategory of(String code) {
        //不允许为空
        Objects.requireNonNull(code);
        //对当前枚举过滤
        //返回与code相同的任意一个枚举值
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()//若没有，則抛异常
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }

}
