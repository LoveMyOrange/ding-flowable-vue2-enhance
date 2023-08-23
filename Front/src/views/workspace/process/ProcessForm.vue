<template>
  <div>
    <!-- <el-input v-model="fromData.comments" placeholder="请输入审批内容"></el-input> -->
    <el-row :gutter="10">
      <el-col :span="24">
        <el-button size="small" type="primary" v-for="item in buttonConfig" :key="item.key" @click="handleClickByType(item.key)">{{ item.text }}</el-button>
      </el-col>
    </el-row>
    <AgreenForm ref="AgreenForm"></AgreenForm>
    <org-picker
      title="请选择可发起本审批的人员/部门"
      multiple
      ref="orgPicker"
      :selected="select"
      @ok="onSelected"
    />
    <!-- 评论 -->
    <comment-modal :visible.sync="modalConfig.commentVisible" :processInfo="processInfo" />
    <!-- 委派 -->
    <delegate-modal :visible.sync="modalConfig.delegateVisible" :processInfo="processInfo" />
    <!-- 委派人完成 -->
    <resolve-modal :visible.sync="modalConfig.resolveVisible" :processInfo="processInfo" />
    <!-- 拒绝 -->
    <refuse-modal :visible.sync="modalConfig.refuseVisible" :processInfo="processInfo" />
    <!-- 转办 -->
    <assignee-modal :visible.sync="modalConfig.assigneeVisible" :processInfo="processInfo" />
    <!-- 退回 -->
    <rollback-modal :visible.sync="modalConfig.rollbackVisible" :processInfo="processInfo" />
    <!-- 加签 -->
    <add-multi-modal :visible.sync="modalConfig.addMultiVisible" :processInfo="processInfo" />
    <!-- 查到签上的人 -->
    <query-multi-user-modal :visible.sync="modalConfig.queryMultiUserVisible" :processInfo="processInfo" />
  </div>
</template>

<script>
import {
  delegateTask,
  revoke,
  deleteMulti,
} from "@/api/design";
import AgreenForm from "./AgreenForm";
import OrgPicker from "@/components/common/OrgPicker";
import CommentModal from './CommentModal';
import DelegateModal from './DelegateModal';
import AssigneeModal from './AssigneeModal';
import ResolveModal from './ResolveModal';
import RefuseModal from './RefuseModal';
import RollbackModal from './RollbackModal';
import AddMultiModal from './AddMultiModal';
import QueryMultiUserModal from './QueryMultiUserModal';

// 待我处理
// [同意][委派][委派人完成][拒绝][转办][退回][加签][减签][评论][查到签上的人]
const TODO_TASK_KEYS = ['agree', 'delegate', 'resolve', 'refuse', 'assignee', 'rollback', 'addMulti', 'deleteMulti', 'comments', 'queryMultiUsersInfo'];

// 我发起的
// [撤销] [评论]
const APPLY_TASK_KEYS = ['revoke', 'comments']

// 关于我的
// [撤销] [评论]
const DONE_TASK_KEYS = ['revoke', 'comments'];

// 根据类型 映射对应关系
const BUTTON_KEYS_MAP = {
  todoTask: TODO_TASK_KEYS,
  applyTask: APPLY_TASK_KEYS,
  doneTask: DONE_TASK_KEYS,
};

// 对应映射关系
const OPERATION_BUTTON_CONFIG = [
  {
    text: "同意",
    key: "agree",
  },
  {
    text: "委派",
    key: "delegate",
  },
  {
    text: "委派人完成",
    key: "resolve",
  },
  {
    text: "拒绝",
    key: "refuse",
  },
  {
    text: "撤销",
    key: "revoke",
  },
  {
    text: "转办",
    key: "assignee",
  },
  {
    text: "退回",
    key: "rollback",
  },
  {
    text: "加签",
    key: "addMulti",
  },
  {
    text: "查到签上的人",
    key: "queryMultiUsersInfo",
  },
  {
    text: "减签",
    key: "deleteMulti",
  },
  {
    text: "评论",
    key: "comments",
  },
];

export default {
  props: ["processInfo", "type"],
  components: {
    AgreenForm,
    OrgPicker,
    CommentModal,
    DelegateModal,
    AssigneeModal,
    ResolveModal,
    RefuseModal,
    RollbackModal,
    AddMultiModal,
    QueryMultiUserModal,
  },
  name: "ProcessForm",
  data() {
    return {
      modalConfig: {
        commentVisible: false,
        delegateVisible: false,
        assigneeVisible: false,
        resolveVisible: false,
        refuseVisible: false,
        rollbackVisible: false,
        addMultiVisible: false,
        queryMultiUserVisible: false
      },
      fromData: {
        comments: "同意",
        processInstanceId: "",
        taskId: "",
      },
      select: [],
    };
  },
  computed: {
    buttonConfig() {
      return OPERATION_BUTTON_CONFIG.filter(item => BUTTON_KEYS_MAP[this.type].includes(item.key))
    }
  },
  methods: {
    handleClickByType(type) {
      const METHOD_MAP = {
        agree: this.onAgree,
        delegate: this.onDelegateTask,
        resolve: this.onResolveTask,
        refuse: this.onRefuse,
        revoke: this.onRevoke,
        assignee: this.onAssignee,
        rollback: this.onRollback,
        addMulti: this.onAddMulti,
        queryMultiUsersInfo: this.onQueryMultiUsersInfo,
        deleteMulti: this.onDeleteMulti,
        comments: this.onComments,
      }
      METHOD_MAP[type]?.()
    },
    onSelected(select) {
      this.select.length = 0;
      select.forEach((val) => this.select.push(val));
      switch (this.selectType) {
        case delegateTask:
          break;

        default:
          break;
      }
    },
    onAgree() {
      this.$refs.AgreenForm.initFrom(this.processInfo, this.callback);
    },
    callback() {},
    onDelegateTask() {
      this.modalConfig.delegateVisible = true;
    },
    onResolveTask() {
      this.modalConfig.resolveVisible = true
    },
    onRefuse() {
      this.modalConfig.refuseVisible = true
    },
    onRevoke() {
      const data = { ...this.fromData, ...this.processInfo };

      revoke(data).then((res) => {
        console.log("同意res", res);
      });
    },

    onAssignee() {
      this.modalConfig.assigneeVisible = true
    },
    onRollback() {
      this.modalConfig.rollbackVisible = true;
    },
    onAddMulti() {
      this.modalConfig.addMultiVisible = true;
    },
    onQueryMultiUsersInfo() {
      this.modalConfig.queryMultiUserVisible = true;
    },
    onDeleteMulti() {
      const data = { ...this.fromData, ...this.processInfo };
      deleteMulti(data).then((res) => {
        console.log("同意res", res);
      });
    },
    // 添加评论
    onComments() {
      this.modalConfig.commentVisible = true;
    },
  },
};
</script>

<style lang="scss" scoped>
</style>