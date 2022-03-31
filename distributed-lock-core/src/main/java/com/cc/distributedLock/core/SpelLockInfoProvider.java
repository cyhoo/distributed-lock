package com.cc.distributedLock.core;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caiweihao
 * @description: TODO
 * @date 2022/3/31 23:22
 */
public class SpelLockInfoProvider implements LockInfoProvider{

    private static final SpelExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    private EvaluationContextProvider evaluationContextProvider;

    private ParserContext parserContext;

    public SpelLockInfoProvider(EvaluationContextProvider evaluationContextProvider, ParserContext parserContext) {
        this.evaluationContextProvider = evaluationContextProvider;
        this.parserContext = parserContext;
    }

    @Override
    public LockInfo createInfo(JoinPoint joinPoint, DistributedLock distributedLock) {
        List<String> parseCompleteKeys = parseSpelKeys(joinPoint, distributedLock);
        LockInfo lockInfo = doCreateInfo(joinPoint,distributedLock,parseCompleteKeys);
        return lockInfo;
    }

    protected LockInfo doCreateInfo(JoinPoint joinPoint, DistributedLock distributedLock,List<String> parseCompleteKeys){
        LockInfo lockInfo = new LockInfo();
        lockInfo.setKeys(parseCompleteKeys);
        lockInfo.setPrefix(distributedLock.prefix());
        lockInfo.setSpiltChart(distributedLock.spiltChart());
        lockInfo.setWaitTime(distributedLock.waitTime());
        lockInfo.setTimeUnit(distributedLock.timeUnit());
        return lockInfo;
    }

    protected List<String> parseSpelKeys(JoinPoint joinPoint, DistributedLock distributedLock){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        Object[] args = joinPoint.getArgs();

        EvaluationContext context = this.evaluationContextProvider.provide();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i],args[i]);
        }

        List<String> parseCompleteKeys = Arrays.stream(distributedLock.keys()).map(key -> {
            String standardKeyTemplate = String.format(this.parserContext.getExpressionPrefix()+"%s"+this.parserContext.getExpressionSuffix(),key);
            Expression expression = SPEL_EXPRESSION_PARSER.parseExpression(standardKeyTemplate, this.parserContext);
            return expression.getValue(context, String.class);
        }).collect(Collectors.toList());
        return parseCompleteKeys;
    }

    @FunctionalInterface
    public interface EvaluationContextProvider{
        EvaluationContext provide();
    }
}
