package com.xzh.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.xzh.coupon.constant.Constant;
import com.xzh.coupon.constant.CouponStatus;
import com.xzh.coupon.dao.CouponDao;
import com.xzh.coupon.entity.Coupon;
import com.xzh.coupon.service.IKafkaService;
import com.xzh.coupon.vo.CouponKafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Kafka 相关的服务接口实现
 * 核心思想: 是将Cache中的Coupon的状态变化同步到DB中
 */
@Slf4j
@Service
public class KafkaServiceImpl implements IKafkaService {

    @Autowired
    private CouponDao couponDao;

    /**
     * 消费优惠券 Kafka 消息
     */
    @Override
    @KafkaListener(topics = {Constant.TOPIC}, groupId = "coupon-1")
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {
        //获取kafkaMessage
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        //如果消息存在
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            CouponKafkaMessage couponInfo = JSON.parseObject(message.toString(), CouponKafkaMessage.class);
            log.info("Receive CouponKafkaMessage: {}", message.toString());

            CouponStatus status = CouponStatus.of(couponInfo.getStatus());
            switch (status) {
                case USABLE:
                    break;
                case USED:
                    processUsedCoupons(couponInfo, status);
                    break;
                case EXPIRED:
                    processExpiredCoupons(couponInfo, status);
                    break;
            }

        }
    }

    /**
     * 处理已使用的用户优惠券
     * @param message
     * @param status
     */
    private void processUsedCoupons(CouponKafkaMessage message, CouponStatus status) {
        // TODO 给用户发送短信
        processCouponsByStatus(message, status);
    }

    /**
     * 处理过期的用户优惠券
     * @param kafkaMessage
     * @param status
     */
    private void processExpiredCoupons(CouponKafkaMessage kafkaMessage, CouponStatus status) {
        // TODO 给用户发送推送
        processCouponsByStatus(kafkaMessage, status);
    }

    /**
     * 根据状态处理优惠券信息
     * @param message
     * @param status
     */
    private void processCouponsByStatus(CouponKafkaMessage message, CouponStatus status) {
        //找到优惠券
        List<Coupon> coupons = couponDao.findAllById(message.getIds());
        //校验
        if (CollectionUtils.isEmpty(coupons) || coupons.size() != message.getIds().size()) {
            log.error("Can Not Find Right Coupon Info: {}", JSON.toJSONString(message));
            // TODO 发送邮件(或短信)
            return;
        }
        coupons.forEach(coupon -> coupon.setStatus(status));
        log.info("CouponKafkaMessage Op Coupon Count: {}", couponDao.saveAll(coupons).size());
    }
}
