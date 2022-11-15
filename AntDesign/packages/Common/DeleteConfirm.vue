<template>
  <div v-if="node.deletable" class="node-delete-layer">
    <a-button :size="size" class="ant-btn-ghost node-delete-cancel" @click.stop="delNode(node, callback)">删除</a-button>
    <a-button :size="size" @click.stop="node.deletable = false">取消</a-button>
  </div>
</template>
<script>
  import { flowMixin } from '../mixins/flowMixin';
  export default {
    name: 'DeleteConfirm',
    mixins: [flowMixin],
    props: {
      node: {
        type: Object,
        default: function() {
          return {};
        },
      },
    },
    data() {
      return {};
    },
    methods: {
      callback() {
        this.$emit('callback');
      },
    },
  };
</script>
