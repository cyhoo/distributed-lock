package com.cc.distributedLock.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributedLock {

    /**
     * 前缀
     * @return
     */
    String prefix() default "";

    /**
     * 分隔符
     * @return
     */
    String spiltChart() default "";

    /**
     * key数组
     * @return
     */
    String[] keys() default {};

    /**
     * 多key构建策略
     * @return
     */
    MultiKeyBuildStrategy multiKeyStrategy() default MultiKeyBuildStrategy.UNION;

    /**
     * 等待超时时间，默认30秒
     * @return
     */
    long waitTime() default 30 * 1000L;

    /**
     * 时间单位，默认毫秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
