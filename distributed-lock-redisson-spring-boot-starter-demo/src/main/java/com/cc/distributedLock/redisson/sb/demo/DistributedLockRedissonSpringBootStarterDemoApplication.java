package com.cc.distributedLock.redisson.sb.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {
        "com.cc.distributedLock.redisson",
        "com.cc.distributedLock.core",
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DistributedLockRedissonSpringBootStarterDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedLockRedissonSpringBootStarterDemoApplication.class, args);
    }

}
