<template>
  <div class="flow-row">
    <div class="flow-box">
      <div class="flow-item" :class="{ 'flow-item-active': isActive }" @click="!readable && open('flowApproverSetting', node)">
        <div class="flow-node-box" :class="{ 'has-error': node.error }">
          <div class="node-name" :class="nameClass(node, node.type == 1 ? 'node-sp' : 'node-transact')">
            <EditName v-model="node.name" />
            <img :src="approverIcon" style="margin-left: 10px;" />
          </div>
          <div class="node-main">
            <span v-if="node.content">
              {{ node.type == 1 ? '审批人' : '办理人' }}:
              <a-tooltip placement="top">
                <template slot="title">
                  <span>{{ node.content }}</span>
                </template>
                {{ node.content }}
              </a-tooltip>
            </span>
            <span v-else class="hint-title">设置此节点</span>
          </div>
          <!-- 错误提示 -->
          <a-icon v-if="node.error" type="exclamation-circle" theme="filled" class="node-error" />
          <div v-if="!readable && !node.deletable" class="close-icon"><a-icon type="close-circle" @click.stop="node.deletable = true" /></div>
          <!-- <div class="flow-node-toolbar">
            <a-icon type="copy" @click.stop="node.deletable = true" />
          </div> -->
          <!-- 删除提示 -->
          <DeleteConfirm :node="node" />
        </div>
      </div>
      <!-- 如果子节点是意见分支,则只能添加一个意见分支 -->
      <FlowAddNode :node.sync="node" :nodeType="node.type" :readable="readable" />
    </div>
    <FlowApproverSetting ref="flowApproverSetting" @close="isActive = false" />
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import FlowAddNode from '../Add/index.vue';
  import FlowApproverSetting from '../../FlowDrawer/Approver/index.vue';
  import EditName from '../../Common/EditName.vue';
  import DeleteConfirm from '../../Common/DeleteConfirm.vue';
  export default {
    name: 'FlowApproverNode',
    components: { FlowAddNode, FlowApproverSetting, EditName, DeleteConfirm },
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
      return {
        deleteable: false,
      };
    },
    methods: {},
  };
</script>
