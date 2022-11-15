<template>
  <FlowSetting ref="FlowSetting" />
</template>
<script>
  export default {
    data() {
      return {
        childNode: {},
      };
    },
    created() {},
    methods: {
      toReturn() {
        this.$router.push({ path: '/flow/model' });
      },
      handleSave() {},
    },
  };
</script>
<style></style>
