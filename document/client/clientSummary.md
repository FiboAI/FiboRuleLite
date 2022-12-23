#开发指南
##Client接入(Spring)
###增加pom依赖
以1.0.0版本为例
```xml
<dependency>
    <groupId>com.fibo.rule</groupId>
    <artifactId>fiborule-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
###增加配置
```Java
//后台配置的appId
fiborule.app=1
//server地址：ip和端口
fiborule.server=127.0.0.1:18121

//场景配置：可以配置多个场景；没有配置的话，默认场景名称为default，扫描全包
//场景名称
fiborule.scene-list[0].name=mall-price
//场景对应的包，可以配置多个包，逗号分隔
fiborule.scene-list[0].path=com.fibo.rule.test.mall
```
##Client接入(非Spring)
###增加pom依赖
以1.0.0版本为例
```xml
<dependency>
    <groupId>com.fibo.rule</groupId>
    <artifactId>fiborule-core</artifactId>
    <version>1.0.0</version>
</dependency>
```
###运行Client
```Java
//场景对应的包路径
Map<String, Set<String>> scenePackages = new HashMap<>();
//传入app、server和场景对应的包路径
FiboNioClient fiboNioClient = new FiboNioClient(1, "127.0.0.1:18121", scenePackages);
//启动客户端应用，连接server
fiboNioClient.start();
//客户端应用关闭后清理
fiboNioClient.destroy();
```
##节点开发
###普通节点
继承FiboNode类，实现runnerStep方法
```Java
@FiboBean(name = "普通节点", desc = "普通节点")
public class CommonNode extends FiboNode {
    @Override
    public void runnerStep() {
        //计算逻辑
    }
}
```
###If节点
继承FiboIfNode类，实现runnerStepIf方法，返回true/false
```Java
@FiboBean(name = "If节点", desc = "If节点")
public class JudgeNode extends FiboIfNode {
    @Override
    public boolean runnerStepIf() {
        //判断逻辑返回true/false
        return false;
    }
}
```
###Switch节点
继承FiboSwitchNode类；实现runnerStepSwitch方法，返回分支的值；实现switchBranchs方法，返回所有分支的值和对应名称
```Java
@FiboBean(name = "Switch节点", desc = "Switch节点")
public class SwitchNode extends FiboSwitchNode {
    @Override
    public String runnerStepSwitch() {
        //判断逻辑，返回分支结果
        return "A";
    }
    @Override
    public Map<String, String> switchBranchs() {
        //key：分支的值，与runnerStepSwitch方法的返回值对应；
        //value：分支的名称，用于后台引擎配置时switch分支名称显示；
        Map<String, String> qmap = new HashMap<>();
        qmap.put("A", "A");
        qmap.put("B", "B");
        qmap.put("C", "C");
        return qmap;
    }
}
```
###注解
####节点注解
使用@FiboBean注解，设置节点的名称（name）和描述（desc），用于后台引擎配置时选择节点。
####节点属性注解
使用@FiboField注解，设置节点属性的名称（name）、描述（desc）、类型（type），用于后台配置时显示。<br>
类型支持数值型、字符型、时间型，未设置时默认按类型进行默认。<br>
默认支持类型有：<br>
* 字符型:String
* 时间型:java.util.Date
* 数值型:BigDecimal, Integer, int, Long, long,
Double, double, Float, float
```Java
@Data
@FiboBean(name = "打折券计算", desc = "打折券计算")
public class FullDisCountNode extends FiboNode {

    @FiboField(name = "满足金额", desc = "满足金额", type = FieldTypeEnum.NUMBER)
    private BigDecimal fullValue;
    @FiboField(name = "折扣", desc = "折扣", type = FieldTypeEnum.NUMBER)
    private BigDecimal discountValue;

    @Override
    public void runnerStep() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);
        BigDecimal originalPrice = priceContext.getOriginalPrice();
        //不满足条件
        if(originalPrice.compareTo(fullValue) < 0) {
            return;
        }
        BigDecimal subtractValue = originalPrice.subtract(originalPrice.multiply(discountValue));
        List<DiscountVo> discountList = priceContext.getDiscountList();
        //添加打折记录
        discountList.add(new DiscountVo(AmountTypeEnum.FULL_DISCOUNT,
                BigDecimal.ZERO.subtract(subtractValue),
                StrUtil.format( "{}(满{}打{}折)", AmountTypeEnum.FULL_DISCOUNT.getName(), fullValue, discountValue.multiply(new BigDecimal(10)))));
    }
}
```
##执行上下文
###默认上下文
默认的执行上下文：DefaultParamBean。默认上下文的实现就是一个Map。<br>
可以通过DefaultParamBean中的setData方法放入数据，通过getData方法获得数据。
###自定义上下文
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
@FiboBean(name = "普通节点", desc = "普通节点")
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
##执行引擎
###执行方法
可以在Springboot/Spring体系中任何被Spring上下文管理的类中进行注入FiboApplication。<br>
FiboApplication有两个重载方法
```Java
//默认上下文执行方法
public EngineResponse runner(Long engineId, Object param)
//传入自定义上下文执行方法
public EngineResponse runner(Long engineId, Object param, Class<?>... paramBeanClazzArray)
```
###执行参数
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
执行返回结果类型为EngineResponse<br>
属性：
* success：执行是否成功
* message：异常信息
* cause：异常
* fiboContext：包含执行上下文、过程参数、执行步骤
