<template>
  <div>
    <el-form inline label-width="100px">
      <el-form-item label="调整优先级" prop="level">
        <el-popover
          placement="right"
          title="拖拽条件调整优先级顺序"
          width="250"
          trigger="click"
        >
          <template #reference>
            <el-button>
              <el-icon><Sort /></el-icon>
              第{{ nowNodeLeave + 1 }}级
            </el-button>
          </template>

          <draggable
            style="width: 100%; min-height: 25px"
            :list="prioritySortList"
            group="from"
            :options="sortOption"
          >
            <div
              :class="{
                'drag-no-choose': true,
                'drag-hover': cd.id === selectedNode.id,
              }"
              v-for="(cd, index) in prioritySortList"
              :key="index"
            >
              <div>{{ cd.name }}</div>
              <div>优先级 {{ index + 1 }}</div>
            </div>
          </draggable>
        </el-popover>
      </el-form-item>
      <el-form-item label="条件组关系" label-width="150px">
        <el-switch
          v-model="config.groupsType"
          active-color="#409EFF"
          inactive-color="#c1c1c1"
          active-value="AND"
          inactive-value="OR"
          active-text="且"
          inactive-text="或"
        >
        </el-switch>
      </el-form-item>
      <el-form-item label="条件组表达式">
        <el-input
          v-model="config.expression"
          placeholder="输入条件组关系表达式  &为与，|为或"
        />
        <span class="item-desc">使用表达式构建复杂逻辑，例如: (A & B) | C</span>
      </el-form-item>
    </el-form>
    <div style="margin-bottom: 15px;">
      <el-button
        type="primary"
        style="margin: 0 15px 0 0"
        round
        @click="addConditionGroup"
      >
        <el-icon style="margin-right: 4px"><Plus /></el-icon>
        添加条件组
      </el-button>
      <span class="item-desc">只有必填选项才能作为审批条件</span>
    </div>
    <group-item />
  </div>
</template>

<script>
import { VueDraggableNext } from "vue-draggable-next";
import GroupItem from "./ConditionGroupItemConfig.vue";

import { store } from "@/views/process/DefaultNodeProps";

export default {
  name: "ConditionNodeConfig",
  components: { draggable: VueDraggableNext, GroupItem },
  props: {
    config: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  computed: {
    select() {
      return this.config.assignedUser || [];
    },
    nowNodeLeave() {
      return this.prioritySortList.indexOf(store.selectedNode);
    },
    //条件节点
    prioritySortList() {
      let node = store.nodeMap.get(store.selectedNode.parentId);
      if (node) {
        return node.branchs || [];
      }
      return [];
    },
  },
  data() {
    return {
      sortOption: {
        animation: 300,
        chosenClass: "choose",
        scroll: true,
        sort: true,
      },
      selectedNode: store.selectedNode,
    };
  },
  methods: {
    addConditionGroup() {
      this.config.groups.push({
        cids: [],
        groupType: "OR",
        conditions: [],
      });
    },
    closeSelect() {},
    selectUser() {
      this.showOrgSelect = true;
    },
    selected(select) {
      console.log(select);
      this.showOrgSelect = false;
      select.forEach((val) => this.select.push(val));
    },
    removeOrgItem(index) {
      this.select.splice(index, 1);
    },
  },
};
</script>

<style lang="less" scoped>
.choose {
  border-radius: 5px;
  margin-top: 2px;
  background: #f4f4f4;
  border: 1px dashed #1890ff !important;
}

.drag-hover {
  background: #79bbff;
  color: #1890ff;
}

.drag-no-choose {
  cursor: move;
  background: #f4f4f4;
  border-radius: 5px;
  position: relative;
  margin-top: 2px;
  padding: 5px 10px;
  border: 1px solid #bcbcbc;
  height: 30px;

  div:nth-child(1) {
    font-size: x-small;
    position: absolute;
    width: 160px;
    left: 10px;
    height: 20px;
    overflow: hidden;
  }

  div:nth-child(2) {
    position: absolute;
    right: 10px;
  }
}
</style>
