<template>
  <span class="node-name-title">
    <span v-if="!isInput" @click.stop="clickEvent()">{{ value }}</span>
    <a-input v-if="isInput" type="text" @blur="blurEvent()" @focus="$event.currentTarget.select()" v-focus v-model="value" :style="{ width: width }" />
  </span>
</template>
<script>
  import { flowMixin } from '../mixins/flowMixin';
  export default {
    name: 'EditName',
    mixins: [flowMixin],
    props: {
      value: {
        type: String,
        required: false,
      },
      width: {
        type: String,
        required: false,
        default: '85%',
      },
    },
    data() {
      return { isInput: false };
    },
    methods: {
      clickEvent(index) {
        this.isInput = true;
        this.$emit('edit', false);
      },
      blurEvent(index) {
        this.isInput = false;
        this.$emit('input', this.value);
        this.$emit('edit', true);
      },
    },
  };
</script>
