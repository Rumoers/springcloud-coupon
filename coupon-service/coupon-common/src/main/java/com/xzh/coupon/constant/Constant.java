package com.xzh.coupon.constant;

/**
 * 通用常用常量定义
 */
public class Constant {

    /** Kafka 消息的 Topic */
    public static final String TOPIC = "xzh_user_coupon_op";

    /**
     * Redis Key 前缀定义
     * */
    public static class RedisPrefix {

        /** 优惠券码 key 前缀 */
        public static final String COUPON_TEMPLATE = "xzh_coupon_template_code_";

        /** 用户当前所有可用的优惠券 key 前缀 */
        public static final String USER_COUPON_USABLE = "xzh_user_coupon_usable_";

        /** 用户当前所有已使用的优惠券 key 前缀 */
        public static final String USER_COUPON_USED = "xzh_user_coupon_used_";

        /** 用户当前所有已过期的优惠券 key 前缀 */
        public static final String USER_COUPON_EXPIRED = "xzh_user_coupon_expired_";
    }
}
