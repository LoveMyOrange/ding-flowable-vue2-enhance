<template>
  <div class="designer-wrap">
    
    <FlowNav v-if="navable && !readable" :currentNav="3" @click="publish" @change="change" />
    <div class="designer-content-box" :style="{ height: readable ? '100vh' : 'calc(100vh - 50px)' }">
      <div class="flow-design-wrap">
        <div id="flow-design" class="flow-design-container" :style="zoomStyle">
          <div id="flow-design-content" class="flow-design-content">
            <FlowStartNode :node="nodeData" />
            <FlowNode :node="nodeData" :readable="readable" />
            <FlowEndNode :node="nodeData" :readable="readable" />
          </div>
        </div>
        <FlowHelper v-if="!readable" />
        <FlowTips v-if="readable" />
        <FlowZoom v-model="zoomValue" />
        <FlowMap v-if="!isMobile" />
      </div>
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../mixins/flowMixin';
  import { getStartNode } from '../util/nodeUtil';
  import FlowZoom from '../Common/FlowZoom.vue';
  import FlowMap from '../Common/FlowMap.vue';
  import FlowNav from '../Common/FlowNav.vue';
  import FlowHelper from '../Common/FlowHelper.vue';
  import FlowTips from '../Common/FlowTips.vue';
  import FlowNode from '../FlowNode/index.vue';
  import FlowStartNode from '../FlowNode/Start';
  import FlowEndNode from '../FlowNode/End';
  export default {
    name: 'FlowDesign',
    mixins: [flowMixin],
    components: { FlowMap, FlowZoom, FlowNav, FlowHelper, FlowTips, FlowStartNode, FlowNode, FlowEndNode },
    props: {
      node: {
        type: Object,
        default: function() {
          return getStartNode();
        },
      },
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
        zoomValue: 100,
      };
    },
    computed: {
      zoomStyle() {
        const zoom = this.zoomValue / 100;
        return {
          zoom: zoom,
        };
      },
      nodeData() {
        return this.$store.getters['flow/node'];
      },
    },
    mounted() {
      // 初始数据
      this.$store.dispatch('flow/setNode', this.node);
    },
    methods: {
      toReturn() {},
      change(type) {},
      handleSave() {},
      getData() {
        return this.nodeData;
      },
      publish() {
       
        this.$emit('publish', this.nodeData);
      },
    },
  };
</script>
