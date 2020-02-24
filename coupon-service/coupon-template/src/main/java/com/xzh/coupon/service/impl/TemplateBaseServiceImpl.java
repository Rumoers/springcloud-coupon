package com.xzh.coupon.service.impl;

import com.xzh.coupon.dao.CouponTemplateDao;
import com.xzh.coupon.entity.CouponTemplate;
import com.xzh.coupon.exception.CouponException;
import com.xzh.coupon.service.ITemplateBaseService;
import com.xzh.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 优惠券模板基础服务接口实现
 */
@Slf4j
@Service
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    private final CouponTemplateDao couponTemplateDao;

    @Autowired
    public TemplateBaseServiceImpl(CouponTemplateDao couponTemplateDao) {
        this.couponTemplateDao = couponTemplateDao;
    }

    /**
     * 根据优惠券模板 id 获取优惠券模板信息
     * @param id 模板id
     * @return {@link CouponTemplate} 优惠券模板实体
     * @throws CouponException
     */
    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws CouponException {
        Optional<CouponTemplate> template = couponTemplateDao.findById(id);
        if (!template.isPresent()) {
            throw new CouponException("Template is not exist: " + id);
        }
        return template.get();
    }

    /**
     * 查找所有可用的优惠券模板
     * @return {@link CouponTemplateSDK} s
     */
    @Override
    public List<CouponTemplateSDK> findAllUsableTemplate() {
        /**
         * 因为是定期清理过期键，所以获取的值可能是过期但未被处理的
         * 调用方仍需判断日期是否合法
         */
        List<CouponTemplate> templates = couponTemplateDao.findAllByAvailableAndExpired(true, false);
        return templates.stream().map(this::template2TemplateSDK).collect(Collectors.toList());
    }

    /**
     * 获取模板 ids 到 CouponTemplateSDK 的映射
     *
     * @param ids 模板 ids
     * @return Map<key: 模板 id, value: CouponTemplateSDK>
     */
    @Override
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids) {
        List<CouponTemplate> couponTemplates = couponTemplateDao.findAllById(ids);
        return couponTemplates.stream().
                map(this::template2TemplateSDK).
                collect(Collectors.toMap(CouponTemplateSDK::getId, Function.identity()));
    }

    /**
     * 将 CouponTemplate 转换为 CouponTemplateSDK
     *
     * @param template
     * @return
     */
    private CouponTemplateSDK template2TemplateSDK(CouponTemplate template) {
        return new CouponTemplateSDK(
                template.getId(),
                template.getName(),
                template.getLogo(),
                template.getDesc(),
                template.getCategory().getCode(),
                template.getProductLine().getCode(),
                //并不是拼装好的Template Key
                template.getKey(),
                template.getTarget().getCode(),
                template.getRule()
        );
    }
}
