<template>
  <div class="flow-row">
    <div class="flow-branch">
      <div class="branch-node" @click="!readable && addBranch(node)">
        <img :src="branchPlusIcon" />
      </div>
      <div class="meet-node"></div>
      <div class="flow-col" v-for="(conditionNode, index) in node.conditionNodes" :key="conditionNode.id">
        <div class="clear-left-border" v-if="index == 0"></div>
        <div class="clear-right-border" v-if="node.conditionNodes.length - 1 == index"></div>
        <div class="flow-row">
          <div class="flow-box">
            <!-- 其他情况不支持配置 -->
            <div class="flow-item flow-node-branch" @click="!readable && node.conditionNodes.length - 1 != index && open('flowBranchSetting', conditionNode, node)">
              <div class="flow-node-box" :class="{ 'has-error': conditionNode.error }">
                <div class="node-name">
                  <EditName v-model="conditionNode.name" @edit="(showPriorityLevel) => (conditionNode.attr.showPriorityLevel = showPriorityLevel)" />
                  <div class="node-name-level" v-if="conditionNode.attr.showPriorityLevel">优先{{ conditionNode.attr.priorityLevel }}</div>
                  <img :src="branchIcon" style="margin-left: 10px" />
                </div>
                <div class="node-main">
                  <span v-if="conditionNode.content">
                    <a-tooltip placement="top">
                      <template slot="title">
                        <span>{{ conditionNode.content }}</span>
                      </template>
                      {{ conditionNode.content }}
                    </a-tooltip>
                  </span>
                  <span v-else class="hint-title">配置筛选条件</span>
                </div>
                <!-- 错误提示 -->
                <a-icon v-if="conditionNode.error" type="exclamation-circle" theme="filled" class="node-error" />
                <!-- 删除按钮,其他情况不支持删除 -->
                <div v-if="!readable && !conditionNode.deletable && node.conditionNodes.length - 1 != index" class="close-icon">
                  <a-icon type="close-circle" @click.stop="conditionNode.deletable = true" />
                </div>
                <!-- 删除提示 -->
                <DeleteConfirm :node="conditionNode" />
              </div>
            </div>
            <FlowAddNode :node.sync="node" :nodeType="3" :id="conditionNode.id" :readable="readable" />
          </div>
        </div>
        <FlowNode v-if="conditionNode.childNode && conditionNode.childNode.hasOwnProperty('name')" :node="conditionNode.childNode" :readable="readable" />
      </div>
    </div>
    <div class="after-branch-btn">
      <FlowAddNode :node.sync="node" :nodeType="4" :readable="readable" />
    </div>
    <FlowBranchSetting ref="flowBranchSetting" @close="isActive = false" />
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import FlowNode from '../../FlowNode/index.vue';
  import FlowAddNode from '../Add/index.vue';
  import FlowBranchSetting from '../../FlowDrawer/Branch/index.vue';
  import EditName from '../../Common/EditName.vue';
  import DeleteConfirm from '../../Common/DeleteConfirm.vue';
  export default {
    name: 'BranchNode',
    components: { FlowNode, FlowAddNode, FlowBranchSetting, EditName, DeleteConfirm },
    mixins: [flowMixin],
    props: {
      node: {
        type: Object,
        default: function() {
          return {};
        },
      },
      readable: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {};
    },
    methods: {},
  };
</script>
