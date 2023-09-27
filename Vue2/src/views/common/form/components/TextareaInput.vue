<template>
  <div>
    <div v-if="mode === 'DESIGN'">
      <el-input size="medium" disabled :placeholder="placeholder" show-word-limit :rows="2" type="textarea" />
    </div>
    <div v-else>
      <el-input v-if="!readerMode" size="medium" v-model="_value" clearable :maxlength="255" :placeholder="placeholder" show-word-limit :rows="3" type="textarea" />
      <div v-else v-text="_value"></div>

    </div>
  </div>
</template>

<script>
import componentMinxins from '../ComponentMinxins'

export default {
  mixins: [componentMinxins],
  name: "TextareaInput",
  components: {},
  props:{
    value: {
      type: String,
      default: null
    },
    placeholder:{
      type: String,
      default: '请输入内容'
    },
    readerMode: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    _value: {
      get() {
        return this.value;
      },
      set(val) {
        this.$emit("input", val);
      }
    }
  },
  data() {
    return {}
  },
  methods: {}
}
</script>

<style scoped>

</style>
