## Client接入(非Spring)
### 增加pom依赖
- - -
以1.0.0版本为例
```xml
<dependency>
    <groupId>com.fibo.rule</groupId>
    <artifactId>fiborule-core</artifactId>
    <version>1.0.0</version>
</dependency>
```
### 运行Client
- - -
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