package com.xzh.coupon.serialization;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.xzh.coupon.entity.Coupon;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 优惠券实体类自定义序列化器
 */
public class CouponSerialize extends JsonSerializer<Coupon> {

    @Override
    public void serialize(Coupon coupon, JsonGenerator generator, SerializerProvider serializers) throws IOException {

        // 开始序列化
        generator.writeStartObject();
        generator.writeStringField("id", coupon.getId().toString());
        generator.writeStringField("templateId", coupon.getTemplateId().toString());
        generator.writeStringField("userId", coupon.getUserId().toString());
        generator.writeStringField("couponCode", coupon.getCouponCode());
        generator.writeStringField("assignTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(coupon.getAssignTime()));
        generator.writeStringField("name", coupon.getTemplateSDK().getName());
        generator.writeStringField("logo", coupon.getTemplateSDK().getLogo());
        generator.writeStringField("desc", coupon.getTemplateSDK().getDesc());
        generator.writeStringField("expiration", JSON.toJSONString(coupon.getTemplateSDK().getRule().getExpiration()));
        generator.writeStringField("discount", JSON.toJSONString(coupon.getTemplateSDK().getRule().getDiscount()));
        generator.writeStringField("usage", JSON.toJSONString(coupon.getTemplateSDK().getRule().getUsage()));
        // 结束序列化
        generator.writeEndObject();
    }
}
