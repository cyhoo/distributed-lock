package com.cc.distributedLock.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributedLock {

    String prefix() default "";

    String spiltChart() default "";

    String[] keys() default {};

    long waitTime() default 30 * 1000L;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
