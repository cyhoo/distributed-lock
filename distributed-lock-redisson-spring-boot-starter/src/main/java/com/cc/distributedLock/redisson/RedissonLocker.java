package com.cc.distributedLock.redisson;

import com.cc.distributedLock.core.DistributedLockException;
import com.cc.distributedLock.core.LockInfo;
import com.cc.distributedLock.core.Locker;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;

public class RedissonLocker implements Locker {

    private RLock rLock;

    public RedissonLocker(RLock rLock) {
        this.rLock = rLock;
    }

    @Override
    public boolean lock(LockInfo info) {
        boolean result = false;
        try {
            result = this.rLock.tryLock(info.getWaitTime(),info.getTimeUnit());
        }catch (Exception e){
            throw new DistributedLockException(e);
        }
        return result;
    }

    @Override
    public boolean release(LockInfo info) {
        if (this.rLock instanceof RedissonMultiLock){
            this.rLock.unlock();
        }else if (this.rLock.isLocked()){
            this.rLock.unlock();
        }
        return true;
    }
}
