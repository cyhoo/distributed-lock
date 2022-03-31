package com.cc.distributedLock.redisson;

import com.cc.distributedLock.core.DistributedLock;
import com.cc.distributedLock.core.LockInfo;
import com.cc.distributedLock.core.LockInfoProvider;
import com.cc.distributedLock.core.SpelLockInfoProvider;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RedissonLockInfoProvider extends SpelLockInfoProvider {

    public RedissonLockInfoProvider() {
        super(StandardEvaluationContext::new,ParserContext.TEMPLATE_EXPRESSION);
    }

    public RedissonLockInfoProvider(SpelLockInfoProvider.EvaluationContextProvider evaluationContextProvider, ParserContext parserContext) {
        super(evaluationContextProvider, parserContext);
    }

    @Override
    protected LockInfo doCreateInfo(JoinPoint joinPoint, DistributedLock distributedLock, List<String> parseCompleteKeys) {
        RedissonLockInfo lockInfo = new RedissonLockInfo();
        lockInfo.setKeys(distributedLock.multiKeyStrategy().build(parseCompleteKeys, distributedLock.spiltChart()));
        lockInfo.setPrefix(distributedLock.prefix());
        lockInfo.setSpiltChart(distributedLock.spiltChart());
        lockInfo.setWaitTime(distributedLock.waitTime());
        lockInfo.setTimeUnit(distributedLock.timeUnit());
        return lockInfo;
    }
}
