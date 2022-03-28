package com.cc.distributedLock.redisson.sb.demo.vo.req;

import lombok.Data;

@Data
public class InventoryDeductReq {

    private Integer uid;

    private String productId;
}
