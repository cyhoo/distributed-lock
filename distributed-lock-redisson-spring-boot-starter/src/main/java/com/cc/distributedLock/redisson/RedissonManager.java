package com.cc.distributedLock.redisson;

import com.cc.distributedLock.core.DistributedLockProps;
import com.cc.distributedLock.core.Manager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonManager extends Manager<RedissonLockProps> {

    private Config config;

    private RedissonClient redissonClient;

    public RedissonManager(DistributedLockProps lockProps, RedissonLockProps modeProps) {
        super(lockProps, modeProps);
        this.config = modeProps.convert();
        this.redissonClient = Redisson.create(this.config);
    }

    public RedissonClient getClient(){
        return this.redissonClient;
    }
}
