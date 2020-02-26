package com.xzh.coupon.service;

import com.alibaba.fastjson.JSON;
import com.xzh.coupon.constant.CouponStatus;
import com.xzh.coupon.exception.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 用户服务功能测试用例
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    //    fake userId
    private Long fakeUserId = 200L;

    @Autowired
    private IUserService userService;

    @Test
    public void testFindCouponByStatus() throws CouponException {
        log.info("Find Coupon By Status Result: {}", JSON.toJSONString(
                userService.findCouponsByStatus(fakeUserId,
                        CouponStatus.USABLE.getCode())
        ));
    }

    @Test
    public void testFindAvailableTemplate() throws CouponException {
        log.info("Find Available Template R esult:{}", userService.findAvailableTemplate(
                fakeUserId
        ));
    }

}