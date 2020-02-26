package com.xzh.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 *  Kafka相关的服务接口定义
 */
public interface IKafkaService {

    /**
     * 消费优惠券Kafka消息
     * @param record {@link ConsumerRecord}
     */
    void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record);
}
