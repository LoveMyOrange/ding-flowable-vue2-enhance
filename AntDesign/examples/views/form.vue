<template>
  <!-- <FlowDesign ref="FlowDesign" :node.sync="childNode" /> -->
  <!--<FlowDesign ref="FlowDesign" :node.sync="childNode" :readable="readable" />-->
  <!-- <FlowDesign ref="FlowDesign" /> -->
  <div></div>
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
