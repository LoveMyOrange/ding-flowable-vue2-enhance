<template>
  <a-select v-model="currentValue" :size="size" allowClear class="w-fill" :placeholder="placeholder" @change="onChange">
    <a-select-option :value="data[valueName]" v-for="(data, i) in datas" :key="i">
      {{ data[labelName] }}
    </a-select-option>
  </a-select>
</template>
<script>
  export default {
    name: 'FlowSimpleSelect',
    props: {
      datas: {
        type: Array,
        required: false,
        default: () => [],
      },
      valueName: {
        type: String,
        required: false,
        default: 'value',
      },
      labelName: {
        type: String,
        required: false,
        default: 'name',
      },
      size: {
        type: String,
        required: false,
        default: 'large',
      },
      placeholder: {
        type: String,
        required: false,
        default: '请选择',
      },
      value: {
        type: Number,
        required: false,
        default: null,
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
    },
    mounted() {
      this.initData(this.value);
    },
    methods: {
      initData(value) {
        if (value) {
          this.currentValue = value;
        } else {
          this.currentValue = null;
        }
      },
      onChange(value) {
        this.$emit('input', value);
        this.$emit(
          'update:name',
          this.datas.filter((data) => data[this.valueName] == value).map((data) => data[this.labelName]),
        );
        this.$emit('change', value);
      },
    },
  };
</script>
