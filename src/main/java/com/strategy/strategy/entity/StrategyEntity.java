package com.strategy.strategy.entity;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StrategyEntity {

    private String str;
    public int num;

}
