package com.cc.distributedLock.redisson.sb.demo.service;

import com.cc.distributedLock.core.DistributedLock;
import com.cc.distributedLock.redisson.sb.demo.vo.req.InventoryDeductReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {


    @Override
    @DistributedLock(prefix = "inventory",spiltChart = "-",keys = {"uid=#{#req.uid}","productId=#{#req.productId}"},waitTime = 3000L)
    public Boolean deduct(InventoryDeductReq req) {
        log.info("deduct 开始:{}",req);
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("扣减成功，线程id:{}",Thread.currentThread().getId());
        return true;
    }
}
