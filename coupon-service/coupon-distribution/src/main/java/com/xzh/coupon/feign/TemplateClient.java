package com.xzh.coupon.feign;

import com.xzh.coupon.feign.hystrix.TemplateClientHystrix;
import com.xzh.coupon.vo.CommonResponse;
import com.xzh.coupon.vo.CouponTemplateSDK;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板微服务 Feign 接口定义
 */
@FeignClient(value = "coupon-template", fallback = TemplateClientHystrix.class)
public interface TemplateClient {

    /**
     * 查找所有可用的优惠券模板
     * */
    @RequestMapping(value = "/template/template/sdk/all", method = RequestMethod.GET)
    CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate();

    /**
     * 获取模板 ids 到 CouponTemplateSDK 的映射
     * */
    @RequestMapping(value = "/template/template/sdk/infos", method = RequestMethod.GET)
    CommonResponse<Map<Integer, CouponTemplateSDK>> findIds2TemplateSDK(@RequestParam("ids") Collection<Integer> ids);
}
