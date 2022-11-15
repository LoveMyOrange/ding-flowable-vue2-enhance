/*
 * @Description: 组件输出文件,不打包antd ui组件
 * @Author: kcz
 * @Date: 2020-01-02 22:41:48
 * @LastEditors: kcz
 * @LastEditTime: 2021-05-14 19:09:25
 */

// 解决Chrome控制台non-passive event listener输出问题
// import "default-passive-events";
// 导入样式
import '../styles/smart-flow-design.less';

// 导出本地iconfont
// import '../static/icons/iconfont';

// 导入单个组件
import { FlowDesign, BasicInfo, FlowSetting } from './FlowDesign/index';
import flowStore from './store/index';

const components = [BasicInfo, FlowDesign, FlowSetting];

const install = function(Vue) {
  // use ant组件
  if (install.installed) return;
  install.installed = true;
  components.map((component) => {
    Vue.component(component.name, component);
  });
};

if (typeof window !== 'undefined' && window.Vue) {
  install(window.Vue);
}

// 这里可以用es6的解构语法导入组件
export { install, BasicInfo, FlowDesign, FlowSetting, flowStore };
// 这里默认导入全部组件
export default {
  install,
  BasicInfo,
  FlowDesign,
  FlowSetting,
  flowStore,
};
