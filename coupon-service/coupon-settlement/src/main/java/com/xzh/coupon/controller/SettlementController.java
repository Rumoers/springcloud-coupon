package com.xzh.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.xzh.coupon.exception.CouponException;
import com.xzh.coupon.executor.ExecuteManager;
import com.xzh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 结算服务 Controller
 * @author YZH
 */
@Slf4j
@RestController
public class SettlementController {

    /** 结算规则执行管理器 */
    private final ExecuteManager executeManager;

    @Autowired
    public SettlementController(ExecuteManager executeManager) {
        this.executeManager = executeManager;
    }

    /**
     * 优惠券结算
     * 127.0.0.1:9094/settlement/settlement/compute
     * 127.0.0.1:9091/coupon/settlement/settlement/compute
     * @param settlement
     * @return
     * @throws CouponException
     */
    @PostMapping("/settlement/compute")
    public SettlementInfo computeRule(@RequestBody SettlementInfo settlement)
            throws CouponException {

        log.info("settlement: {}", JSON.toJSONString(settlement));
        return executeManager.computeRule(settlement);
    }
}
