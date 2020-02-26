package com.xzh.coupon.converter;

import com.xzh.coupon.constant.CouponStatus;

import javax.persistence.AttributeConverter;

/**
 * 优惠券状态枚举属性转换器
 */
public class CouponStatusConverter implements AttributeConverter<CouponStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CouponStatus couponStatus) {
        return couponStatus.getCode();
    }

    @Override
    public CouponStatus convertToEntityAttribute(Integer code) {
        return CouponStatus.of(code);
    }
}
