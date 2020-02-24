package com.xzh.coupon.controller;

import com.xzh.coupon.exception.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健康检查接口
 */
@Slf4j
@RestController
public class HealthCheckController {

    /** 服务发现客户端 */
    private final DiscoveryClient client;
    /** 服务注册接口, 提供了获取服务 id 的方法
     *  每个注册在eureka server的微服务都会分配一个服务id
     * */
    private final Registration registration;

    @Autowired
    public HealthCheckController(DiscoveryClient client, Registration registration) {
        this.client = client;
        this.registration = registration;
    }

    /**
     * 健康检查接口: 127.0.0.1:9092/template/health
     * @return
     */
    @GetMapping("/health")
    public String health() {
        log.debug("view health api");
        return "CouponTemplate Is OK!";
    }

    /**
     * 异常测试接口
     * @return 127.0.0.1:9092/template/exception
     * @throws CouponException
     */
    @GetMapping("/exception")
    public String exception() throws CouponException {

        log.debug("view exception api");
        throw new CouponException("CouponTemplate Has Some Problem");
    }

    /**
     * 获取 Eureka Server 上的微服务元信息
     * @return 127.0.0.1:9092/template/info
     */
    @GetMapping("/info")
    public List<Map<String, Object>> info() {

        // 大约需要等待两分钟时间才能获取到注册信息
        /**
         * registration.getServiceId()返回当前服务注册早eureka server上的id
         */
        List<ServiceInstance> instances = client.getInstances(registration.getServiceId());

        List<Map<String, Object>> result = new ArrayList<>(instances.size());

        instances.forEach(i -> {
            Map<String, Object> info = new HashMap<>();
            info.put("serviceId", i.getServiceId());//服务id
            info.put("instanceId", i.getInstanceId());//实例id，与服务不同，可能多实例部署
            info.put("port", i.getPort());//当前服务端口号

            result.add(info);
        });

        return result;
    }
}
