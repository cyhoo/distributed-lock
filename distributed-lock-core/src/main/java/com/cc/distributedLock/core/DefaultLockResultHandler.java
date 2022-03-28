package com.cc.distributedLock.core;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultLockResultHandler implements LockResultCallback {
    public DefaultLockResultHandler() {
    }

    @Override
    public void lockResultHandle(LockInfo lockInfo, boolean lockResult,DistributedLockException lockException) {
        log.info("distributedLock lockResult:{}",lockResult);
        if (!lockResult){
            throw new DistributedLockException("distributedLock lock fail");
        }
    }
}
