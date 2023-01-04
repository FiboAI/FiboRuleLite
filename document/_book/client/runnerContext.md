##执行上下文
###默认上下文
- - -
默认的执行上下文：DefaultParamBean。默认上下文的实现就是一个Map。<br>
可以通过DefaultParamBean中的setData方法放入数据，通过getData方法获得数据。
###自定义上下文
- - -
可以使用任意的Bean作为上下文进行传入，对Bean没有任何要求。<br>
自定义的上下文是强类型，更加贴合实际的业务<br>
传入方式：
```Java
//单个上下文
EngineResponse engineResponse = fiboApplication.runner(1L, 引擎参数, CustomBean.class);
//多个上下文
EngineResponse engineResponse = fiboApplication.runner(1L, 引擎参数, CustomBean.class, OrderBean.class);
```
传入之后，会在调用时进行初始化，给上下文分配唯一的实例。在组件之中可以去获得这个上下文实例：
```Java
@FiboBean(name = "计算节点", desc = "计算节点")
public class CommonNode extends FiboNode {
    @Override
    public void runnerStep() {
        //获取context，多个上下文时按类型获取对应的上下文
        CustomBean customBean = this.getContextBean(CustomBean.class);
        OrderBean orderBean = this.getContextBean(OrderBean.class);
        //获取第一个上下文
        CustomBean customBean = this.getFirstContextBean();
    }
}
```