package com.cc.distributedLock.redisson;

import com.cc.distributedLock.core.Constant;
import com.cc.distributedLock.core.Locker;
import com.cc.distributedLock.core.LockerProvider;
import org.redisson.api.RLock;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RedissonLockerProvider implements LockerProvider<RedissonLockInfo> {

    private RedissonManager redissonManager;

    public RedissonLockerProvider(RedissonManager redissonManager) {
        this.redissonManager = redissonManager;
    }

    @Override
    public Locker createLocker(RedissonLockInfo info) {
        List<RLock> locks = info.getKeys().stream().map(key -> {
            List<String> prefixSegments = this.getPrefixSegments(info);
            List<String> allSegments = new ArrayList<>();
            allSegments.addAll(prefixSegments);
            allSegments.add(key);
            String fullKey = String.join(getSpiltChart(info), allSegments);
            return this.redissonManager.getClient().getLock(fullKey);
        }).collect(Collectors.toList());
        RLock rLock = null;
        if (locks.size() > 1){
            rLock = this.redissonManager.getClient().getMultiLock(locks.toArray(new RLock[0]));
        }else {
            rLock = locks.get(0);
        }
        Locker locker = new RedissonLocker(rLock);
        return locker;
    }

    private String getSpiltChart(RedissonLockInfo info){
        //决定分隔符
        String spilt = info.getSpiltChart();
        if (!StringUtils.hasText(spilt)){
            spilt = this.redissonManager.getLockProps().getSpiltCharacter();
        }
        if (!StringUtils.hasText(spilt)){
            spilt = Constant.GLOBAL_SPILT_CHARACTER;
        }
        return spilt;
    }

    private List<String> getPrefixSegments(RedissonLockInfo info){
        List<String> prefixSegments = new ArrayList<>();
        if (StringUtils.hasText(this.redissonManager.getLockProps().getPrefix())){
            prefixSegments.add(this.redissonManager.getLockProps().getPrefix());
        }
        if (StringUtils.hasText(info.getPrefix())){
            prefixSegments.add(info.getPrefix());
        }
        return prefixSegments;
    }
}
