package com.cc.distributedLock.core;

import lombok.Data;

@Data
public class DistributedLockFailException extends DistributedLockException{

    private String msg;

    public DistributedLockFailException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public DistributedLockFailException(Throwable cause) {
        super(cause);
    }
}
