package com.strategy.strategy.service.impl;

import com.strategy.strategy.annotation.StrategyPoint;
import com.strategy.strategy.service.TestStrategy;
import org.springframework.stereotype.Service;

@Service
public class TestStrategyImpl implements TestStrategy {
    @Override
    public String one(String str) {
        return "这是一："+str;
    }

    @Override
    public String two(String str) {
        return "这是二："+str;

    }

    @Override
    public String three(String str) {
        return "这是三："+str;

    }

    @Override
    public String four(String str) {
        return "这是四："+str;

    }
}
