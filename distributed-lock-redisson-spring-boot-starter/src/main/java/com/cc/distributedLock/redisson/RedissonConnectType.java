package com.cc.distributedLock.redisson;

import lombok.AllArgsConstructor;
import org.redisson.config.Config;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum RedissonConnectType {

    SINGLE("单机", props -> {
        List<String> addressList = spiltFormatAddress(props.getAddress());
        Config config = new Config();
        config.useSingleServer().setAddress(addressList.get(0));
        config.useSingleServer().setUsername(props.getUsername());
        if (StringUtils.hasText(props.getPassword())){
            config.useSingleServer().setPassword(props.getPassword());
        }
        config.useSingleServer().setDatabase(props.getDatabase());
        return config;
    }),
    MASTER_SLAVE("主从", props -> {
        List<String> addressList = spiltFormatAddress(props.getAddress());
        Config config = new Config();
        config.useMasterSlaveServers().setMasterAddress(addressList.get(0));
        addressList.stream().skip(1).forEach(ad->config.useMasterSlaveServers().addSlaveAddress(ad));
        config.useMasterSlaveServers().setUsername(props.getUsername());
        if (StringUtils.hasText(props.getPassword())){
            config.useMasterSlaveServers().setPassword(props.getPassword());
        }
        config.useMasterSlaveServers().setDatabase(props.getDatabase());
        return config;
    }),
    SENTINEL("哨兵", props -> {
        List<String> addressList = spiltFormatAddress(props.getAddress());
        Config config = new Config();
        config.useSentinelServers().setSentinelAddresses(addressList);
        config.useSentinelServers().setUsername(props.getUsername());
        if (StringUtils.hasText(props.getPassword())){
            config.useSentinelServers().setPassword(props.getPassword());
        }
        config.useSentinelServers().setDatabase(props.getDatabase());
        return config;
    }),
    CLUSTER("集群", props -> {
        List<String> addressList = spiltFormatAddress(props.getAddress());
        Config config = new Config();
        config.useClusterServers().setNodeAddresses(addressList);
        config.useClusterServers().setUsername(props.getUsername());
        if (StringUtils.hasText(props.getPassword())){
            config.useClusterServers().setPassword(props.getPassword());
        }
        return config;
    });

    private String desc;
    private ConvertFunc convertFunc;

    public Config convert(RedissonLockProps props){
        return this.convertFunc.convert(props);
    }

    @FunctionalInterface
    interface ConvertFunc{
        Config convert(RedissonLockProps props);
    }

    public static List<String> spiltFormatAddress(String address){
        return Arrays.stream(address.split(",")).map(m-> RedissonConstant.REDIS_CONNECT_PREFIX+m).collect(Collectors.toList());
    }
}
