package com.strategy.strategy.service.impl;

import com.strategy.strategy.annotation.Strategy;
import com.strategy.strategy.annotation.StrategyPoint;
import org.springframework.stereotype.Service;

@StrategyPoint()
//@Service
public class TestClass {
    @Strategy("test1")
    public String test1(String str) {
        return "这是类测试：" + str;
    }
}
