package com.cc.distributedLock.core;

/**
 * @author caiweihao
 * @description: 加锁失败策略
 * @date 2022/3/30 22:56
 */
public enum LockFailStrategy {

    /**
     * 抛出异常
     */
    THROW_EXCEPTION,
    /**
     * 忽略异常
     */
    IGNORE_EXCEPTION
}
