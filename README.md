# Strategy
# 说明 illustrate
这是一种基于**键值模型**以及**java反射技术**实现的策略模式新型实现方式。

This is a new implementation method of strategy pattern based on key value model and Java reflection technology.

# 目的 objective
1: 可优化多**if...else**语句以及**switch**语句,

1: Can optimize multiple **if...Else** statement and **switch** statement.

2: 采用的算法方法而不是算法类，较传统的策略模式来看，不仅实现了策略模式的功能，还简化了超大量策略类。

2: The algorithm method used instead of the algorithm class not only achieves the functionality of the strategy mode, but also simplifies a large number of strategy classes compared to traditional strategy modes.

# 实现技术 implementation technique
1: java annotation  
2: interface  
3: Reflection  
4: map
# 实现逻辑 implementation logic
1: 设计两个注解，一个@StrategyPonit 策略铆钉注解，放置在接口上，表明该接口为受策略管理的接口（目的为了兼容java多实现技术）；一个@Strategy 注解放置在具体的接口算法方法上，表明该方法为策略方法，该注解需要 String[] values，值为判断值，也就是该算法方法的索引，定位该方法，

1: Design two annotations, one @StrategyPonit policy rivet annotation, placed on the interface to indicate that it is a policy managed interface (aimed at compatibility with Java multi implementation technology); A @Strategy annotation is placed on a specific interface algorithm method, indicating that the method is a policy method. The annotation requires String [] values, which are the judgment values, that is, the index of the algorithm method, to locate the method.

2: 交由spring bean管理，构造方法将算法接口的实现类传入，完成算法方法的索引建立，完成建模，

2: Managed by the spring bean, the construction method passes in the implementation class of the algorithm interface, completes the index establishment of the algorithm method, and completes the modeling.

3: 调用执行方法，传入参数和索引，方法内使用索引获取到方法，然后使用反射技术将其传入。

3: Call the execution method, pass in parameters and index, use index to obtain the method within the method, and then use reflection technology to pass it in.




