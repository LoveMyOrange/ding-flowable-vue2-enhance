/*
 * @Descripttion:
 * @Author: kcz
 * @Date: 2021-05-02 16:04:02
 * @LastEditors: kcz
 * @LastEditTime: 2021-05-21 13:55:46
 */
// 引入@babel/polyfill处理兼容
import 'babel-polyfill';

import Vue from 'vue';
import App from './App.vue';
import router from './router/';
// import '../packages/core/components_use';
import VueStorage from 'vue-ls';
//import Crownboot from 'crownboot-antd-vue/lib/crownboot-antdv-mini.umd';
import Crownboot from 'crownboot-antd-vue';
import 'crownboot-antd-vue/lib/crownboot-antdv-mini.css';
import CrownBootFlowVue from '../packages/index';
import store from './store';
import { VueAxios } from './utils/request';
import UUID from 'vue-uuid';

Vue.use(VueStorage, {
  namespace: 'pro__', // key prefix
  name: 'ls', // name variable Vue.[ls] or this.[$ls],
  storage: 'local', // storage name session, local, memory
});
Vue.prototype.$store = store;
Vue.use(VueAxios);
Vue.use(UUID);
Vue.use(Crownboot);
Vue.use(CrownBootFlowVue);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
