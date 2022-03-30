package com.cc.distributedLock.core;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class DistributedLockAspect {

    private LockInfoProvider lockInfoProvider;

    private LockerProvider lockerProvider;

    public DistributedLockAspect(LockInfoProvider lockInfoProvider, LockerProvider lockerProvider) {
        this.lockInfoProvider = lockInfoProvider;
        this.lockerProvider = lockerProvider;
    }

    @Pointcut("@annotation(distributedLock)")
    public void pointcut(DistributedLock distributedLock){};

    @Around(value = "pointcut(distributedLock)", argNames = "proceedingJoinPoint,distributedLock")
    public void around(ProceedingJoinPoint proceedingJoinPoint,DistributedLock distributedLock) throws Throwable {
        LockInfo lockInfo = this.lockInfoProvider.createInfo(proceedingJoinPoint, distributedLock);
        Locker locker = lockerProvider.createLocker(lockInfo);

        //加锁
        boolean lockResult = locker.lock(lockInfo);
        if (!lockResult){
            log.info("分布式锁加锁失败,lockInfo:{}",lockInfo);
            if (distributedLock.lockFailStrategy() == LockFailStrategy.THROW_EXCEPTION){
                throw new DistributedLockFailException("分布式锁加锁失败");
            }
        }
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
