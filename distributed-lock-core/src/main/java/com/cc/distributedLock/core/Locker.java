package com.cc.distributedLock.core;

public interface Locker {

    boolean lock(LockInfo info);

    boolean release(LockInfo info);
}
