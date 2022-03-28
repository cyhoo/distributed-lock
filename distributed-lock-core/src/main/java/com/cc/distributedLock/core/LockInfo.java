package com.cc.distributedLock.core;

import lombok.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class LockInfo {
    private String prefix;

    private List<String> keys;

    private String spiltChart;

    private long waitTime;

    private TimeUnit timeUnit;
}
