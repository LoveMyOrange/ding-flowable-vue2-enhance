<template>
  <div class="route-tab">
    <el-tabs v-bind="$attrs" :value="$route.path" @tab-click="handleClick">
      <el-tab-pane
        v-for="route in routes"
        :key="route.path"
        :label="route.label"
        :name="route.path"
        :disabled="route.disabled"
        :closable="route.closable"
      >
        <slot v-if="route.path === $route.path"></slot>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: 'RouteTab',
  props: {
    routes: {
      type: Array,
      default: () => []
    },
  },
  methods: {
    handleClick(tab) {
      this.$router.push({
        path: tab.name,
      })
    },
  },
}
</script>
