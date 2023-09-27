<template>
  <div>
    <div v-if="mode === 'DESIGN'">
      <el-input size="medium" disabled :placeholder="placeholder" />
    </div>
    <div v-else>
      <el-input v-if="!readerMode" size="medium" clearable v-model="_value" :placeholder="placeholder" />
      <div v-else v-text="_value"></div>
    </div>
  </div>
</template>

<script>
import componentMinxins from '../ComponentMinxins'

export default {
  mixins: [componentMinxins],
  name: "TextInput",
  components: {},
  props: {
    value: {
      type: String,
      default: null
    },
    placeholder: {
      type: String,
      default: '请输入内容'
    },
    readerMode: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {}
  },
  methods: {}
}
</script>

<style scoped>

</style>
