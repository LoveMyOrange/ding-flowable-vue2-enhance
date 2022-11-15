<template>
  <div class="flow-row">
    <div class="flow-box">
      <div class="flow-item">
        <div class="node-name node-start">开始</div>
      </div>
      <FlowAddNode :node="node" :nodeType="0" :readable="readable" />
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import FlowAddNode from '../Add/index.vue';
  export default {
    name: 'FlowStartNode',
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
        default: true,
      },
    },
    data() {
      return {};
    },
    methods: {},
  };
</script>
