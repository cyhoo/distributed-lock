package com.cc.distributedLock.core;

public class Manager<P> {

    private DistributedLockProps lockProps;

    private P sourceProps;

    public Manager(DistributedLockProps lockProps, P sourceProps) {
        this.lockProps = lockProps;
        this.sourceProps = sourceProps;
    }

    public DistributedLockProps getLockProps() {
        return lockProps;
    }

    public P getSourceProps() {
        return sourceProps;
    }
}
