package com.xzh.coupon.service.impl;

import com.google.common.base.Stopwatch;
import com.xzh.coupon.constant.Constant;
import com.xzh.coupon.dao.CouponTemplateDao;
import com.xzh.coupon.entity.CouponTemplate;
import com.xzh.coupon.service.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 异步服务接口实现
 */
@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    /**
     * CouponTemplate Dao
     */
    private final CouponTemplateDao templateDao;

    /**
     * 注入 Redis 模板类
     */
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AsyncServiceImpl(CouponTemplateDao templateDao,
                            StringRedisTemplate redisTemplate) {
        this.templateDao = templateDao;
        this.redisTemplate = redisTemplate;
    }


    /**
     * 根据模板异步的创建优惠券码
     *
     * @param couponTemplate {@link CouponTemplate} 优惠券模板实体
     */
    @Async("getAsyncExecutor")
    @Override
    @SuppressWarnings("all")
    public void asyncConstructCouponByTemplate(CouponTemplate couponTemplate) {
        //计时器
        Stopwatch watch = Stopwatch.createStarted();

        Set<String> couponCodes = buildCouponCode(couponTemplate);
        String redisKey = String.format("%s%s", Constant.RedisPrefix.COUPON_TEMPLATE, couponTemplate.getId().toString());
        log.info("Push CouponCode To Redis: {}", redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));

        //优惠券码也生成，设置为可用状态
        couponTemplate.setAvailable(true);
        //保存模版信息到数据库
        templateDao.save(couponTemplate);

        watch.stop();
        log.info("Construct CouponCode By Template Cost: {}ms", watch.elapsed(TimeUnit.MILLISECONDS));

        // TODO 发送短信或者邮件通知优惠券模板已经可用 暂时以打日志形式代替
        log.info("CouponTemplate({}) Is Available!", couponTemplate.getId());
    }

    /**
     *   构造优惠券码
     *  优惠券码(对应于每一张优惠券, 18位):
     *      前四位: 产品线 + 类型
     *      中间六位: 日期随机(200101)
     *      后八位: 0 ~ 9 随机数构成
     * @param template {@link CouponTemplate} 实体类
     * @return Set<String> 与 template.count 相同个数的优惠券码
     */
    @SuppressWarnings("all")
    private Set<String> buildCouponCode(CouponTemplate template) {
        //计时器，计算创建所消耗时间
        Stopwatch watch = Stopwatch.createStarted();
        //创建模版数量大小的hashSet
        Set<String> result = new HashSet<>(template.getCount());
        //优惠券码前四位
        String prefix4 = template.getProductLine().getCode().toString() + template.getCategory().getCode();

        String date = new SimpleDateFormat("yyMMdd").format(template.getCreateTime());
        //遍历生成对应数量的优惠券码
        for (int i = 0; i != template.getCount(); ++i) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }
        // 产生重复码之后需要重新补上少的那部分优惠券码
        while (result.size() < template.getCount()) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }
        assert result.size() == template.getCount();

        watch.stop();
        log.info("Build Coupon Code Cost: {}ms", watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    /**
     * 构造优惠券码的后14位
     * @param date 创建优惠券的日期
     * @return 14位优惠券码
     */
    private String buildCouponCodeSuffix14(String date) {
        char[] bases = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        // 获取date 转换我char
        List<Character> chars = date.chars().mapToObj(e -> (char)e).collect(Collectors.toList());
        //对date随机
        Collections.shuffle(chars);
        //将所有字符组合成字符串
        String mid6 = chars.stream().map(Object::toString).collect(Collectors.joining());

        // 后八位
        String suffix8 = RandomStringUtils.random(1, bases) + RandomStringUtils.randomAlphanumeric(7);
        return mid6 + suffix8;
    }

}