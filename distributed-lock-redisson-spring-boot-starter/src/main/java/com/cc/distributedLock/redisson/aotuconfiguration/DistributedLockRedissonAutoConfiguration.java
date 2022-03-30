package com.cc.distributedLock.redisson.aotuconfiguration;

import com.cc.distributedLock.core.*;
import com.cc.distributedLock.redisson.RedissonLockInfoProvider;
import com.cc.distributedLock.redisson.RedissonLockProps;
import com.cc.distributedLock.redisson.RedissonLockerProvider;
import com.cc.distributedLock.redisson.RedissonManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "distributed-lock.redisson",name = "enable",havingValue = "true", matchIfMissing = false)
@ConditionalOnBean(value = {DistributedLockProps.class,RedissonLockProps.class})
public class DistributedLockRedissonAutoConfiguration {

    @Bean("redissonManager")
    @ConditionalOnMissingBean(RedissonManager.class)
    public RedissonManager redissonManager(DistributedLockProps distributedLockProps, RedissonLockProps redissonLockProps){
        RedissonManager redissonManager = new RedissonManager(distributedLockProps,redissonLockProps);
        return redissonManager;
    }

    @Bean("lockInfoProvider")
    @ConditionalOnMissingBean(LockInfoProvider.class)
    public LockInfoProvider lockInfoProvider(){
        return new RedissonLockInfoProvider();
    }

    @Bean("redissonLockerProvider")
    @ConditionalOnBean(RedissonManager.class)
    @ConditionalOnMissingBean(RedissonLockerProvider.class)
    public RedissonLockerProvider redissonLockerProvider(RedissonManager redissonManager){
        RedissonLockerProvider redissonLockerProvider = new RedissonLockerProvider(redissonManager);
        return redissonLockerProvider;
    }

    @Bean("distributedLockAspect")
    @ConditionalOnBean(value = {LockInfoProvider.class,RedissonLockerProvider.class})
    public DistributedLockAspect distributedLockAspect(LockInfoProvider lockInfoProvider, RedissonLockerProvider redissonLockerProvider){
        DistributedLockAspect distributedLockAspect = new DistributedLockAspect(lockInfoProvider,redissonLockerProvider);
        return distributedLockAspect;
    }
}
