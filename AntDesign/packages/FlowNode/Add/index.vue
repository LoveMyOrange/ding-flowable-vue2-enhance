<template>
  <div class="after-node-btn" @click="visible = true">
    <a-popover placement="right" v-model="visible">
      <template slot="content">
        <a-menu mode="vertical" class="flow-ant-menu-vertical">
          <a-menu-item key="1" @click="addType(1)">
            <img :src="approverIcon2" class="anticon" />
            <span>审批节点</span>
          </a-menu-item>
          <a-menu-item key="4" @click="addType(4)">
            <img :src="branchIcon2" class="anticon" />
            <span>分支节点</span>
          </a-menu-item>
          <a-menu-item v-if="nodeType == 1 && $store.state.flow.suggestBranchEnable" key="7" @click="addType(7)">
            <img :src="branchIcon2" class="anticon" />
            <span>意见分支</span>
          </a-menu-item>
          <a-menu-item v-if="nodeType != 10 && $store.state.flow.parallelBranchEnable" key="9" @click="addType(9)">
            <img :src="parallelIcon" class="anticon" />
            <span>并行分支</span>
          </a-menu-item>
          <a-menu-item key="2" @click="addType(2)">
            <img :src="ccIcon2" class="anticon" />
            <span>抄送节点</span>
          </a-menu-item>
          <a-menu-item key="6" @click="addType(6)">
            <img :src="writeIcon2" class="anticon" />
            <span>办理节点</span>
          </a-menu-item>
          <a-menu-item key="20" @click="addType(20)">
            <img :src="noticeIcon2" class="anticon" />
            <span>通知节点</span>
          </a-menu-item>
          <a-menu-item key="5" @click="addType(5)">
            <img :src="webhookIcon2" class="anticon" />
            <span>事件节点</span>
          </a-menu-item>
        </a-menu>
      </template>
      <!-- 当审批节点下添加意见分支,就不允许添加其他类型的节点了 -->
      <img :src="plusIcon" v-if="!readable && (nodeType != 1 || (nodeType == 1 && node.addable))" />
    </a-popover>
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  export default {
    name: 'FlowAddNode',
    components: {},
    mixins: [flowMixin],
    props: {
      node: {
        type: Object,
        default: function() {
          return {
            addable: true,
          };
        },
      },
      nodeType: {
        type: Number,
        default: 1,
      },
      id: {
        type: String,
        default: '',
      },
      readable: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        visible: false,
      };
    },
    methods: {
      addType(type) {
        var addNode = null;
        if (type == 1 || type == 6) {
          addNode = this.addApproverNode(type);
        } else if (type == 2) {
          addNode = this.addCcNode(type);
        } else if (type == 4) {
          addNode = this.addBranchNode(type);
        } else if (type == 5) {
          addNode = this.addEventNode(type);
        } else if (type == 7) {
          addNode = this.addSuggestNode(type);
        } else if (type == 9) {
          addNode = this.addParallelNode(type);
        } else if (type == 20) {
          addNode = this.addNoticeNode(type);
        }
        const nodeType = this.nodeType;
        const currNode = this.node;
        const id = this.id;
        this.$store.dispatch('flow/addNode', { addNode, currNode, nodeType, id });
        if (nodeType == 1 && type == 7) {
          // 当审批节点下添加意见分支,就不允许添加其他类型的节点了
          this.$store.dispatch('flow/updateNode', { currNode, field: 'addable', value: false });
        }
        // this.$message.success('节点添加成功');
      },
      /**
       * 添加审批节点 ||
       */
      addApproverNode(type) {
        return {
          id: this.uuid(),
          name: type == 1 ? '审批人' : '办理人',
          type: type,
          // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
          status: -1,
          // 流程基础配置属性
          attr: {
            // 审批类型
            approvalMethod: 1,
            // 审批方式
            approvalMode: 1,
            // 审批人与发起人为同一人时
            sameMode: 2,
            // 审批人为空时
            noHander: 4,
          },
          // 审批设置
          approverGroups: [
            {
              id: this.uuid(),
              // 审批人模式
              approverType: 1,
              // 层级模式
              levelMode: 1,
              // 审批人ID
              approverIds: [],
              // 审批人名称
              approverNames: [],
            },
          ],
          // 表单权限
          privileges: [],
          // 高级配置
          configure: {},
          // 子节点
          childNode: null,
          // 显示添加按钮
          addable: true,
          // 可删除提示
          deletable: false,
          // 是否有错误
          error: false,
          // 显示内容
          content: null,
        };
      },
      /**
       * 添加抄送节点
       */
      addCcNode(type) {
        return {
          id: this.uuid(),
          name: '抄送人',
          type: type,
          // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
          status: -1,
          // 子节点
          childNode: null,
          // 抄送人设置
          approverGroups: [
            {
              id: this.uuid(),
              // 审批人模式
              approverType: 1,
              // 层级模式
              levelMode: 1,
              // 审批人ID
              approverIds: [],
              // 审批人名称
              approverNames: [],
            },
          ],
          // 表单权限
          privileges: [],
          // 高级配置
          configure: {},
          // 显示添加按钮
          addable: true,
          // 可删除提示
          deletable: false,
          // 是否有错误
          error: false,
          // 显示内容
          content: null,
        };
      },
      /**
       * 添加通知节点
       */
      addNoticeNode(type) {
        return {
          id: this.uuid(),
          name: '通知',
          type: type,
          // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
          status: -1,
          // 子节点
          childNode: null,
          // 审批设置
          approverGroups: [
            {
              id: this.uuid(),
              // 审批人模式
              approverType: 1,
              // 层级模式
              levelMode: 1,
              // 审批人ID
              approverIds: [],
              // 审批人名称
              approverNames: [],
            },
          ],
          // 显示添加按钮
          addable: true,
          // 可删除提示
          deletable: false,
          // 是否有错误
          error: false,
        };
      },
      /**
       * 添加事件节点
       */
      addEventNode(type) {
        return {
          id: this.uuid(),
          name: '事件',
          type: type,
          // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
          status: -1,
          // 子节点
          childNode: null,
          // 显示添加按钮
          addable: true,
          // 可删除提示
          deletable: false,
          // 是否有错误
          error: false,
        };
      },
      /**
       * 添加办理节点
       */
      addWriteNode(type) {
        return {
          id: this.uuid(),
          name: '办理',
          type: type,
          // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
          status: -1,
          // 子节点
          childNode: null,
          // 表单权限
          privileges: [],
          // 显示添加按钮
          addable: true,
          // 可删除提示
          deletable: false,
          // 是否有错误
          error: false,
        };
      },
      /**
       * 添加分支节点
       */
      addBranchNode(type) {
        const uid = this.uuid();
        return {
          id: uid,
          name: '路由',
          type: type,
          // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
          status: -1,
          // 子节点
          childNode: null,
          // 显示添加按钮
          addable: true,
          // 条件节点
          conditionNodes: [
            {
              id: this.uuid(),
              pid: uid,
              name: '分支1',
              type: 3,
              // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
              status: -1,
              // 是否有错误
              error: false,
              // 子节点
              childNode: null,
              // 显示添加按钮
              addable: true,
              // 可删除提示
              deletable: false,
              // 条件组
              conditionGroup: [],
              // 流程基础配置属性
              attr: {
                // 分支类型
                branchType: 1,
                // 优先级
                priorityLevel: 1,
                // 显示优先级
                showPriorityLevel: true,
              },
            },
            {
              id: this.uuid(),
              pid: uid,
              name: '其他情况',
              type: 3,
              // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
              status: -1,
              // 子节点
              childNode: null,
              // 显示添加按钮
              addable: true,
              // 可删除提示
              deletable: false,
              // 条件组
              conditionGroup: [],
              // 流程基础配置属性
              attr: {
                // 分支类型
                branchType: 1,
                // 优先级
                priorityLevel: 2,
                // 显示优先级
                showPriorityLevel: true,
              },
              // 是否有错误
              error: false,
              // 显示内容
              content: '其他情况进入此流程',
            },
          ],
        };
      },
      /**
       * 添加意见分支节点
       */
      addSuggestNode(type) {
        const uid = this.uuid();
        return {
          id: uid,
          name: '意见',
          type: type,
          // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
          status: -1,
          // 子节点
          childNode: null,
          // 显示添加按钮
          addable: true,
          // 是否有错误
          error: false,
          conditionNodes: [
            {
              id: this.uuid(),
              pid: uid,
              name: '同意',
              type: 8,
              // 流程基础配置属性
              attr: {
                // 优先级
                priorityLevel: 2,
              },
              // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
              status: -1,
              // 子节点
              childNode: null,
              // 显示添加按钮
              addable: true,
              // 可删除提示
              deletable: false,
              // 是否有错误
              error: false,
            },
            {
              id: this.uuid(),
              pid: uid,
              name: '不同意',
              type: 8,
              // 流程基础配置属性
              attr: {
                // 优先级
                priorityLevel: 2,
              },
              // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
              status: -1,
              // 子节点
              childNode: null,
              // 显示添加按钮
              addable: true,
              // 可删除提示
              deletable: false,
              // 是否有错误
              error: false,
            },
          ],
        };
      },
      /**
       * 添加并行节点
       */
      addParallelNode(type) {
        const uid = this.uuid();
        return {
          id: uid,
          name: '并行',
          type: type,
          // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
          status: -1,
          // 显示添加按钮
          addable: true,
          // 聚合节点
          childNode: {
            id: this.uuid(),
            pid: uid,
            name: '聚合',
            type: 11,
            status: -1,
            childNode: null,
            // 显示添加按钮
            addable: true,
            // 可删除提示
            deletable: false,
          },
          conditionNodes: [
            {
              id: this.uuid(),
              pid: uid,
              name: '并行1',
              type: 10,
              // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
              status: -1,
              // 是否有错误
              error: false,
              // 显示内容
              content: '任意(其他)',
              // 子节点
              childNode: null,
              // 显示添加按钮
              addable: true,
              // 可删除提示
              deletable: false,
              // 流程基础配置属性
              attr: {
                // 分支类型
                branchType: 3,
              },
              // 条件组
              conditionGroup: [],
            },
            {
              id: this.uuid(),
              pid: uid,
              name: '并行2',
              type: 10,
              // 流程节点状态(用于只读模式, 0:未进行 1:进行中  2:已完成)
              status: -1,
              // 是否有错误
              error: false,
              // 显示内容
              content: '任意(其他)',
              // 子节点
              childNode: null,
              // 显示添加按钮
              addable: true,
              // 可删除提示
              deletable: false,
              // 流程基础配置属性
              attr: {
                // 分支类型
                branchType: 3,
              },
              // 条件组
              conditionGroup: [],
            },
          ],
        };
      },
    },
  };
</script>
