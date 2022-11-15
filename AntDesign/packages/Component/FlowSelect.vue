<template>
  <a-select v-model="currentValue" :size="size" :mode="mode" allowClear class="w-fill" @change="onChange">
    <a-select-option :value="data[valueName]" v-for="(data, i) in datas" :key="i">
      {{ data[labelName] }}
    </a-select-option>
  </a-select>
</template>
<script>
  export default {
    name: 'FlowSelect',
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
      mode: {
        type: String,
        required: false,
        default: 'default',
      },
      value: {
        type: Array,
        required: false,
        default: () => [],
      },
    },
    data() {
      return {
        currentValue: this.mode == 'multiple' ? [] : null,
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
        if (value && value.length > 0 && this.mode == 'default') {
          this.currentValue = value[0];
        } else if (value && value.length > 0 && this.mode == 'multiple') {
          this.currentValue = value;
        } else {
          this.currentValue = this.mode == 'multiple' ? [] : null;
        }
      },
      onChange(value) {
        this.$emit('input', this.mode == 'multiple' ? value : [value]);
        this.$emit(
          'update:name',
          this.datas.filter((data) => (this.mode == 'multiple' ? value.includes(data[this.valueName]) : data[this.valueName] == value)).map((data) => data[this.labelName]),
        );
        this.$emit('change', value);
      },
    },
  };
</script>
