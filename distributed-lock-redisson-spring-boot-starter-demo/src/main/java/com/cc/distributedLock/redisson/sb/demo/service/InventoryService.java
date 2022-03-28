package com.cc.distributedLock.redisson.sb.demo.service;


import com.cc.distributedLock.redisson.sb.demo.vo.req.InventoryDeductReq;

public interface InventoryService {

    Boolean deduct(InventoryDeductReq req);
}
