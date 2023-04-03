# dingding-mid     中国式传统流程引擎开源标杆(Activiti 567 Flowable 56,Camunda7 )

### 大家可扫码加入交流群， 如果二维码失效了，可以加我微信 Doctor4JavaEE  备注 钉钉 拉你入群
<img src="https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/3-10%E5%BE%AE%E4%BF%A1_1680505120682.png" alt="微信群" style="zoom:20%;" />
<img src="https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/%E5%BE%AE%E4%BF%A1_1665560718233.png" alt="作者微信" style="zoom:20%;" />

在介绍本项目之前,我先想与大家谈论谈论业务框架 和 技术框架的问题 以及工作流的诸多问题

## 我想这些话是更比这个项目的源码更有用的

> 那是很早的事了..  
> 回忆在这几年JavaEE的路途中,我的老师在讲Struts2框架(那还是SSH的时代)的时候 提过一段概念,这让我记忆犹新  
> 什么是软件?什么是框架?  

#### 软件(也即给公司做的各种系统) = 应用组件(不变的) + 业务组件(变化的)  
#### 应用组件来形成框架，即框架是半成品的软件！  

> 先定义一个概念 框架分为 技术框架 和 业务框架  

> 列举几个Java领域的框架  
> SpringBoot Spring MyBatis SpringMVC Hibernate  
> 此种框架大家可以理解为 纯技术框架   
> 从gitHub上下载下来 它与我们即将要做的任何系统里面的业务逻辑是没有任何关系的  
> 同时他们也只能算系统分层开发中的某一层, 比如MyBatis 仅仅是持久层(Manager,Dao) SpringMVC(Action,Controller) 

> 但大家需要明白的是   
工作流框架(JBPM,Activiti,Flowable,Camunda,Zeebe)  
并不仅仅是 技术框架, 它从另一个角度来说 也可以算做是 业务框架   
为什么这么说呢? 因为工作流确确实实解决了某些审批中的疑难问题 以及 业务编排的中的诸多问题  
在审批流领域:  
工作流框架提供了整个流程图运转的核心代码,对比于传统的状态机来说,她就灵活多了  

> 另外  也直接就解决了工作流系统中的一些常见业务的代码实现  
比如: 流程跳转与驳回,撤回,审批转办,委派,审批过程中加签,减签,流程迁移,我的待办,我的已办,我发起的,等等常见痛点需求  
而且工作流并不是系统分层开发中的某一层,它本身就连接了几十张表 他是一个完整的项目 解决了审批领域/业务编排系统的痛点  

> 像我们来做业务系统来说 一般情况下 二次开发框架的可能性是非常小的  
比如像Spring 我们顶多自己定义一些 BeanFactory  FactoryBean 一些Processor  
比如像MyBatis 我们顶多自己定义一些 Plugin Interceptor  
但是这些东西并没有改动人家本来的源码 只是我们多加了一些东西  

>但像工作流框架就不一样了, 以Activiti 567 举例    
其框架就没有实现中国式的动态审批跳转以及驳回(注意:不是通过流程图画连线来跳转驳回 我指的是没有连线也能跳转 想跳哪跳哪)  
可以想象一下 我们自己实现流程跳转和驳回那是相当困难的,需要熟悉Activiti底层使用的一些类  
诸如ExecutionEntity TaskEntity CommandContext  XXXAgenda 等等  


> 以上我想表达的意思就是   
- `工作流框架  算是技术框架 + 业务框架`  
- `二次开发工作流框架是相当有技术难点的`  

> 这才造成了  
> 各大视频讲解各种Spring MyBatis源码的视频 多如繁星 而深度讲解工作流的视频国内都没几个(主要是相当有技术难点,而且因为工作流和业务会产生关系)  

> 第二 中国式流程 基本上 属于中国内地才有这样的需求,在外国就没有了,比如流程跳转,驳回, 所以在Activiti ,JBPM时代 这是更加痛苦的    
以往别的框架有问题 我们可以去StackOverFlow里面搜 ,在工作流框架里面 这个法子就不灵了    

> 第三 你像 工作流框架本身就要操作 几十张表, 他内部的逻辑 对我们来说 算是黑盒  
    但是别的系统我们自己写的业务代码 都是白盒(因为是我们亲手写的)  
    一旦工作流内部有问题,我们得各种阅读其本身源码, 这是是工作流框架复杂的另一个原因  
    
    
# 以上 所以国内目前暂无好的开源的工作流  本系统在此背景下特意开源出来,供大家学习使用  

# 👀使用本系统进行商业化之前 请注意以下几点
> - 1>由于本系统具有实际存在的业务价值 具体使用务必按照LINCENSE 文件以及LICENSE_补充协议 实行
  补充协议已经说的非常明白了

> - 2>本系统不允许任何以售卖低代码平台的框架 直接集成到各自系统,
  如确定需要集成,请框架原作者联系我,直接集成进各自系统属于侵权
  因为本系统并不是Apache2.0 协议 以及 MIT协议 本系统协议为 GPL3.0 以及附带了相关补充协议
  包含但不限于 
  pig pigx,ruoyi,yudao,guns,zheng,jeesite,JeecgBoot,橙单,snowy,toduck,bladex 等等
    
> - 3>本系统不允许做云上的系统直接集成并以此来售卖,如果公司确实需要把本系统集成到项目里,请公司联系我,否则视为侵权

> - 4>任何公司需要集成本项目并以此来售卖,请联系我并提供公司名字,否则视为侵权

> - 5>任何个人需要集成本项目并以此来售卖,请粘贴本项目LINCENSE_补充协议到各自项目中,否则视为侵权

> - 6>任何国家机关以及公益机构以及学校对本项目可以无限制使用,本人愿意无偿提供其集成以及探讨问题,算是为国家出一份自己微薄的力量   

### 当然,如果做工作流的,当然都知道,后端才是本项目的重点,特别是json转bpmn的那里 ,  
### 有些缺德的公司/个人/友商/间谍狗倒是有可能单单直接把后台借鉴进去,这样就不受GPL3.0协议的限制了,首先我想说的是,这块才是本项目的核心代码  
### 对于这个,我只能说是道德问题,我无法要求你们, 只能说有点不要脸   
### 但这样做的话,属于说使得国内开源环境越来越坏,这样越来越没有人愿意开源了, 纯纯属于是 污染国内开源环境了  
### 我希望大家不要做这样的人/公司!!!  


  

> - 7>本项目前端并不是本人开发,是码云上的开源项目  为方便本项目 本项目维护一份此项目的前端源码保留原有协议以及作者版权印记  
    开源地址为: https://gitee.com/willianfu/jw-workflow-engine  
  
### -------注意:-------->

> - 8>本项目后台不开放PR,也不开放新功能收集,光提issue就行了,     
  因为本人是专门做流程引擎系统(低代码平台)的  
  工作流可以说基本什么需求都做过,我觉得可以加的我自己就加了,不需要提PR,我自己一个人就能做  
  本项目前端的PR直接去上述前端地址去那里提就行,当然为了方便,本项目会带上一份前端源码在项目结构中,但会保留原作者版权信息



### 感谢如下开源项目
前端项目  
> - 1>https://gitee.com/willianfu/jw-workflow-engine 提供了前端源码 保留版权信息 Apache2.0协议(此是Vue2版本的)  
> - 2>https://gitee.com/crowncloud/smart-flow-design 提供了前端源码 保留版权信息 Apache2.0协议(此是AntDV2版本的)  
> - 3>https://github.com/StavinLi/Workflow-Vue3 提供了前端源码 保留版权信息 MIT协议(此是Vue3版本的)  
> - 4>https://github.com/cedrusweng/workflow-react 提供了前端源码 保留版权信息 无LICENSE文件,未对其进行二次开发,仅引用 保留版权信息 (此是React版本的)  
> - 5>https://github.com/wangzhenggui/dingding-approval-flow 提供了前端源码 保留版权信息 无LICENSE文件,未对其进行二次开发,仅引用 保留版权信息 (此是React版本的)  
后台项目  
> - 1>https://gitee.com/willianfu/jw-workflow-engine-server 提供了几个用户,部门表结构  Apache2.0协议  
个人  
> - 1>感谢如下个人    
    李 銍 lzgabel  lz19960321lz@gmail.com  
    于胜灵 yushengling@zhihuisystem.com  
    Flowable群小白菜 18877811997  
    
以上人员可以找我领取一套 本人珍藏的Flowable\Activiti\Camunda视频教程 感谢你们的付出!  





  
# 👀本项目商业计划(本项目是2022年初先发布的商业版,目前也发了开源版)
> - 0> 开源版和商业版不是一套东西 商业版用的也是仿钉钉流程设计器,功能更加强大,建议企业来采购,个人别来购买,用开源版就行,开源版也会持续更新滴!!!  
> - 1> 开源是因为发现很多个人开发者需要这样的系统,以及国内暂无可用的后台接Flowable的  
       开源版也是有其商业价值的!!! !   
       开源版是对标wflow商业版的,他这个作者原价好几千块钱,本人完全免费开源,前后端代码没有后门限制      
> - 2>本项目也有Activiti/Camunda版本,此两个版本暂不开源.但JSON-转BPMN部分代码 Activiti567均可以完全照搬 也是可以做到的        
> - 3>本项目也有Ativiti5,6,7,Flowable5,6,Camunda7 的真正商业版本,功能比开源版更多,且重写了部分工作流框架源码进行优化,如有需要,可以联系我微信    
> - 4>本人接公司培训工作流兼职  可以培训 如下内容: Activiti5,6,7,Flowable5,6,Camunda7 这些框架 或技术支持     
      如有公司需要培训工作流框架,可以联系我  

> - 5>过段时间之后可能会根据当前这个项目 做一个付费课程  讲本项目的核心源码以及如何和业务打通 属于是真正的实战课程了 全网唯一的纯实战  

> - 6>当前项目从工作流角度来讲是一个好东西,但是并没有和业务打通 这算是一个点 交给你们自行弄或者群里讨论也可以的


## 最后, 给学习流程引擎框架(Activiti567,Flowable56,Camunda7,Zeebe8)的Java开发人员几个建议
> - 1> 一定一定不要认为Activiti 没有提供对应的表查询的API ,那么就无法完成了   
      我们可以 把他的这些表 Mapper 都写出来, 自己查就可以了 ,但是要注意的是, 需要先看好Activiti的索引是怎么建的,  
      防止自己写导致索引失效,导致查询效率慢 (自己不要写更新SQL, 可以通过CMD 更新, 因为 他有乐观锁版本列  ,自己写的Mapper仅仅用于查询 推荐)  
> - 2> 一定一定要把他的执行SQL 打印出来, 这样才会知道该如何优化 ,因为工作流本身 比如很小的一个功能,     
      就至少会 操作 十几张表 比如start 工作流 ,所以一定要注意它的性能优化,对于SQL不强的人,可以在了解的Activiti的表结构之后 看一下他是如何进行多表联查的  
> - 3> 一定不要觉得,从数据库 中查询出数据 就代表 工作流 进阶了 , 像类似这种, 从数据库查询到的对应的数据,   
      只是Activiti的 入门, 根本满足不了 产品的需求的, 比如一个Activiti 稍微难一点(中国式流程)的功能, 流程任意跳转,  
      功能, 比如需要完成这个功能, 是相当有难度的, 要完成这个功能 , 其实大部分的操作 都不是表, 而是 一些高级的类中,   
      只不过数据最后流转完了之后到了数据库中     



## 想深耕低代码平台\工作流引擎的,可以看看我B站的几个视频,会对你们有帮助的
   https://www.bilibili.com/video/BV1nZ4y1z7B2/?spm_id_from=333.788


## 💪支持作者

**开源不易，需要鼓励，如果觉得这个项目有帮助到了你请我喝杯咖啡吧😋**

### 大家可扫码加入交流群， 如果二维码失效了，可以加我微信 Doctor4JavaEE  备注 钉钉 拉你入群
<img src="https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/%E5%BE%AE%E4%BF%A1_1665560718233.png" alt="作者微信" style="zoom:20%;" />
<img src="https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/3-10%E5%BE%AE%E4%BF%A1_1680505120682.png" alt="微信群" style="zoom:20%;" />
<img src="https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/QQ%E7%BE%A4_1667801471531.jpg" alt="QQ群更新" style="zoom:20%;" />
<img src="https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/%E5%BE%AE%E4%BF%A1%E6%94%AF%E4%BB%98%E5%AE%9D%E5%90%88%E5%B9%B6_1666146178265.jpg" alt="打赏" style="zoom:20%;" />






## 👀开源版界面一览

###  **工作区面板** 

<img src="https://images.gitee.com/uploads/images/2020/1005/140253_39e3f2d5_4928216.png" alt="输入图片说明" title="屏幕截图.png" style="zoom: 50%;" />

<img src="https://images.gitee.com/uploads/images/2020/1005/140329_89cd5aac_4928216.png" alt="输入图片说明" title="屏幕截图.png" style="zoom:50%;" />



### 表单管理

 **工作流表单管理，支持分组和单组表单拖拽排序** 

<img src="https://images.gitee.com/uploads/images/2020/1005/140358_17fc6838_4928216.png" alt="输入图片说明" title="屏幕截图.png" style="zoom:50%;" />

<img src="https://images.gitee.com/uploads/images/2020/1005/140502_bdc2ea04_4928216.png" alt="输入图片说明" title="屏幕截图.png" style="zoom:50%;" />


---------

####  **表单基本设置** 

<img src="https://images.gitee.com/uploads/images/2020/1005/140559_5c51a89b_4928216.png" alt="输入图片说明" title="屏幕截图.png" style="zoom: 50%;" />




--------

####  **表单设计器**

>  支持分栏布局、明细表格、以及多种基础组件，支持自定义开发组件

![image-20220724220114724](https://pic.rmb.bdstatic.com/bjh/b0f1ed22d61ea86b4222b89dbea6ecd1.png)

![image-20220724221040780](https://pic.rmb.bdstatic.com/bjh/73e71e1323812a57802a76beffe27906.png)






---------

 #### 流程设计器

> 任意条件层级审批流程设计， 审批节点支持多种业务类型设置，支持流程校验

![image-20220711111351476](https://pic.rmb.bdstatic.com/bjh/3300dbc60218a8376b45ed6ed46e8162.png)



**自定义审批条件**

![image-20220722182318650](https://pic.rmb.bdstatic.com/bjh/4599e414142004f3b0445e478018b8be.png)


---------

**自定义复杂流转条件**

> 可视化流程逻辑分支条件

![image-20220722182622661](https://pic.rmb.bdstatic.com/bjh/299989bb8b256beae152a29ea611b790.png)

---------



 **支持多种类型业务节点，支持配置校验，灵活配置** 

<img src="https://pic.rmb.bdstatic.com/bjh/e35d8375eae56b4b9bbace88ee2a00fd.png" alt="image-20220722182427315" style="zoom:50%;" />

**支持无限层级嵌套**

![image-20220711111911427](https://pic.rmb.bdstatic.com/bjh/02cd8936e081bdd053bfa695826817ba.png)

**自动校验设置项，列出所有错误提示**

<img src="https://pic.rmb.bdstatic.com/bjh/ddd20cd54d9502f8eec59565864dfb2a.png" alt="image-20220731215022817" style="zoom:50%;" />

**条件节点优先级动态拖拽，实时刷新**

<img src="https://images.gitee.com/uploads/images/2021/0416/200127_a59216a1_4928216.png" alt="输入图片说明" title="屏幕截图.png" style="zoom:50%;" />



## ✍开发










> 特别说明：源码、JDK、MySQL、Redis等存放路径禁止包含中文、空格、特殊字符等

## 环境要求

> 官方建议： JDK版本不低于 `1.8.0_281`版本，可使用`OpenJDK 8`、`Alibaba Dragonwell 8`、`BiShengJDK 8`

项目  | 推荐版本                              | 说明
-----|-----------------------------------| -------------
JDK  | 1.8.0_281            | JAVA环境依赖(需配置环境变量)
Maven  | 3.6.3                             | 项目构建(需配置环境变量)
Redis  | 3.2.100(Windows)/6.0.x(Linux,Mac) |
MySQL  | 5.7.x+                            | 数据库任选一(默认)
SQLServer  | 2012+                             | 数据库任选一
Oracle  | 11g+                              | 数据库任选一
PostgreSQL  | 12+                               | 数据库任选一

## 工具推荐
> 为防止无法正常下载Maven以来，请使用以下IDE版本

IDEA版本  | Maven版本
-----|-------- | 
IDEA2020及以上版本  | Maven 3.6.3及以上版本 |

## IDEA插件

- `Lombok`
- `Alibaba Java Coding Guidelines`
- `MybatisX`


## 环境配置
- 打开`dingding-mid\src\main\resources\application.yml`

> 环境变量
> - dev  开发环境
> - test  测试环境
> - preview 预发布环境
> - pro 生产环境

``` yml
  #环境 dev|test|pro|preview
  profiles:
  active: dev
```

- 打开`application-x.yml`(`x`表示环境变量),需配置以下
  - 服务端口(`port`)
  - 数据库连接
  - Redis

## 启动项目
- `dingding-mid\src\main\java\com\dingding\mid\DingDingAdminApplication.java`，右击运行即可。

### 项目发布

- 在`IDEA`右侧`Maven`-`dingding-mid(root)`-`Lifecycle`中双击`clean`清理下项目
- 双击`package`打包项目
- 打开项目目录，依次打开`dingding-mid\target`，将`dingding-mid-{version}-RELEASE.jar`上传至服务器

### swagger接口文档
- `http://localhost:10000/swagger-ui/`
