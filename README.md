# dingding-mid     中国式传统流程引擎开源标杆(Activiti 567 Flowable 56,Camunda7 )

# 真的不知道起什么名字(内心OS)
# 后端所有节点实现均以开放!!! 留言登记默认合规

```
本项目属于聚合项目,因为不想分开成好几个项目,所以都放到了1个项目里面(内心OS),里面用到的开源项目非常多,本项目不会去贴没有LICENSE的项目(会引发争议)

但本人可以确保,在Activiti&Flowable&Camunda工作流的方案下 且 不改动Activiti&Flowable&Camunda源码的情况下(特别是Camunda),
不可能有比本项目更好的实现方式(不管是开源项目还是收费项目),有你请找我,给我俩大耳光.
包含但不限于-> 会签实现逻辑,找人逻辑,条件表达式组装逻辑,自动通过逻辑..等这几块核心实现
**由于本项目前端略简陋** ,学习后台代码请着重看上述我说的几个地方实现,像什么查代办\已办,这玩意接口有啥好看的.......... 学就学主要有亮点的地方
下方是,用到的前端项目简介

```

| 项目名  |做的改动   | 代码位置  |
|---|---|---|
| wflow  | 在源作者基础上完善了部分前端&完整的后台实现  | Vue2文件夹  |
| wflow-vue3  | 源作者没有写,另一个人写的,我仅仅引用  | 名字很长的文件夹  |
| Workflow  | 稍微改动,  |  Vue3 |
| smart-flow-design  | 稍微改动  | AntDeign  |
| Workflow  | 稍微改动  | React下的03_stavnLi  |


### 大家可扫码加入交流群， 如果二维码失效了，可以加我微信 Doctor4JavaEE  备注 钉钉 拉你入群
| <img src="https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/%E5%BE%AE%E4%BF%A1_1665560718233.png" alt="作者微信" style="zoom:20%;" /> |
|-------------------------------------------------------------------------------------------------------------------------------------|




| 史上最全工作流文档  | http://123.249.74.161:1024/                           |
|------------|-------------------------------------------------------|
| 演示地址:      | http://123.249.74.161/                                |
| Vue3 演示地址: | http://123.249.74.161:5200/                           |
| Vue3 get方式 | http://123.249.74.161:1024/blogs/jianhua/00_demo.html |
| 超高TPS-QPS性能工作流 | http://123.249.74.161:1024/blogs/enterprise_jituan/00_tps-qps.html |
| 单台普通PC服务器日支撑千万级业务流 | http://123.249.74.161:1024/blogs/enterprise_jituan/00_tps-qps.html |








# 👀使用本系统进行商业化之前 请注意以下几点(不收费! 不收费!不收费!)
> - 1>由于本系统具有实际存在的业务价值 具体使用务必按照LINCENSE 文件以及LICENSE_补充协议 实行
> - 2>不能拿来做竞争对手\竞业项目(流程平台)
> - 3>不能二次fork出来再开源(如何fork也是可以的,但是协议必须是GPL 或AGPL)
> - 4>公司使用提供公司名字
> - 5>任何个人需要集成本项目并以此来售卖,请粘贴本项目LINCENSE_补充协议到各自项目中,或者gitee issue写公司名字既可
> - 6>任何国家机关以及公益机构以及学校对本项目可以无限制使用,本人愿意无偿提供其集成以及探讨问题,算是为国家出一份自己微薄的力量   

### 当然,如果做工作流的,当然都知道,后端才是本项目的重点,特别是json转bpmn的那里 ,有些缺德的公司/个人/友商/间谍狗倒是有可能单单直接把后台借鉴进去,
### 这样就不受GPL3.0协议的限制了,首先我想说的是,这块才是本项目的核心代码,
### 对于这个,我只能说是道德问题,我无法要求你们, 只能说有点不要脸,但这样做的话,属于说使得国内开源环境越来越坏,这样越来越没有人愿意开源了, 
### 纯纯属于是 污染国内开源环境了,.我希望大家不要做这样的人/公司!!!

| 相关抄袭狗    | https://gitee.com/junyue/cxygzl?_from=gitee_search |
|----------|----------------------------------------------------|
| 具体抄袭详情请看 | http://123.249.74.161:1024/blogs/copy/00_demo.html |



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
> - 6>https://github.com/miantj/workflow_vue3.git 提供了前端源码 保留版权信息 无LICENSE文件,未对其进行二次开发,仅引用 保留版权信息 (此是Vue3版本的)
后台项目  
> - 1>https://gitee.com/willianfu/jw-workflow-engine-server 提供了几个用户,部门表结构  Apache2.0协议  
个人  
> - 1>感谢如下个人    
    李 銍 lzgabel  lz19960321lz@gmail.com  (已给)
    于胜灵 yushengling@zhihuisystem.com  (已给)
    Flowable群小白菜 18877811997  (已给)
    
以上人员可以找我领取一套 本人珍藏的Flowable\Activiti\Camunda视频教程 感谢你们的付出!  





  
# 👀本项目商业计划(本项目是2022年初先发布的商业版,目前也发了开源版)
> - 0> 开源版和商业版不是一套东西 商业版用的也是仿钉钉流程设计器,功能更加强大,建议企业来采购,个人别来购买,用开源版就行,开源版也会持续更新滴!!!
> - 1> 开源是因为发现很多个人开发者需要这样的系统,以及国内暂无可用的后台接Flowable的,开源版也是有其商业价值的!!!!  


  




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






## 💪支持作者

**开源不易，需要鼓励，如果觉得这个项目有帮助到了你请我喝杯咖啡吧😋**

### 大家可扫码加入交流群， 如果二维码失效了，可以加我微信 Doctor4JavaEE  备注 钉钉 拉你入群
<img src="https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/%E5%BE%AE%E4%BF%A1_1665560718233.png" alt="作者微信" style="zoom:20%;" />
<img src="https://pro.cxygzl.com/wj//2023-09-18/36a3f20a346646cc87b32abc3c44d63d-918.jpg" alt="微信群" style="zoom:20%;" />
<img src="https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/QQ%E7%BE%A4_1667801471531.jpg" alt="QQ群更新" style="zoom:20%;" />







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

### knife4j接口文档
- `http://ip:端口/doc.html


### 启动前端(因为是前后端分离的项目) (B站有配套视频讲解了 各个版本的前端以及后端如何启动)

- `Vue2 版本对应视频 :  https://www.bilibili.com/video/BV18D4y1C7qE/?spm_id_from=333.999.0.0`
- `Vue3 版本对应视频 :  https://www.bilibili.com/video/BV1aG4y1t7JQ/?spm_id_from=333.788&vd_source=fb655b691713324522e551b8acef3630`
- `AntDesignVue 版本对应视频 :  https://www.bilibili.com/video/BV1X84y1C7AD/?spm_id_from=333.788&vd_source=fb655b691713324522e551b8acef3630`
- `React 版本对应视频 :  https://www.bilibili.com/video/BV13Y411d7g6/?spm_id_from=333.788&vd_source=fb655b691713324522e551b8acef3630`
- `RuoYI-Vue-Camunda 版本对应视频 :  https://www.bilibili.com/video/BV1rv4y1L7wD/?spm_id_from=333.788&vd_source=fb655b691713324522e551b8acef3630`


