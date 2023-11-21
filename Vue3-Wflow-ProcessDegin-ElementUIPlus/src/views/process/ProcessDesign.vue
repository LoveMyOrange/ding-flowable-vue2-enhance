<template>
  <el-main>
    <div class="scale">
      <el-button @click="scale += 10" :disabled="scale >= 150" circle>
        <el-icon><Plus /></el-icon>
      </el-button>
      <span>{{ scale }}%</span>
      <el-button @click="scale -= 10" :disabled="scale <= 40" circle>
        <el-icon><Minus /></el-icon>
      </el-button>
      <el-button size="large" @click="validate">校验流程</el-button>
    </div>
    <div class="design" :style="'transform: scale(' + scale / 100 + ');'">
      <process-tree ref="process-tree" @selectedNode="nodeSelected" />
    </div>
    <el-drawer
      v-model="showConfig"
      :title="selectedNode.name"
      :size="selectedNode.type === 'CONDITION' ? '600px' : '500px'"
      direction="rtl"
      :modal="false"
      destroy-on-close
      :show-close="false"
    >
      <template #header="{ close, titleId, titleClass }">
        <div style="width: 96%; display: inline-block">
          <el-input
            v-model="selectedNode.name"
            v-show="showInput"
            style="width: 300px"
            @blur="showInput = false"
          ></el-input>
          <el-link
            v-show="!showInput"
            @click="showInput = true"
            style="font-size: medium"
          >
            <el-icon style="margin-right: 10px"><EditPen /></el-icon>

            {{ selectedNode.name }}
          </el-link>
        </div>

        <el-icon @click="close" style="cursor: pointer"><Close /></el-icon>
      </template>

      <div class="node-config-content">
        <node-config />
      </div>
    </el-drawer>
  </el-main>
</template>

<script>
import ProcessTree from "./ProcessTree.vue";
import NodeConfig from "./node/config/NodeConfig";
//import ProcessTree from '@/views/common/process/FormComponentConfig.vue'

import { store } from "@/views/process/DefaultNodeProps";

export default {
  name: "ProcessDesign",
  components: { ProcessTree, NodeConfig },
  data() {
    return {
      scale: 100,
      selected: {},
      showInput: false,
      showConfig: false,
    };
  },
  computed: {
    selectedNode() {
      return store.selectedNode;
    },
  },
  mounted() {},
  methods: {
    validate() {
      this.$refs["process-tree"].validateProcess();
    },
    nodeSelected(node) {
      console.log("配置节点", node);
      this.showConfig = true;
    },
  },
  watch: {
    // selectedNode: {
    // handler(node) {
    //   console.log("更新");
    //   this.$refs["process-tree"].nodeDomUpdate(node);
    // },
    // },
  },
};
</script>

<style lang="less" scoped>
.design {
  padding-top: 20px;
  display: flex;
  transform-origin: 50% 0px 0px;
  background: #f5f6f6;
}

.scale {
  z-index: 999;
  position: absolute;
  top: 80px;
  right: 40px;

  span {
    margin: 0 10px;
    font-size: 15px;
    color: #7a7a7a;
    width: 50px;
  }
}

.node-config-content {
  padding: 0 20px 20px;
}

:deep(.el-drawer__body) {
  overflow-y: auto;
  padding: 0;
}
</style>
