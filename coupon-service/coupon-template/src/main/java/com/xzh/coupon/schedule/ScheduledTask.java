package com.xzh.coupon.schedule;

import com.xzh.coupon.dao.CouponTemplateDao;
import com.xzh.coupon.entity.CouponTemplate;
import com.xzh.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时清理已过期的优惠券模板
 */
@Slf4j
@Component
public class ScheduledTask {

    private final CouponTemplateDao templateDao;

    @Autowired
    public ScheduledTask(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    /**
     * 下线已过期的优惠券模板
     * 定时一小时检查一次
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void offlineCouponTemplate() {
        log.info("Start To Expire CouponTemplate");

        //获取当前标记未过期的模版
        List<CouponTemplate> templates = templateDao.findAllByExpired(false);
        //已全部过期
        if (CollectionUtils.isEmpty(templates)) {
            log.info("Done To Expire CouponTemplate.");
            return;
        }

        Date current = new Date();
        List<CouponTemplate> expiredTemplates = new ArrayList<>(templates.size());
        templates.forEach(t -> {
            // 根据优惠券模板规则中的 "过期规则" 校验模板是否过期
            TemplateRule rule = t.getRule();
            if (rule.getExpiration().getDeadline() < current.getTime()) {
                //设置为已过期并重新保存到数据库
                t.setExpired(true);
                expiredTemplates.add(t);
            }
        });

        if (CollectionUtils.isNotEmpty(expiredTemplates)) {
            log.info("Expired CouponTemplate Num: {}", templateDao.saveAll(expiredTemplates));
        }

        // TODO 发送短信或者邮件通知已下线的优惠券模板
        log.info("Done To Expire CouponTemplate.");
    }
}
