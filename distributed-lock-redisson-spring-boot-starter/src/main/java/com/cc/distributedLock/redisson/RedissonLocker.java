package com.cc.distributedLock.redisson;

import com.cc.distributedLock.core.LockInfo;
import com.cc.distributedLock.core.Locker;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;

public class RedissonLocker implements Locker {

    private final RLock rLock;

    private boolean lockSuccess;

    public RedissonLocker(RLock rLock) {
        this.rLock = rLock;
    }

    @Override
    public boolean lock(LockInfo info) throws Exception {
        this.lockSuccess = this.rLock.tryLock(info.getWaitTime(),info.getTimeUnit());
        return this.lockSuccess;
    }

    @Override
    public boolean release(LockInfo info) {
        if (this.rLock instanceof RedissonMultiLock){
            if (this.lockSuccess){
                this.rLock.unlock();
            }
        }else if (this.rLock.isLocked() && this.rLock.isHeldByCurrentThread() && this.lockSuccess){
            this.rLock.unlock();
        }
        return true;
    }
}
