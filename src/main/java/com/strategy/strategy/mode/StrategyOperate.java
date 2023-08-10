package com.strategy.strategy.mode;

import com.strategy.strategy.annotation.Strategy;
import com.strategy.strategy.annotation.StrategyPoint;
import com.strategy.strategy.entity.StrategyEntity;
import com.strategy.strategy.exception.StrategyExecuteMethodException;
import com.strategy.strategy.exception.StrategyOperateInitException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author liyang
 * @date 2023-07-19
 * @description: 该类为<基于注解的方法策略模式>的核心思想，new时将类对象放入，然后获取类对象的实现接口，
 * 接口上的 StrategyPoint 注解为接口管理注解，有这个注解的接口才可以被策略管理，
 * 在每一个策略方法上面加上 Strategy 注解，并且为该注解填值，完成对方法的索引添加
 * ，本类的构造方法会进行检测注解并管理策略
 */

public class StrategyOperate {
    private final Map<String, Class<?>> classMap = new HashMap<>();
    private final Map<String, Method> map = new HashMap<>();
    private Class<?> defaultClass = null;
    private Method defaultMethod = null;


    /**
     * 构造方法，将需要策略管理的类对象传入，然后会检测他的实现接口上的注解,
     * 没有实现接口就检测是否有父类，没有父类那就是本类
     *
     * @param clazzes 需要被方法策略模式监管的方法载体类，需要在类上安置注解@StrategyPoint
     */
    public StrategyOperate(Class<?>... clazzes) {
        for (Class<?> aClass : clazzes) {
            Class<?>[] c = aClass.getInterfaces();
            //证明这个类没有实现任何的接口，将进行本类判断
            if (c.length == 0) {
                if (aClass.getAnnotation(StrategyPoint.class) == null) {
                    //证明本类没有进行策略注册
                    throw new StrategyOperateInitException("No StrategyPoint annotation on class:" + aClass);
                }
                this.extracted(aClass, aClass);
            } else {
                for (Class<?> interfaceClazz : c) {
                    if (interfaceClazz.getAnnotation(StrategyPoint.class) == null) {
                        continue;
                    }
                    this.extracted(aClass, interfaceClazz);
                }
            }
        }
        if (this.map.size() == 0 || this.classMap.size() == 0) {
            //说明传入的类的内部没有被策略监管的方法
            throw new StrategyOperateInitException("No method or class managed by policy");
        }
    }


    /**
     * 获取类中的方法，并且托管带有注解@Strategy的被策略监管的方法
     *
     * @param instanceClass 实现类，算法类，实际业务类，这个类理论上是交给了spring管理的
     * @param pointClass    需要监管的类，可以是接口，父类等等实现了@StrategyPoint注解的类
     */
    private void extracted(Class<?> instanceClass, Class<?> pointClass) {
        Method[] methods = pointClass.getDeclaredMethods();
        //获取当前类中所有的方法，然后进行策略监管判断,只有实现@Strategy了的方法才会被纳入管理
        for (Method method : methods) {
            Strategy annotation = method.getAnnotation(Strategy.class);
            if (annotation == null) {
                continue;
            }
            if (annotation.isDefault()) {
                this.defaultClass = instanceClass;
                this.defaultMethod = method;
            }
            if (annotation.values().length != 0) {
                for (int i = 0; i < annotation.values().length; i++) {
                    this.extracted(instanceClass, method, annotation.values()[i]);
                }
            } else {
                if (StringUtils.hasText(annotation.value())) {
                    this.extracted(instanceClass, method, annotation.value());
                }
            }

        }
    }

    /**
     * 将类和方法存下来，形成键值对
     *
     * @param instanceClass 实现类
     * @param method        方法
     * @param index         索引
     */
    private void extracted(Class<?> instanceClass, Method method, String index) {
        this.classMap.put(index, instanceClass);
        this.map.put(index, method);
    }

    /**
     * 依赖于spring的执行方法，类里面如果使用的spring管理的依赖类，就得使用这个方法调用，实现类就必须从spring中获取，否则会报错
     *
     * @param index      索引
     * @param parameters 参数
     * @return
     * @throws Exception
     */
    public Object executeMethodSpring(String index, Object... parameters) throws Exception {
        return this.executeMethod(true, index, parameters);
    }

    /**
     * 普通方式的调用，类的实现都重新创建
     *
     * @param index
     * @param parameters
     * @return
     * @throws Exception
     */
    public Object executeMethod(String index, Object... parameters) throws Exception {
        return this.executeMethod(false, index, parameters);
    }

    /**
     * 执行方法，传入索引获取策略方法，然后使用反射将值植入进去
     *
     * @param index      监管策略方法的索引，@Strategy的值
     * @param parameters 该方法的入参
     * @return 该 index 索引的策略方法的返回值
     * @throws IllegalAccessException    反射异常
     * @throws InstantiationException    反射异常
     * @throws InvocationTargetException 反射异常
     */
    public Object executeMethod(boolean isSpring, String index, Object... parameters)
            throws Exception {
        Class<?> clazz = this.classMap.get(index);
        Method method = this.map.get(index);
        if (clazz == null || method == null) {
//            throw new StrategyExecuteMethodException("No class/method to this index:" + index);
            clazz = this.defaultClass;
            method = this.defaultMethod;
        }
        Object obj = null;
        if (isSpring) {
            obj = ApplicationBean.getBean(clazz);// 交给spring管理
        } else {
            obj = clazz.newInstance();
        }
        paramTypeEq(clazz, method, parameters);
        try {
            return method.invoke(obj, parameters);
        } catch (InvocationTargetException e) {
            throw (Exception) e.getTargetException();
        }
    }

    private void paramTypeEq(Class<?> clazz, Method method, Object[] parameters) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != parameters.length) {
            throw new StrategyExecuteMethodException(" method " + clazz.getName() + "." + method.getName() + " parameter quantity is "
                    + parameterTypes.length + " but input parameter quantity is " + parameters.length);
        }
    }


    public Map<String, Class<?>> getClassMap() {
        return classMap;
    }

    public Map<String, Method> getMap() {
        return map;
    }

}
