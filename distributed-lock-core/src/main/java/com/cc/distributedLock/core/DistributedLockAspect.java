package com.cc.distributedLock.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class DistributedLockAspect {

    private LockInfoProvider lockInfoProvider;

    private LockResultCallback lockResultCallback;

    private LockerProvider lockerProvider;

    public DistributedLockAspect(LockInfoProvider lockInfoProvider, LockResultCallback lockResultCallback, LockerProvider lockerProvider) {
        this.lockInfoProvider = lockInfoProvider;
        this.lockResultCallback = lockResultCallback;
        this.lockerProvider = lockerProvider;
    }

    @Pointcut("@annotation(distributedLock)")
    public void pointcut(DistributedLock distributedLock){};

    @Around(value = "pointcut(distributedLock)", argNames = "proceedingJoinPoint,distributedLock")
    public void around(ProceedingJoinPoint proceedingJoinPoint,DistributedLock distributedLock) throws Throwable {
        LockInfo lockInfo = this.lockInfoProvider.createInfo(proceedingJoinPoint, distributedLock);
        Locker locker = lockerProvider.createLocker(lockInfo);
        boolean lockResult = false;
        DistributedLockException lockException = null;
        //处理加锁步骤
        try {
            lockResult = locker.lock(lockInfo);
        }catch (DistributedLockException e){
            lockException = e;
        }finally {
            if (lockResult && lockException != null){
                locker.release(lockInfo);
            }
        }
        //处理加锁结果
        this.lockResultCallback.lockResultHandle(lockInfo,lockResult,lockException);

        //执行业务逻辑
        try {
            proceedingJoinPoint.proceed();
        }finally {
            //加锁成功需要释放锁
            if (lockResult){
                locker.release(lockInfo);
            }
        }
    }
}
