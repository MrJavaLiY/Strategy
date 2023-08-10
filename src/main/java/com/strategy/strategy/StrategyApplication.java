package com.strategy.strategy;

import com.strategy.strategy.mode.StrategyOperate;
import com.strategy.strategy.service.TestStrategy;
import com.strategy.strategy.service.impl.TestClass2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootApplication
public class StrategyApplication {
    @Resource
    TestStrategy testStrategy;
    @Resource
    TestClass2 testClass;

    public static void main(String[] args) {
        SpringApplication.run(StrategyApplication.class, args);
    }

    @Bean
    public StrategyOperate strategyOperate() {
        return new StrategyOperate(testStrategy.getClass(), testClass.getClass());
    }

}
