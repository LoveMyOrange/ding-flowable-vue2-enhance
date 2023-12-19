import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

const viewport = {
  content: "width=device-width, initial-scale=1.0, user-scalable=no",
};

const router = new Router({
  //mode: 'history',
  //base: __dirname,
  routes: [
    {
      path: "/",
      redirect: "/index",
    },
    {
      path: "/index",
      name: "index",
      component: () => import("@/views/Index.vue"),
      meta: { title: "OA工作流", viewport: viewport },
    },
    {
      path: "/workspace",
      name: "workspace",
      component: () => import("@/views/workspace/index.vue"),
      meta: { title: "工作区", viewport: viewport },
      redirect: '/workspace/approval',
      children: [
        {
          path: "approval",
          name: 'approval',
          component: () => import('@/views/workspace/approval/index.vue'),
          meta: { title: "审批列表", viewport: viewport },
        },
        {
          path: "todo",
          name: 'todo',
          component: () => import('@/views/workspace/todo/index.vue'),
          meta: { title: "待我处理", viewport: viewport },
        },
        {
          path: "apply",
          name: 'apply',
          component: () => import('@/views/workspace/apply/index.vue'),
          meta: { title: "我发起的", viewport: viewport },
        },
        {
          path: "about",
          name: 'about',
          component: () => import('@/views/workspace/about/index.vue'),
          meta: { title: "关于我的", viewport: viewport },
        },
        
        {
          path: "cc",
          name: 'cc',
          component: () => import('@/views/workspace/cc/index.vue'),
          meta: { title: "抄送我的", viewport: viewport },
        },
        {
          path: "submit",
          name: 'submit',
          component: () => import('@/views/workspace/submit/index.vue'),
          meta: { title: "数据管理", viewport: viewport },
        }
      ]
    },
    {
      path: "/workspace/process/instance/tabs",
      name: "processInstanceTabs",
      component: () =>
        import("@/views/workspace/process/ProcessInstanceTabs.vue"),
      meta: { title: "流程详情", viewport: viewport },
    },
    {
      path: "/formsPanel",
      name: "formsPanel",
      component: () => import("@/views/admin/FormsPanel.vue"),
      meta: { title: "表单列表", viewport: viewport },
    },
    {
      path: "/admin/design",
      name: "design",
      component: () => import("@/views/admin/FormProcessDesign.vue"),
      meta: { title: "表单流程设计", viewport: viewport },
      /*   children: [
        {
          path: "baseSetting",
          name: "baseSetting",
          component: () => import("@/views/admin/layout/FormBaseSetting.vue"),
          meta: {title: '基础设置'}
        }, {
          path: "formSetting",
          name: "formSetting",
          component: () => import("@/views/admin/layout/FormDesign.vue"),
          meta: {title: '表单设计'}
        }, {
          path: "processDesign",
          name: "processDesign",
          component: () => import("@/views/admin/layout/ProcessDesign.vue"),
          meta: {title: '流程设计'}
        }, {
          path: "proSetting",
          name: "proSetting",
          component: () => import("@/views/admin/layout/FormProSetting.vue"),
          meta: {title: '高级设置'}
        }
      ]*/
    },
  ],
});

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  if (to.meta.content) {
    const head = document.getElementByTagName("head");
    const meta = document.createElement("meta");
    meta.name = "viewport";
    meta.content = "width=device-width, initial-scale=1.0, user-scalable=no";
    head[0].appendChild(meta);
  }
  next();
  sessionStorage.setItem("router-path", to.path);
});

export default router;
