package com.cc.distributedLock.redisson;

import lombok.Data;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "distributed-lock.redisson")
public class RedissonLockProps {

    private RedissonConnectType connectType;

    private String address;

    private String username;

    private String password;

    private int database;

    public Config convert(){
        return this.connectType.convert(this);
    }
}
