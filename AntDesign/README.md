# Antdv 工作流组件 smart-flow-design

<p align="center">
  <a href="https://github.com/vuejs/vue">
    <img src="https://img.shields.io/badge/vue-2.6.14-brightgreen.svg" alt="vue">
  </a>
  <a href="https://github.com/vueComponent/ant-design-vue">
    <img src="https://img.shields.io/badge/Ant%20Design%20Vue-1.7.8-blue" alt="ant-design-vue">
  </a>
  <a href="https://gitee.com/crowncloud/smart-flow-design/blob/master/LICENSE">
    <img src="https://img.shields.io/badge/license-Apache-blue" alt="license">
  </a>
  <a href='https://gitee.com/crowncloud/smart-flow-design/stargazers'>
     <img src='https://gitee.com/crowncloud/smart-flow-design/badge/star.svg?theme=dark' alt='star'/>
  </a>
  <a href='https://gitee.com/crowncloud/smart-flow-design/members'><img src='https://gitee.com/crowncloud/smart-flow-design/badge/fork.svg?theme=white' alt='fork'/>
  </a>
</p>

![图片](https://gitee.com/crowncloud/smart-flow-design/raw/master/demo.jpg) ![图片](https://gitee.com/crowncloud/smart-flow-design/raw/master/setting.jpg)

## 简介

基于 vue、ant-design-vue 的自定义组件

注：该项目不兼容 vue3.0 项目，暂无后续兼容 vue3.0 的计划

- [github](https://github.com/smart-flow/smart-flow-design)
- [码云](https://gitee.com/crowncloud/smart-flow-design)

## 预览 

- 地址 http://47.109.22.115:10090/
- 账号 admin/123456 luopeng/123456

## 特性

- 支持在线流程设计器，钉钉审批模式，中国式工作流
- 支持流程办理、退回、自由流、会签、并行、串行、服务任务等
- 支持退回任务，退回到指定环节，退回到上一步，退回到发起人
- 支持转办任务，将任务交接给他人办理，办理完成后继续下一步骤
- 支持委托任务，将任务委托给他人，他人办理完成后再回到委托人
- 支持智能提交，相同处理人自动跳过，支持自由指定下一步处理人
- 支持作废流程，允许发起人快速终止流程，管理员维护终止流程
- 支持自由流程，根据环节选择，自由跳转到指定环节，特事特办
- 支持流程撤回，下一步未办理的任务，可进行取回撤销重做任务
- 支持流程跟踪图，流程状态展现，流转信息，任务历史，任务分配信息
- 支持一个流程模型挂接多个业务单据，如某公司 8 种费用审批流程，表单不一样，但流程相同
- 支持一个表单挂接多个流程环节，以表单角度去管理流程，方便业务理解
- 支持全局表单，用于流程全局表单配置，目前支持内置表单、url 表单。如果不配置则发起流程会提示错误。
- 支持节点表单，节点表单配置。如果不配置默认使用全局表单。
- 流程事件脚本在线编写，包括：流程启动、完成、取消；任务分配、创建、结束等
- 流程脚本管理（Groovy、Beetl），在线编辑、自动完成、脚本测试、多语言脚本模板维护
- 我的待办任务处理，我的已办任务、我创建的任务查询、流程跟踪、审批记录查询
- 流程管控，在无关联表单情况下流程调试，如流程发起、挂起；流程定义、实例、任务等查询；任务办理，重定位等
- 支持流程组件标签定义（流程按钮、意见审批、下一步流程信息等）快速与自定义的业务表单建立关系。
- 支持版本化管理流程，新调整的流程业务不影响正在运行，未结束的流程继续流转。
- 支持任务加签、催办任务、传阅任务、流程委托设置、流水号管理、常用语管理

## 术语

- **或签：** 一名负责人通过即可通过审批节点
- **会签：** 需所有负责人通过才能通过审批节点
- **加签：** 审批过程中加签
  - 前加签：在当前任务的前面加签，如果选择此操作，则当前待办会消失，等待选择的加签人审批后才能办理当前任务。
  - 后加签：即在当前任务的后面加签，选择此操作后会将任务发送给选择的加签人审批，加签人审批后再发给流程设计的下一步人审批。
  - 并签：即和当前任务并列审批。
- **委托(派)：** 将任务委托给他人处理，他人办理完成后再回到委托人的任务中
- **转交：** 将审批单转交给指定人处理，直接将办理人换成别人，这时任务的拥有着不再是转办人
- **签收：** 一般情况就是多个候选人，或者候选组的情况下，要先把这个任务签收下来，以免别人又做了同样的任务
- **反签收：** 就是把执行人设置为空
  - 注意事项：反签收的时候，一定要先确定是否有候选人或者候选组，如果没有的话，不能反签收。因为会导致这个任务无法认领。

#模块安装

## 组件

- FlowDesign 流程设计

## 发布

```cmd
npm login

npm publish
```

## 本地运行

```cmd
# 使用yarn
yarn install

yarn run dev

# 使用npm
npm install

npm run dev
```

## 插件安装

```cmd
# 使用yarn
yarn add smart-flow-design

# 使用npm
npm i smart-flow-design --save
```

## 引入组件

```javascript
// 在main.js引入
import SmartFlowDesign from 'smart-flow-design';
import 'smart-flow-design/styles/flow-design.less';
Vue.use(SmartFlowDesign);
```

## 使用组件

```html
<template>
  <div>
    <FlowDesign ref="FlowDesign" />
  </div>
</template>
```

## 引入 mini 包

默认包是集成了 antd ui 的，如果项目中也有使用 antd ui，可以选择引入 mini 包来达到减小最终项目的体积，当然要确认项目中已经引入 components_use.js 中的组件使用可以参考 [smart-flow-design-demo](https://gitee.com/crowncloud/smart-flow-design-demo)

```javascript
- import SmartFlowDesign from 'smart-flow-design';
+ import SmartFlowDesign from 'smart-flow-design/lib/smart-flow-design-mini.umd.min'
import 'smart-flow-design/lib/smart-flow-design.css'
Vue.use(SmartFlowDesign)
```

## 参考smart-flow-design-demo
地址 https://gitee.com/crowncloud/smart-flow-design-demo

```node
# 构建mini,生成lib目录
yarn run mini

# 在lib的文件发布到npm
npm login

npm publish
```


## 更新日志

[CHANGELOG](./CHANGELOG.md)

## 如何贡献

非常欢迎你的加入！[提一个 Issue](https://gitee.com/crowncloud/smart-flow-design/issues/new?issue) 或者提交一个 Pull Request。

**Pull Request:**

1. Fork 代码!
2. 创建自己的分支: `git checkout -b feat/xxxx`
3. 提交你的修改: `git commit -am 'feat(function): add xxxxx'`
4. 推送您的分支: `git push origin feat/xxxx`
5. 提交`pull request`

## Git 贡献提交规范

- 参考 [vue](https://github.com/vuejs/vue/blob/dev/.github/COMMIT_CONVENTION.md) 规范 ([Angular](https://github.com/conventional-changelog/conventional-changelog/tree/master/packages/conventional-changelog-angular))

  - `feat` 增加新功能
  - `fix` 修复问题/BUG
  - `style` 代码风格相关无影响运行结果的
  - `perf` 优化/性能提升
  - `refactor` 重构
  - `revert` 撤销修改
  - `test` 测试相关
  - `docs` 文档/注释
  - `chore` 依赖更新/脚手架配置修改等
  - `workflow` 工作流改进
  - `ci` 持续集成
  - `types` 类型定义文件更改
  - `wip` 开发中

## 交流

点击链接加微信，可以直接提问及反馈 bug

![图片](https://gitee.com/crowncloud/smart-flow-design/raw/master/wx.png)

## 字体资源地址

[FiraCode 和 Operator Mono 字体下载地址](https://gitcode.net/mirrors/beichensky/font)

## License

[Apache](https://gitee.com/crowncloud/smart-flow-design/blob/master/LICENSE) Copyright (c) 2022 luocheng
