package com.strategy.strategy.service;

import com.strategy.strategy.annotation.Strategy;
import com.strategy.strategy.annotation.StrategyPoint;
@StrategyPoint
public interface TestStrategy {
    @Strategy(values = {"1","12"})
    String one(String str);

    @Strategy(value = "2")
    String two(String str);

    @Strategy(value = "3")
    String three(String str);

    @Strategy(value = "4")
    String four(String str);
}
