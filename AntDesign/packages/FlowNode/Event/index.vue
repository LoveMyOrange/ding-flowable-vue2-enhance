<template>
  <div class="flow-row">
    <div class="flow-box">
      <div class="flow-item" :class="{ 'flow-item-active': isActive }" @click="!readable && open('flowEventSetting', node)">
        <div class="node-name" :class="nameClass(node, 'node-temmi')">
          事件
          <div v-if="!readable" class="close-icon"><a-icon type="close-circle" @click.stop="delNode(node)" /></div>
        </div>
      </div>
      <FlowAddNode :node.sync="node" :nodeType="5" :readable="readable" />
    </div>
    <FlowEventSetting ref="flowEventSetting" @close="isActive = false" />
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import FlowAddNode from '../Add/index.vue';
  import FlowEventSetting from '../../FlowDrawer/Event/index.vue';
  export default {
    name: 'EventNode',
    components: { FlowAddNode, FlowEventSetting },
    mixins: [flowMixin],
    props: {
      node: {
        type: Object,
        default: function() {
          return {};
        },
      },
      readable: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {};
    },
    methods: {},
  };
</script>
