##Client接入(Spring)
###增加pom依赖
- - -
以1.0.0版本为例
```xml
<dependency>
    <groupId>com.fibo.rule</groupId>
    <artifactId>fiborule-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
###增加配置
- - -
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