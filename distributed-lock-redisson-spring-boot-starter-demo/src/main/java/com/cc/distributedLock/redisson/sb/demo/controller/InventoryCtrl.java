package com.cc.distributedLock.redisson.sb.demo.controller;

import com.cc.distributedLock.redisson.sb.demo.service.InventoryService;
import com.cc.distributedLock.redisson.sb.demo.vo.resp.InventoryDeductResp;
import com.cc.distributedLock.redisson.sb.demo.vo.req.InventoryDeductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryCtrl {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/deduct")
    public Object deduct(@RequestBody InventoryDeductReq req){
        inventoryService.deduct(req);
        Map<String,Object> map = new HashMap();
        map.put("code",0);
//        return map;
        InventoryDeductResp inventoryDeductResp = new InventoryDeductResp();
        inventoryDeductResp.setCount(100);

//        redisTemplate.opsForValue().set("uid",req.getUid()+"");
        return inventoryDeductResp;
    }

    @GetMapping("/test")
    public Object test(){
        Map map = new HashMap();
        map.put("uid","cwh");
        return map;
    }
}
