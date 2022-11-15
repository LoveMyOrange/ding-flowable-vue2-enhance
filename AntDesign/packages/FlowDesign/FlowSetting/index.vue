<template>
  <div class="designer-wrap">
    <FlowNav v-if="navable && !readable" :currentNav="4" :buttonName="buttonName" @change="change" />
    <MenuShot :menus="menus" @change="changeMenu" />
    <div class="designer-content-box">
      <div class="flowSetting-box">
        <div v-if="!isMobile" class="flowSetting-nav-box">
          <div class="flowSetting-nav-group">
            <div v-for="(menu, i) in menus" :key="i" :class="{ 'act-item': menu.activate == true }" class="flowSetting-nav-group-item" @click="changeMenu(menu)">
              <img :src="settingBaseIcon" />
              <span>{{ menu.name }}</span>
            </div>
          </div>
        </div>
        <div class="flowSetting-content-box">
          <Advanced v-if="currentContext == 1"></Advanced>
          <Exhibition v-if="currentContext == 2"></Exhibition>
          <Remind v-if="currentContext == 3"></Remind>
          <Print v-if="currentContext == 5"></Print>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import FlowNav from '../../Common/FlowNav.vue';
  import MenuShot from './MenuShot.vue';
  import Advanced from './Advanced.vue';
  import Exhibition from './Exhibition.vue';
  import Remind from './Remind.vue';
  import Print from './Print.vue';
  export default {
    name: 'FlowSetting',
    mixins: [flowMixin],
    components: { FlowNav, MenuShot, Advanced, Exhibition, Remind, Print },
    props: {
      navable: {
        type: Boolean,
        default: true,
      },
      readable: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        currentNav: 2,
        currentContext: 1,
        buttonName: '保存',
        menus: [
          {
            name: '基本配置',
            code: 1,
            activate: true,
          },
          {
            name: '展示设置',
            code: 2,
            activate: false,
          },
          {
            name: '提醒填写',
            code: 3,
            activate: false,
          },
          /* {
            name: '审批人设置',
            code: 4,
            activate: false,
          }, */
          {
            name: '打印模板',
            code: 5,
            activate: false,
          },
        ],
      };
    },
    computed: {
      isActivate() {
        return function(menu) {
          return menu.activate;
        };
      },
    },
    mounted() {},
    methods: {
      toReturn() {},
      change(type) {},
      changeMenu(menu) {
        this.currentContext = menu.code;
        this.menus.forEach((m) => {
          if (m.name == menu.name) {
            m.activate = true;
          } else {
            m.activate = false;
          }
        });
      },
      save() {
        this.$emit('save', this.nodeData);
      },
    },
  };
</script>
