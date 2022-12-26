##组件开发
###普通节点
- - -
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
- - -
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
- - -
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
- - -
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