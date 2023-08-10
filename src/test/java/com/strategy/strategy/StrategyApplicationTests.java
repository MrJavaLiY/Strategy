package com.strategy.strategy;

import com.strategy.strategy.entity.StrategyEntity;
import com.strategy.strategy.mode.StrategyOperate;
import com.strategy.strategy.service.TestStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

@SpringBootTest
class StrategyApplicationTests {
    @Resource
    StrategyOperate strategyOperate;

    @Test
    void contextLoads() throws Exception {
        StrategyEntity entity = new StrategyEntity();
        entity.setStr("值原本").setNum(0);
        System.out.println(strategyOperate.getMap());
        System.out.println(strategyOperate.executeMethod("test1", entity, false));
        System.out.println(strategyOperate.executeMethodSpring("1", "sada"));
        System.out.println(strategyOperate.executeMethodSpring("12", "sada"));
        System.out.println(strategyOperate.executeMethodSpring("3", "sada"));
        System.out.println(strategyOperate.executeMethodSpring("4", "sada"));

    }

}
