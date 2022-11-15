<template>
  <div class="flow-row">
    <div class="flow-box">
      <div class="flow-item">
        <div class="node-name" :class="nameClass(node, 'node-temmi')">
          分隔
          <div v-if="!readable" class="close-icon"><a-icon type="close-circle" /></div>
        </div>
      </div>
      <FlowAddNode :node.sync="node" :nodeType="7" :readable="readable" />
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import FlowAddNode from '../Add/index.vue';
  export default {
    name: 'DividNode',
    components: { FlowAddNode },
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
