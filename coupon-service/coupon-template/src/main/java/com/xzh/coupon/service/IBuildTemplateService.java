package com.xzh.coupon.service;

import com.xzh.coupon.entity.CouponTemplate;
import com.xzh.coupon.exception.CouponException;
import com.xzh.coupon.vo.TemplateRequest;

/**
 * 构建优惠券模板接口定义
 */
public interface IBuildTemplateService {

    /**
     * 创建优惠券模板
     * @param request {@link TemplateRequest} 模板信息请求对象
     * @return {@link CouponTemplate} 优惠券模板实体
     * @throws CouponException
     */
    CouponTemplate buildTemplate(TemplateRequest request) throws CouponException;
}