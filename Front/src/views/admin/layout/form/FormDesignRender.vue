<template>
  <component ref="form" :is="config.name" :mode="mode" v-bind="config.props" v-model="_value" />
</template>
<script>

import components from '@/views/common/form/ComponentExport'

export default {
  name: "FormRender",
  components: components,
  props:{
    mode:{
      type: String,
      default: 'DESIGN'
    },
    value: {
      default: undefined
    },
    config:{
      type: Object,
      default: ()=>{
        return {}
      }
    }
  },
  computed: {
    _value: {
      get() {
        const valueType = this.config.valueType
        const value = valueType === "Number" && this.value ? Number(this.value) : this.value;
        return value;
      },
      set(val) {
        const valueType = this.config.valueType
        const value = valueType === "Number" ? Number(val) : val
        this.$emit("input", value);
      }
    }
  },
  data() {
    return {}
  },
  methods: {
    validate(call){
      this.$refs.form.validate(call)
    }
  }
}
</script>

<style lang="less" scoped>

</style>
