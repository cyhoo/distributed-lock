package com.cc.distributedLock.core;

public interface LockResultCallback {

    void lockResultHandle(LockInfo lockInfo, boolean lockResult,DistributedLockException lockException);

}
