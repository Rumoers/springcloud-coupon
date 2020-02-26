package com.xzh.coupon.dao;

import com.xzh.coupon.constant.CouponStatus;
import com.xzh.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Coupon Dao接口定义
 */
public interface CouponDao extends JpaRepository<Coupon, Integer> {

    /**
     * 根据userId + 状态寻找优惠券记录
     * @param userId
     * @param status
     * @return
     */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);
}
