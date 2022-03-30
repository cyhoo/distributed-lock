package com.cc.distributedLock.core;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author caiweihao
 * @description: 多key策略
 * @date 2022/3/30 22:23
 */
@AllArgsConstructor
public enum MultiKeyBuildStrategy {

    UNION((keys,unionCharacter) -> {
        return Collections.singletonList(String.join(unionCharacter,keys));
    }),
    SINGLY((keys,unionCharacter) -> {
        return keys;
    })

    ;

    private Builder builder;

    public List<String> build(List<String> keys,String unionCharacter){
        return this.builder.build(keys,unionCharacter);
    }

    interface Builder{
        List<String> build(List<String> keys,String unionCharacter);
    }
}
