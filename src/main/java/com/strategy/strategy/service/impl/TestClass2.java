package com.strategy.strategy.service.impl;


import com.strategy.strategy.annotation.Strategy;
import com.strategy.strategy.annotation.StrategyPoint;
import com.strategy.strategy.entity.StrategyEntity;
import org.springframework.stereotype.Service;

@StrategyPoint
@Service
public class TestClass2 extends TestClass {
    @Strategy("test1")
    public StrategyEntity test1(StrategyEntity entity,boolean b) {
        entity.setNum(123);
        entity.setStr("值变化");
        if (b){
            throw new RuntimeException("测试");
        }
        return entity;
    }
}
