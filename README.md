# spring cloud sample
该工程代码为spring cloud 相关组件的组合工程代码，为微服务一站式解决方案的调研示例代码。

包括 spring cloud eureka，spring cloud zuul，spring cloud config，spring cloud hystrix(dashboard)，spring cloud turbine(dashboard)，spring boot admin，还有两个springboot 示例工程，分别为 service-pri，service-abis

## 功能
本工程实例致力于想使用spring cloud框架来解决微服务存在问题时，面临如此多的组件，以及对应
版本，无需自己手动搭建示例工程。只需clone该工程，直接修改、新增对应服务即可

## 版本介绍
该项目所选用的spring boot版本为2.1.6.RELEASE，spring cloud版本为Greenwich.SR2，JDK1.8。
详情参见工程根目录的pom.xml文件

## 快速开始
要使用该工程，只需要以下步骤。

### clone本工程
```shell script
$ git clone xxx.spring-cloud-sample.git
```

### 验证注册中心eureka
spring cloud项目中的服务，都是注册在注册中心来进行统一管理的

#### 1 启动eureka
在idea中启动eureka对应的模块cloud-eureka-server

启动完成后，访问eureka对应的服务页面查看
http://localhost/8761

可以看到eureka 对应的页面中，当前注册的只有自己

#### 2 启动服务模块service-pri,service-abis
同理，将工程中的service-pri,service-abis也启动

此时再看eureka服务页面，可以发现service-pri,service-abis服务也注册到了eureka

然后我们访问service-pri,service-abis服务提供的url，看是否能正常访问

service-pri: http://localhost:10070/pri

service-abis: http://localhost:10060/abis

### 验证网关zuul
网管zuul负责对前端请求进行集中管理，也就是前端请求通过网管转发到后端服务；

#### 1 启动zuul模块
在idea中启动zuul对应的模块cloud-zuul

#### 2 验证zuul转发
zuul提供默认的路由规则为：在请求url中加上eureka中注册的服务名。
例如，此时访问上述service-pri,service-abis两个服务

service-pri: http://localhost:10050/service-pri-server/pri

service-abis: http://localhost:10050/service-abis-server/abis

可以看到，返回结果与直接访问两个服务是一样的，这就是网关zuul的作用。

### 验证ribbon负载均衡
到这步时我们的应用service-pri,service-abis都只是起了一个，此时，我们修改应用
端口，再起一个应用。以service-abis为例：
1.修改service-abis中resources下的application.yaml文件，将端口号改为
```yaml
server.port: 10061
```
2.在idea中启动该应用。然后通过网管zuul（或直接访问service-abis）提供的url，

http://localhost:10050/service-abis-server/abis

可以看到其返回结果中的端口号是轮训10060,10061变化的，说明负载均衡生效

3.若想要修改应用的负载均衡策略，可以在cloud-zuul下的conf/RibbonConfiguration.java
中进行修改，内有注释说明

### 验证hystrix熔断保护
hystrix为服务提供熔断保护策略，zuul，openfeign中都直接集成了hystrix

#### 熔断示例
1.打开service-pri,service-abis任意一个服务

2.通过http访问服务提供的超时url，以service-abis为例，浏览器访问

http://localhost:10060/timeout

由于该url对应的方法会sleep 5秒，超过配置中hystrix的配置(4秒)，所以首次访问会在
超时后返回回退方法对应的内容：

```text
该次调用触发了hystrix方法保护机制
```
然后在接下来的5秒钟内，再次访问上述url会直接短路请求，返回上述回退方法的内容。

#### hystrix dashboard 查看单个应用

hystrix dashboard的使用需要在linux环境下。示例中以 host-application 为例。

1.将cloud-eureka-server，cloud-hystrix-dashboard，service-abis-server打成jar包部署到linux环境中

2.启动上述3个服务，浏览器访问

http://host-application:10030/hystrix

可以看到Hystrix-Dashboard的主界面，主界面上输入

http://host-application:10060/actuator/hystrix.stream ， 

Title输入随意（如abis），然后点击 `Monitor Stream`按钮

可以跳转Hystrix-Dashboard显示页面，显示为loading状态，此时需要访问一下service-abis-server
提供的url服务，才可以看到显示数据。

http://host-application:10060/abis

tips：

> 如果遇到页面加载不出来，可以通过浏览器查看http://host-application:10060/actuator/hystrix.stream，http://host-application:10070/actuator/hystrix.stream页面是否正常返回结果（除了ping以外的返回信息）。如果只是在ping，则试着访问一下两个服务提供的url，比如：http://host-application:10060/abis，http://host-application:10070/pri。然后再看上述页面是否有返回结果，如果有，再看Hystrix-Dashboard的界面是否可以正常显示。
>
> 此时如果还不能正常显示，那么可能就是由于Hystrix-Dashboard的主界面中输入的url有误，请认真核对。

#### turbine dashboard 查看多个应用
hystrix dashboard只能针对单个服务进行查看；
如果需要对多个服务在一个页面同时查看，则需要用到turbine dashboard

`Turbine`可以将所有单独的`hystrix.stream`聚合成一个`turbine.stream`，
以便在`Hystrix Dashboard`上查看，它使用`DiscoveryClient`接口找出生产
`/hystrix.stream`的相关服务。

1.将cloud-hystrix-dashboard, service-pri-server，service-abis-server打成jar包部署到linux环境中

2.启动上述3个服务，浏览器访问

http://host-application:10040/hystrix

可以看到Hystrix-Dashboard的主界面，主界面上输入

http://host-application:10040/turbine.stream 

Title输入随意（如abis），然后点击 `Monitor Stream`按钮

可以跳转Turbine-Dashboard显示页面，显示为loading状态，此时需要访问一下
service-abis-server，service-pri-server提供的url服务，才可以看到显示数据。

http://host-application:10060/abis

http://host-application:10070/pri

### 验证配置中心 sprng cloud config
spring cloud config配置中心如果想要实现动态刷新，则需要 spring cloud bus 
再加上kafka/rabbitmq的支持。

本示例中kafka搭建在hostname为host-application的主机中。
示例中以本地文件为例，当然也可以使用git。

本示例提供的功能为：

> 监听本地E:/cloud-config-client-dev.properties文件，当文件发生变化以后，向/actuator/bus-refresh url中发送post请求，让spring cloud更新@RefreshScope作用域中的值，实现热更新

1.将本示例中的cloud-config-client-dev.properties文件放到本地磁盘的 E:\temp目录下

2.启动cloud-config-server, cloud-config-client两个服务

3.访问cloud-config-client服务暴露的url接口

http://localhost:10000/showWord

查看返回结果，即为此时E:\temp下的cloud-config-client-dev.properties文件中的
`test.word`对应的内容

4.修改E:\temp\cloud-config-client-dev.properties下`test.word`对应的内容
然后再访问

http://localhost:10000/showWord

发现内容实现了热更新

### spring cloud sleuth 链路追踪
spring cloud 中如果对服务调用链路进行追踪记录，需要用到spring-cloud-sleuth组件
加上zipkin
> Zipkin是一个致力于收集分布式服务的时间数据的分布式跟踪系统。
> 提供了可插拔数据存储方式：In-Memory、MySql、Cassandra以及Elasticsearch。

注意：
Spring Boot 2.0之前，需要自己实现一个zipkin-server。
在Spring Boot 2.0之后官方不再建议自定义zipkin，建议使用官方提供的zipkin.jar包。

[官网链接](<https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/>)

也可以使用本示例提供的jar包zipkin-server-2.12.9-exec.jar，

然后 java -jar 来启动，zipkin的默认端口为9411，通过浏览器访问

```shell script
$ java -jar zipkin-server.jar
```

http://localhost:9411/

tips：

> 访问页面时看不到对应的应用，需要先访问服务提供的url，然后才能在zipkin页面看到应用和

### spring boot admin监控后台服务模块

spring boot项目可以通过 spring boot admin 进行监控查看，
其内部通过spring-boot-starter-actuator实现

1.启动spring-boot-admin-server服务

2.浏览器访问该服务

http://localhost:10080/

可以看到启动的springboot服务模块，内含有各个服务的日志监控，健康状态，jvm情况等

### spring cloud gateway
使用方式通spring cloud zuul，该组件需要redis支持，用于计算令牌桶。本示例中redis搭建在host-application主机上。

具体代码参加sprng cloud gateway

### spring cloud zuul ratelimite

用于给zuul提供令牌桶限流功能。