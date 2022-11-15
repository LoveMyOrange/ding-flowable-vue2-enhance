<template>
  <a-input v-model="currentValue" :size="size" :placeholder="placeholder" class="w-fill" />
</template>
<script>
  export default {
    name: 'FlowInput',
    props: {
      size: {
        type: String,
        required: false,
        default: 'large',
      },
      value: {
        type: Array,
        required: false,
        default: () => [],
      },
      placeholder: {
        type: String,
        required: false,
        default: '数值',
      },
    },
    data() {
      return {
        currentValue: null,
      };
    },
    watch: {
      value(curVal, oldVal) {
        this.initData(curVal);
      },
      currentValue(curVal, oldVal) {
        this.onChange(curVal);
      },
    },
    mounted() {
      this.initData(this.value);
    },
    methods: {
      initData(value) {
        if (value && value.length > 0) {
          this.currentValue = value[0];
        } else {
          this.currentValue = null;
        }
      },
      onChange(value) {
        this.$emit('input', [value]);
        this.$emit('update:name', [value]);
      },
    },
  };
</script>
