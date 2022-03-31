package com.cc.demo;

import com.cc.distributedLock.redisson.sb.demo.DistributedLockRedissonSpringBootStarterDemoApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest(classes = DistributedLockRedissonSpringBootStarterDemoApplication.class)
class DemoApplicationTests {


    private static final SpelExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();

    @Test
    void contextLoads() {
        Car car1 = new Car("宝马");
        Car car2 = new Car("奥迪");
        List<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        User user = new User("赵四",18,cars);

        String key = "#user.name+'在'+#user.age+'岁就拥有'+#user.cars[0].name";
//        String key = "#{#user.name}在#{#user.age}岁就拥有#{#user.cars[0].name}";

        Expression expression = SPEL_EXPRESSION_PARSER.parseExpression(String.format("#{%s}",key), ParserContext.TEMPLATE_EXPRESSION);
//        Expression expression = SPEL_EXPRESSION_PARSER.parseExpression(key, ParserContext.TEMPLATE_EXPRESSION);

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user",user);
        String value = expression.getValue(context, String.class);
        log.info("spel value:{}",value);
    }

    @Data
    @AllArgsConstructor
    static class User{
        String name;
        Integer age;
        List<Car> cars;
    }

    @Data
    @AllArgsConstructor
    static class Car{
        String name;
    }
}
