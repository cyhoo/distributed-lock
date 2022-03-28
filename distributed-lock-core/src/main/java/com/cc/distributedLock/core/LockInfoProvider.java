package com.cc.distributedLock.core;

import org.aspectj.lang.JoinPoint;

public interface LockInfoProvider {

    LockInfo createInfo(JoinPoint joinPoint,DistributedLock distributedLock);
}
