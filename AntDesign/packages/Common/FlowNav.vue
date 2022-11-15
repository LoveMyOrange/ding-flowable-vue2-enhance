<template>
  <div class="designer-nav-box">
    <div class="designer-nav-return">
      <img :src="backIcon" />
      <div v-if="!isMobile" class="select-version-box">
        <span class="title">test</span>
        <span class="version">&nbsp;V1</span>
        <i class="el-icon-caret-bottom selec-down"></i>
      </div>
    </div>
    <div class="designer-nav-center">
      <div v-for="(item, i) in navItems" :key="i" class="designer-nav-center-wrap">
        <div class="designer-nav-center-wrap-item" @click="onChange(item)">
          <span :class="{ 'act-item': currentNav == item.value }">{{ isMobile ? item.shortName : item.name }}</span>
        </div>
      </div>
    </div>
    <div class="designer-nav-button">
      <a-button type="primary" :size="size" @click="onClick">
        <span>{{ buttonName }}</span>
      </a-button>
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../mixins/flowMixin';
  export default {
    name: 'FlowNav',
    components: {},
    mixins: [flowMixin],
    props: {
      currentNav: {
        type: Number,
        default: 1,
      },
      buttonName: {
        type: String,
        default: '发布',
      },
    },
    data() {
      return {
        navItems: [
          {
            name: '基础信息',
            shortName: '基础',
            value: 1,
            path: '/basicInfo',
          },
        //  {
        //     name: '表单设计',
        //     shortName: '表单',
        //     value: 2,
        //     path: '/formDesign',
        //   },
          {
            name: '流程设计',
            shortName: '流程',
            value: 3,
            path: '/flowDesign',
          },
          {
            name: '更多配置',
            shortName: '配置',
            value: 4,
            path: '/flowSetting',
          },
        ],
      };
    },
    methods: {
      onChange(item) {
        this.$router.push({ path: item.path });
        this.$emit('change', item);
      },
      onClick() {
        this.$emit('click');
      },
    },
  };
</script>
