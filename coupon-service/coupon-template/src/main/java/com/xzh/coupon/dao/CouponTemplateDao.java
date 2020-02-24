package com.xzh.coupon.dao;

import com.xzh.coupon.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *T:实体类
 *ID：实体类主键类型
 */
public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Integer> {

    /**
     * 根据模板名称查找模板
     */
    CouponTemplate findByName(String name);

    /** 根据可用状态和过期标志(未过期或已过期)查询模板 */
    /**
     *findAllBy+AvailableAndExpired= where available=...and Expired=...
     */
    List<CouponTemplate> findAllByAvailableAndExpired(boolean available, boolean expired);

    /** 根据过期标志查询模板 */
    List<CouponTemplate> findAllByExpired(boolean expired);
}
