package com.strategy.strategy.annotation;

import java.lang.annotation.*;

/**
 * 策略监管方法的核心注解，带有该注解的方法才可以被策略监管
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Strategy {
    /**
     * 监管方法的索引，必填
     *
     * @return
     */
    String value() default "";

    String[] values() default {};
    boolean isDefault() default false;
}
