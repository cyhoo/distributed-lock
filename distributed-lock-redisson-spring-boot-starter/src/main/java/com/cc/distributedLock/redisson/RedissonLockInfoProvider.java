package com.cc.distributedLock.redisson;

import com.cc.distributedLock.core.DistributedLock;
import com.cc.distributedLock.core.LockInfo;
import com.cc.distributedLock.core.LockInfoProvider;
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

public class RedissonLockInfoProvider implements LockInfoProvider {

    private static final SpelExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public RedissonLockInfo createInfo(JoinPoint joinPoint, DistributedLock distributedLock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        Object[] args = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i],args[i]);
        }

        List<String> formatKeys = Arrays.stream(distributedLock.keys()).map(key -> {
            Expression expression = SPEL_EXPRESSION_PARSER.parseExpression(key, ParserContext.TEMPLATE_EXPRESSION);
            return expression.getValue(context, String.class);
        }).collect(Collectors.toList());

        RedissonLockInfo lockInfo = new RedissonLockInfo();
        lockInfo.setKeys(distributedLock.multiKeyStrategy().build(formatKeys, distributedLock.spiltChart()));
        lockInfo.setPrefix(distributedLock.prefix());
        lockInfo.setSpiltChart(distributedLock.spiltChart());
        lockInfo.setWaitTime(distributedLock.waitTime());
        lockInfo.setTimeUnit(distributedLock.timeUnit());
        return lockInfo;
    }
}
