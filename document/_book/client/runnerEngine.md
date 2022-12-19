##执行引擎
###执行方法
- - -
可以在Springboot/Spring体系中任何被Spring上下文管理的类中进行注入FiboApplication。<br>
FiboApplication有两个重载方法
```Java
//默认上下文执行方法
public EngineResponse runner(Long engineId, Object param)
//传入自定义上下文执行方法
public EngineResponse runner(Long engineId, Object param, Class<?>... paramBeanClazzArray)
```
###执行参数
- - -
* engineId：后台配置的引擎id
* param：引擎执行所需参数，节点执行时可以通过getRequestData方法直接获取
```Java
@FiboBean(name = "普通节点", desc = "普通节点")
public class CommonNode extends FiboNode {
    @Override
    public void runnerStep() {
        //获取请求参数，例如传入的是OrderVo
        OrderVo orderVo = this.getRequestData();
    }
}
```
* paramBeanClazzArray：执行上下文

###执行结果
- - -
执行返回结果类型为EngineResponse<br>
* success：执行是否成功
* message：异常信息
* cause：异常
* fiboContext：包含执行上下文、过程参数、执行步骤