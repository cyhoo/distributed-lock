package com.cc.distributedLock.core;

public interface LockerProvider<I extends LockInfo> {

    Locker createLocker(I info);
}
