<template>
  <el-popover
    placement="bottom-start"
    title="添加流程节点"
    width="376"
    trigger="click"
  >
    <div class="node-select">
      <div @click="addApprovalNode">
        <el-icon style="color: rgb(255, 148, 62)" size="37"><Stamp /></el-icon>
        <span>审批人</span>
      </div>
      <div @click="addCcNode">
        <el-icon style="color: rgb(50, 150, 250)" size="37"
          ><Promotion
        /></el-icon>
        <span>抄送人</span>
      </div>
      <div @click="addConditionsNode">
        <el-icon style="color: rgb(21, 188, 131)" size="37"><Share /></el-icon>
        <span>条件分支</span>
      </div>
      <div @click="addConcurrentsNode">
        <el-icon style="color: #718dff" size="37"><Operation /></el-icon>
        <span>并行分支</span>
      </div>
      <div @click="addDelayNode">
        <el-icon style="color: #f25643" size="37"><Clock /></el-icon>
        <span>延迟等待</span>
      </div>
      <div @click="addTriggerNode">
        <el-icon style="color: #15bc83" size="37"><SetUp /></el-icon>
        <span>触发器</span>
      </div>
    </div>
    <template #reference>
      <el-button type="primary" circle>
        <el-icon><Plus /></el-icon>
      </el-button>
    </template>
  </el-popover>
</template>

<script>
export default {
  name: "InsertButton",
  components: {},
  data() {
    return {};
  },
  computed: {},
  methods: {
    addApprovalNode() {
      this.$emit("insertNode3", "APPROVAL");
    },
    addCcNode() {
      this.$emit("insertNode3", "CC");
    },
    addDelayNode() {
      this.$emit("insertNode3", "DELAY");
    },
    addConditionsNode() {
      this.$emit("insertNode3", "CONDITIONS");
    },
    addConcurrentsNode() {
      this.$emit("insertNode3", "CONCURRENTS");
    },
    addTriggerNode() {
      this.$emit("insertNode3", "TRIGGER");
    },
  },
};
</script>

<style lang="less" scoped>
.node-select {
  div {
    display: inline-block;
    margin: 5px 5px;
    cursor: pointer;
    padding: 10px 15px;
    border: 1px solid #f8f9f9;
    background-color: #f8f9f9;
    border-radius: 10px;
    width: 165px;
    position: relative;
    span {
      position: absolute;
      left: 65px;
      top: 18px;
    }
    &:hover {
      background-color: #fff;
      box-shadow: 0 0 8px 2px #d6d6d6;
    }
    i {
      font-size: 25px;
      padding: 5px;
      border: 1px solid #dedfdf;
      border-radius: 14px;
      vertical-align: middle;
    }
  }
}
</style>
