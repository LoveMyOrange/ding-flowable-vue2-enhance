<template>
  <div class="flow-row-container">
    <FlowApproverNode v-if="node && (node.type == 1 || node.type == 6)" :node="node" :readable="readable" />
    <FlowCopyerNode v-if="node && node.type == 2" :node="node" :readable="readable" />
    <BranchNode v-if="node && node.type == 4" :node="node" :readable="readable" />
    <EventNode v-if="node && node.type == 5" :node="node" :readable="readable" />
    <WriteNode v-if="node && node.type == 0" :node="node" :readable="readable" />
    <SuggestNode v-if="node && node.type == 7" :node="node" :readable="readable" />
    <ParallelNode v-if="node && node.type == 9" :node="node" :readable="readable" />
    <PolymerizeNode v-if="node && node.type == 11" :node="node" :readable="readable" />
    <NoticeNode v-if="node && node.type == 20" :node="node" :readable="readable" />
    <FlowNode v-if="node && node.childNode && node.childNode.hasOwnProperty('name')" :node="node.childNode" :readable="readable" />
  </div>
</template>
<script>
  import { flowMixin } from '../mixins/flowMixin';
  import FlowApproverNode from './Approver/index.vue';
  import FlowCopyerNode from './Copyer/index.vue';
  import NoticeNode from './Notice/index.vue';
  import WriteNode from './Write/index.vue';
  import EventNode from './Event/index.vue';
  import FlowAddNode from './Add/index.vue';
  import FlowBranchSetting from '../FlowDrawer/Branch/index.vue';
  import EditName from '../Common/EditName.vue';
  export default {
    name: 'FlowNode',
    components: {
      FlowApproverNode,
      FlowCopyerNode,
      BranchNode: () => import('./Branch/index.vue'),
      SuggestNode: () => import('./Branch/Suggest.vue'),
      ParallelNode: () => import('./Branch/Parallel.vue'),
      PolymerizeNode: () => import('./Branch/Polymerize.vue'),
      NoticeNode,
      WriteNode,
      EventNode,
      FlowAddNode,
      FlowBranchSetting,
      EditName,
    },
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
