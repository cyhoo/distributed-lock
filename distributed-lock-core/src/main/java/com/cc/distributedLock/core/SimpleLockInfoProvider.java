package com.cc.distributedLock.core;

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

public class SimpleLockInfoProvider implements LockInfoProvider{

    private static final SpelExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public LockInfo createInfo(JoinPoint joinPoint, DistributedLock distributedLock) {
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

        LockInfo lockInfo = new LockInfo();
        lockInfo.setKeys(formatKeys);
        lockInfo.setPrefix(distributedLock.prefix());
        lockInfo.setSpiltChart(distributedLock.spiltChart());
        lockInfo.setWaitTime(distributedLock.waitTime());
        lockInfo.setTimeUnit(distributedLock.timeUnit());
        return lockInfo;
    }

    public static void main(String[] args) {
        Car ft = new Car();
        ft.setName("mengdiou");
        Car mzd = new Car();
        mzd.setName("aksl");
        List<Car> cars= new ArrayList<>();
        cars.add(ft);
        cars.add(mzd);
        User cwh = new User();
        cwh.setAge(28);
        cwh.setCars(cars);
        cwh.setName("蔡伟浩");
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("cwh",cwh);
        ExpressionParser expressionParser = new SpelExpressionParser();
//        System.out.println(expressionParser.parseExpression("key:value:xxx",
//                        ParserContext.TEMPLATE_EXPRESSION)
//                .getValue(context,String.class));
//        System.out.println(expressionParser.parseExpression("#{#cwh.name}在#{#cwh.age}岁拥有#{#cwh.cars[0].name}",
//                        ParserContext.TEMPLATE_EXPRESSION)
//                .getValue(context,String.class));
        System.out.println(expressionParser.parseExpression("#{#cwh.name}在#{#cwh.age}岁拥有#{#cwh.cars[0].name}",
                        ParserContext.TEMPLATE_EXPRESSION)
                .getValue(context,String.class));
//        System.out.println(expressionParser.parseExpression("",
//                ParserContext.TEMPLATE_EXPRESSION)
//                .getValue(context,String.class));
    }

    @Data
    public static class User{
        private List<Car> cars;
        private Integer age;
        private String name;
    }
    @Data
    public static class Car{
        private String name;
    }
}
