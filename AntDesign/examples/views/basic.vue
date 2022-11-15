<template>
  <BasicInfo ref="BasicInfo" />
</template>
<script>
  export default {
    data() {
      return {
        readable: true,
      };
    },
    created() {},
    methods: {
      toReturn() {
        this.$router.push({ path: '/flow/model' });
      },
      handleSave() {
        this.$refs.FlowDesign.handleSave();
      },
    },
  };
</script>
