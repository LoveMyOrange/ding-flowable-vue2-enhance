<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="24">
        <el-button size="small" type="primary" v-for="item in buttonConfig" :key="item.key" @click="handleClickByType(item.key)">{{ item.text }}</el-button>
      </el-col>
    </el-row>
    <!-- 同意 -->
    <agree-modal :visible.sync="modalConfig.agreeVisible" :processInfo="processInfo" /> 
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
    <!-- 撤销 -->
    <revoke-modal :visible.sync="modalConfig.revokeVisible" :processInfo="processInfo" />
    <!-- 减签 -->
    <delete-multi-modal :visible.sync="modalConfig.deleteMultiVisible" :processInfo="processInfo" />
  </div>
</template>

<script>
import AgreeModal from './AgreeModal';
import CommentModal from './CommentModal';
import DelegateModal from './DelegateModal';
import AssigneeModal from './AssigneeModal';
import ResolveModal from './ResolveModal';
import RefuseModal from './RefuseModal';
import RollbackModal from './RollbackModal';
import AddMultiModal from './AddMultiModal';
import QueryMultiUserModal from './QueryMultiUserModal';
import RevokeModal from './RevokeModal'
import DeleteMultiModal from './DeleteMultiModal';

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

// 对应按钮映射关系
const OPERATION_BUTTON_MAP = {
  agree: "同意",
  delegate: "委派",
  resolve: "委派人完成",
  refuse: "拒绝",
  revoke: "撤销",
  assignee: "转办",
  rollback: "退回",
  addMulti: "加签",
  queryMultiUsersInfo: "查到签上的人",
  deleteMulti: "减签",
  comments: "评论"
}

export default {
  props: ["processInfo", "type"],
  components: {
    CommentModal,
    DelegateModal,
    AssigneeModal,
    ResolveModal,
    RefuseModal,
    RollbackModal,
    AddMultiModal,
    QueryMultiUserModal,
    RevokeModal,
    DeleteMultiModal,
    AgreeModal
  },
  name: "ProcessForm",
  data() {
    return {
      modalConfig: {
        agreeVisible: false,
        commentVisible: false,
        delegateVisible: false,
        assigneeVisible: false,
        resolveVisible: false,
        refuseVisible: false,
        rollbackVisible: false,
        addMultiVisible: false,
        queryMultiUserVisible: false,
        revokeVisible: false,
        deleteMultiVisible: false
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
      return Object.keys(OPERATION_BUTTON_MAP).filter(key => BUTTON_KEYS_MAP[this.type].includes(key)).map(key => {
        return {
          text: OPERATION_BUTTON_MAP[key],
          key
        }
      })
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
    onAgree() {
      this.modalConfig.agreeVisible = true;
    },
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
      this.modalConfig.revokeVisible = true;
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
      this.modalConfig.deleteMultiVisible = true;
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