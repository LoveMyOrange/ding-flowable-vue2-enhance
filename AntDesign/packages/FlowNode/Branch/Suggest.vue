<template>
  <div class="flow-row">
    <div class="flow-branch">
      <div class="meet-node"></div>
      <div class="flow-col" v-for="(conditionNode, index) in node.conditionNodes" :key="index">
        <div class="clear-left-border" v-if="index == 0"></div>
        <div class="clear-right-border" v-if="node.conditionNodes.length - 1 == index"></div>
        <div class="flow-row">
          <div class="flow-box">
            <div class="flow-item flow-node-branch">
              <div class="flow-branch-suggest">
                <div class="node-name">
                  <span>{{ conditionNode.name }}</span>
                  <span style="margin-left: 10px;">
                    <a-icon v-if="index == 0" type="check-circle" theme="filled" style="color: green;" />
                    <a-icon v-if="node.conditionNodes.length - 1 == index" type="close-circle" theme="filled" style="color: red;" />
                  </span>
                </div>
                <div v-if="!readable && !conditionNode.deletable" class="close-icon"><a-icon type="close-circle" @click.stop="conditionNode.deletable = true" /></div>
                <!-- 删除提示 -->
                <DeleteConfirm :node="conditionNode" @callback="delCallback" />
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
  import FlowNode from '../index.vue';
  import FlowAddNode from '../Add/index.vue';
  import FlowBranchSetting from '../../FlowDrawer/Branch/index.vue';
  import EditName from '../../Common/EditName.vue';
  import DeleteConfirm from '../../Common/DeleteConfirm.vue';
  export default {
    name: 'SuggestNode',
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
    methods: {
      delCallback(conditionNode) {
        let currNode = {
          id: this.node.pid,
        };
        // 将对应的审批节点的添加按钮开启
        this.$store.dispatch('flow/updateNode', { currNode, field: 'addable', value: true });
      },
    },
  };
</script>
