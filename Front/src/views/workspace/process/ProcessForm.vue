<template>
  <div>
    <!-- <el-input v-model="fromData.comments" placeholder="请输入审批内容"></el-input> -->
    <el-row :gutter="10">
      <el-col :span="24">
        <el-button type="primary" @click="onAgree">同意</el-button>
        <el-button type="primary" @click="onDelegateTask">委派</el-button>
        <el-button type="primary" @click="onResolveTask">委派人完成</el-button>
        <el-button type="primary" @click="onRefuse">拒绝</el-button>
        <el-button type="primary" @click="onRevoke">撤销</el-button>
        <el-button type="primary" @click="onAssignee">转办</el-button>
        <el-button type="primary" @click="onrollback">退回</el-button>
        <el-button type="primary" @click="onAddMulti">加签</el-button>
        <el-button type="primary" @click="onQueryMultiUsersInfo"
          >查到签上的人</el-button
        >
        <el-button type="primary" @click="onDeleteMulti">减签</el-button>
        <el-button type="primary" @click="onComments">评论</el-button>
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
  </div>
</template>

<script>
import {
  delegateTask,
  resolveTask,
  refuse,
  revoke,
  assignee,
  rollback,
  addMulti,
  queryMultiUsersInfo,
  deleteMulti,
  comments,
} from "@/api/design";
import AgreenForm from "./AgreenForm";
import OrgPicker from "@/components/common/OrgPicker";

export default {
  props: ["processInfo"],
  components: {
    AgreenForm,
    OrgPicker,
  },

  name: "ProcessForm",
  data() {
    return {
      fromData: {
        comments: "同意",
        processInstanceId: "",
        taskId: "",
      },
      select:[]
    };
  },
  methods: {
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
      this.$refs.AgreenForm.initFrom(this.processInfo,this.callback);
    },
    callback(){

    },
    onDelegateTask() {
      this.$refs.orgPicker.show();
     this.selectType = "delegateTask"
    //   const data = { ...this.fromData, ...this.processInfo };

    //   delegateTask(data).then((res) => {
    //     console.log("同意res", res);
    //     this.$message.success("审批成功");
    //   });
    },

    onResolveTask() {
      const data = { ...this.fromData, ...this.processInfo };

      resolveTask(data).then((res) => {
        console.log("同意res", res);
      });
    },
    onRefuse() {
      const data = { ...this.fromData, ...this.processInfo };

      refuse(data).then((res) => {
        console.log("同意res", res);
      });
    },

    onRevoke() {
      const data = { ...this.fromData, ...this.processInfo };

      revoke(data).then((res) => {
        console.log("同意res", res);
      });
    },

    onAssignee() {
      const data = { ...this.fromData, ...this.processInfo };

      assignee(data).then((res) => {
        console.log("同意res", res);
      });
    },

    onrollback() {
      const data = { ...this.fromData, ...this.processInfo };

      rollback(data).then((res) => {
        console.log("同意res", res);
      });
    },

    onAddMulti() {
      const data = { ...this.fromData, ...this.processInfo };

      addMulti(data).then((res) => {
        console.log("同意res", res);
      });
    },

    onQueryMultiUsersInfo() {
      const data = { ...this.fromData, ...this.processInfo };

      queryMultiUsersInfo(data).then((res) => {
        console.log("同意res", res);
      });
    },
    onDeleteMulti() {
      const data = { ...this.fromData, ...this.processInfo };
      deleteMulti(data).then((res) => {
        console.log("同意res", res);
      });
    },
    onComments() {
      const data = { ...this.fromData, ...this.processInfo };
      comments(data).then((res) => {
        console.log("同意res", res);
      });
    },
  },
};
</script>

<style lang="scss" scoped>
</style>