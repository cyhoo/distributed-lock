package com.cc.distributedLock.core;

import lombok.Data;

@Data
public class DistributedLockException extends RuntimeException{

    private String msg;

    public DistributedLockException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public DistributedLockException(Throwable cause) {
        super(cause);
    }
}
