<template>
  <div class="flow-ant-drawer-footer" :style="footerStyle">
    <a-button type="primary" :size="size" :style="{ marginRight: '8px' }" @click="onSave">
      保存
    </a-button>
    <a-button :size="size" @click="onClose">
      取消
    </a-button>
  </div>
</template>
<script>
  import { flowMixin } from '../mixins/flowMixin';
  import scale from '../util/deviceUtil';
  export default {
    name: 'FlowDrawerFooter',
    mixins: [flowMixin],
    data() {
      return {};
    },
    computed: {
      footerStyle: () => {
        const textAlign = scale.isMobile() ? 'center' : 'left';
        return {
          'text-align': textAlign,
        };
      },
    },
    methods: {
      onSave() {
        this.$emit('save');
      },
      onClose() {
        this.$emit('close');
      },
    },
  };
</script>
