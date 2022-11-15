<template>
  <div class="flow-row">
    <div class="flow-box">
      <div class="flow-item" :class="{ 'flow-item-active': isActive }" @click="!readable && open('flowWriteSetting', node)">
        <div class="flow-node-box" :class="{ 'has-error': node.error }">
          <div class="node-name" :class="nameClass(node, 'node-fill')">
            <EditName v-model="node.name" />
            <img :src="writeIcon" style="margin-left: 10px;" />
          </div>
          <div class="node-main">
            <span v-if="node.content">
              表单权限:
              <a-tooltip placement="top">
                <template slot="title">
                  <span>{{ node.content }}</span>
                </template>
                {{ node.content }}
              </a-tooltip>
            </span>
            <span v-else class="hint-title">默认表单全可编辑</span>
          </div>
          <!-- 错误提示 -->
          <a-icon v-if="node.error" type="exclamation-circle" theme="filled" class="node-error" />
          <!-- 只有是填写节点才能删除，发起节点不能删除 -->
          <div v-if="!readable && !node.deletable && node.type == 6" class="close-icon">
            <a-icon type="close-circle" @click.stop="node.deletable = true" />
          </div>
          <!-- 删除提示 -->
          <DeleteConfirm :node="node" />
        </div>
      </div>
      <FlowAddNode :node.sync="node" :nodeType="6" :readable="readable" />
    </div>
    <FlowWriteSetting ref="flowWriteSetting" @close="isActive = false" />
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import FlowAddNode from '../Add/index.vue';
  import FlowWriteSetting from '../../FlowDrawer/Write/index.vue';
  import EditName from '../../Common/EditName.vue';
  import DeleteConfirm from '../../Common/DeleteConfirm.vue';
  export default {
    name: 'WriteNode',
    components: { FlowAddNode, EditName, FlowWriteSetting, DeleteConfirm },
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
