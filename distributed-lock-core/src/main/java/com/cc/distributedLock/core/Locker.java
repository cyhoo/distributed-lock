package com.cc.distributedLock.core;

public interface Locker {

    boolean lock(LockInfo info) throws Exception;

    boolean release(LockInfo info);
}
