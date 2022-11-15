<template>
  <div class="flow-tips">
    <div class="flow-tips-item">
      <span class="flow-tips-item-icon flow-tips-complete">●</span>
      <span>已完成</span>
    </div>
    <div class="flow-tips-item">
      <span class="flow-tips-item-icon flow-tips-current">●</span>
      <span>进行中</span>
    </div>
    <div class="flow-tips-item">
      <span class="flow-tips-item-icon flow-tips-not">●</span>
      <span>未进行</span>
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../mixins/flowMixin';
  export default {
    name: 'FlowTips',
    mixins: [flowMixin],
    data() {
      return {};
    },
    computed: {},
    methods: {},
  };
</script>
