import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: 'flowDesign',
    },
    {
      path: '/basicInfo',
      name: 'basic',
      component: () => import('../views/basic.vue'),
    },
    {
      path: '/formDesign',
      name: 'form',
      component: () => import('../views/form.vue'),
    },
    {
      path: '/flowDesign',
      name: 'flow',
      component: () => import('../views/flow.vue'),
    },
    {
      path: '/flowSetting',
      name: 'setting',
      component: () => import('../views/setting.vue'),
    },
  ],
});
