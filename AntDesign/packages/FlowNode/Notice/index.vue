<template>
  <div class="flow-row">
    <div class="flow-box">
      <div class="flow-item" :class="{ 'flow-item-active': isActive }" @click="!readable && open('flowNoticeSetting', node)">
        <div class="flow-node-box" :class="{ 'has-error': node.error }">
          <div class="node-name" :class="nameClass(node, 'node-tz')">
            <EditName v-model="node.name" />
            <img :src="noticeIcon" alt="" style="margin-left: 10px;" />
          </div>
          <div class="node-main"><span class="hint-title">设置此节点</span></div>
          <!-- 错误提示 -->
          <a-icon v-if="node.error" type="exclamation-circle" theme="filled" class="node-error" />
          <div v-if="!readable && !node.deletable" class="close-icon"><a-icon type="close-circle" @click.stop="node.deletable = true" /></div>
          <!-- 删除提示 -->
          <DeleteConfirm :node="node" />
        </div>
      </div>
      <FlowAddNode :node.sync="node" :nodeType="5" :readable="readable" />
    </div>
    <FlowNoticeSetting ref="flowNoticeSetting" @close="isActive = false" />
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import FlowAddNode from '../Add/index.vue';
  import FlowNoticeSetting from '../../FlowDrawer/Notice/index.vue';
  import EditName from '../../Common/EditName.vue';
  import DeleteConfirm from '../../Common/DeleteConfirm.vue';
  export default {
    name: 'NoticeNode',
    components: { FlowAddNode, FlowNoticeSetting, EditName, DeleteConfirm },
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
