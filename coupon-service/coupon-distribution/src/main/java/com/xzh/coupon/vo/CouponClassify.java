package com.xzh.coupon.vo;

import com.xzh.coupon.constant.CouponStatus;
import com.xzh.coupon.constant.PeriodType;
import com.xzh.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户优惠券的分类, 根据优惠券状态
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponClassify {

    /** 可以使用的 */
    private List<Coupon> usable;

    /** 已使用的 */
    private List<Coupon> used;

    /** 已过期的 */
    private List<Coupon> expired;

    /**
     * 对当前的优惠券进行分类
     * @param coupons
     * @return
     */
    public static CouponClassify classify(List<Coupon> coupons) {
        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());

        coupons.forEach(c -> {

            // 判断优惠券是否过期
            boolean isTimeExpire;
            long curTime = new Date().getTime();
            //固定日期
            if (c.getTemplateSDK().getRule().getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())) {
                isTimeExpire = c.getTemplateSDK().getRule().getExpiration().getDeadline() <= curTime;
            } else {//可变日期  领取时间+有效天数与当前时间潘判断
                isTimeExpire = DateUtils.addDays(c.getAssignTime(), c.getTemplateSDK().getRule().getExpiration().getGap()).getTime() <= curTime;
            }

            if (c.getStatus() == CouponStatus.USED) {
                used.add(c);
                //已标记过期  或  刚判断已过期
            } else if (c.getStatus() == CouponStatus.EXPIRED || isTimeExpire) {
                expired.add(c);
            } else {
                usable.add(c);
            }
        });

        return new CouponClassify(usable, used, expired);
    }
}
