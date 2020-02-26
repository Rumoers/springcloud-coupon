package com.xzh.coupon.service;

import com.xzh.coupon.entity.Coupon;
import com.xzh.coupon.exception.CouponException;

import java.util.List;

/**
 * Redis相关的操作服务接口定义
 *  1. 用户的三个状态优惠券Cache相关操作
 *  2. 优惠券模板生成的优惠券码Cache操作
 */
public interface IRedisService {

    /**
     * 根据 userId 和状态找到缓存的用户某状态下优惠券列表数据
     * @param userId 用户id
     * @param status 优惠券状态 {@link com.xzh.coupon.constant.CouponStatus}
     * @return {@link Coupon}  注意, 可能会返回 null, 代表从没有过记录
     */
    List<Coupon> getCachedCoupons(Long userId, Integer status);

    /**
     * 保存空的优惠券列表到缓存中
     * @param userId 用户id
     * @param status 优惠券状态列表
     *
     * 主要目的是为了避免缓存穿透
     */
    void saveEmptyCouponListToCache(Long userId, List<Integer> status);

    /**
     * 尝试从Template 缓存中获取一个优惠券码
     * @param templateId 优惠券模板主键
     * @return 优惠券码  可能优惠券码领取完了返回空 null
     */
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    /**
     * 用户领取优惠券后，将优惠券与用户保存到 Cache 中
     * @param userId 用户id
     * @param coupons {@link Coupon}
     * @param status 优惠券状态  初始为未使用
     * @return 保存成功的个数
     * @throws CouponException
     */
    Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException;
}
