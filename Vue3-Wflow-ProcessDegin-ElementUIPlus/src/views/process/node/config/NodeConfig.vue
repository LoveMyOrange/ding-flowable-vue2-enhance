<template>
  <div>
    <el-tabs v-model="active" v-if="name && formConfig.length > 0">
      <el-tab-pane :label="name" name="properties">
        <component
          :is="(selectNode.type || '').toLowerCase()"
          :config="selectNode.props"
        />
      </el-tab-pane>
      <el-tab-pane label="表单权限设置" name="permissions">
        <form-authority-config />
      </el-tab-pane>
    </el-tabs>
    <component
      :is="(selectNode.type || '').toLowerCase()"
      v-else
      :config="selectNode.props"
    />
  </div>
</template>

<script>
import Approval from "./ApprovalNodeConfig.vue";
import Condition from "./ConditionNodeConfig.vue";
import Delay from "./DelayNodeConfig.vue";
import Cc from "./CNodeConfig.vue";
import Trigger from "./TriggerNodeConfig.vue";
import FormAuthorityConfig from "./FormAuthorityConfig.vue";
import Root from "./RootNodeConfig.vue";
// import { useStore } from "vuex";
import { store } from "@/views/process/DefaultNodeProps";

export default {
  name: "NodeConfig",
  components: {
    Approval,
    Condition,
    Trigger,
    Delay,
    Root,
    Cc,
    FormAuthorityConfig,
  },
  data() {
    return {
      active: "properties",
      // $store: useStore(),
    };
  },
  computed: {
    selectNode() {
      return store.selectedNode;
    },
    formConfig() {
      return store.design.formItems;
    },
    name() {
      switch (this.selectNode.type) {
        case "ROOT":
          return "设置发起人";
        case "APPROVAL":
          return "设置审批人";
        default:
          return null;
      }
    },
  },
  methods: {},
};
</script>

<style lang="less" scoped></style>
