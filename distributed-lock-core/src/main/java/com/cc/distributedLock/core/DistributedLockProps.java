package com.cc.distributedLock.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "distributed-lock")
public class DistributedLockProps {

    private String prefix;

    private String spiltCharacter;
}
