<template>
  <div class="process-view">
    <div class="process-view__header">
      <el-button
        icon="el-icon-back"
        class="back"
        type="info"
        size="mini"
        plain
        @click="$router.go(-1)"
        >返回主页</el-button
      >
    </div>
    <div class="process-view__tabs" v-loading="loading">
      <!-- <el-button type="success"
        >下方按钮前端交互写的比较简陋, 相关入参前端传递的也有问题,前端水平有限,
        大家可以PostMan去测试,参数可以可以参照swagger文档来传递</el-button
      ><br />
      <el-button type="success"
        >3个Tab页的数据都是从instanceInfo接口返回回来的,目前只做了第一个和第三个tab页,第二个Tab页
        暂时没有前端联调</el-button
      ><br />
      <el-button type="success"
        >待办页面应该展现如下按钮:
        [同意][委派][委派人完成][拒绝][转办][退回][加签][减签][评论][查到签上的人]</el-button
      ><br />
      <el-button type="success"
        >我发起页面应该展现如下按钮: [撤销] [评论] </el-button
      ><br />
      <el-button type="success"
        >关于我的页面应该展现如下按钮: [撤销] [评论]</el-button
      ><br /> -->
      <el-tabs type="border-card">
        <el-tab-pane label="表单详情">
          <process-form :processInfo="processInfo" :type="type"></process-form>
          <form-render
            class="process-form"
            mode="PC"
            ref="form"
            :forms="forms"
            v-model="formData"
          />
        </el-tab-pane>
        <el-tab-pane label="操作记录">
          待开发
        </el-tab-pane>
        <el-tab-pane label="流程图">
          <process-diagram-viewer />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { getProcessInstanceInfo } from "@/api/design";
import FormRender from "@/views/common/form/FormRender";
import { flatFormItem } from "../form";
import ProcessDiagramViewer from "@/views/admin/layout/ProcessDiagramViewer";
import ProcessForm from "./ProcessForm";
export default {
  components: { FormRender, ProcessDiagramViewer, ProcessForm },
  data() {
    return {
      loading: false,
      processInstanceId: "",
      taskId: "",
      form: null,
      formData: {},
      currentNode: {},
      processInfo: "",
    };
  },
  computed: {
    forms() {
      return this.$store.state.design.formItems;
    },
    type() {
      return this.$route.query.type;
    },
  },
  methods: {
    getProcessInfo() {
      this.loading = true;
      getProcessInstanceInfo(this.processInstanceId, this.taskId)
        .then((rsp) => {
          console.log("流程详情", rsp.data);
          const form = { ...rsp.data.result.processTemplates };
          const currentNode = { ...rsp.data.result?.currentNode };
          const formData = { ...rsp.data.result.formData };

          form.logo = JSON.parse(form.logo);
          form.settings = JSON.parse(form.settings);
          form.process = JSON.parse(form.process);

          const perms = currentNode?.props?.formPerms || [];

          // 表单项 从json转换为数组 用于渲染表单 扁平化处理 去除了spanLayout
          const formItems = flatFormItem(JSON.parse(form.formItems));
          // item显示状态映射关系
          const itemStatusMap = new Map(perms.map((it) => [it.id, it.perm]));
          const items = formItems
            .map((item) => {
              // perm说明 只读R  编辑E  隐藏H
              const perm = itemStatusMap.get(item.id);
              if (perm === "E") {
                return item;
              } else if (perm === "R") {
                item.props.readerMode = true;
                return item;
              } else if (perm === "H") {
                return undefined;
              }
            })
            .filter(Boolean);

          form.formItems = items;

          this.$store.state.design = form;
          this.$store.state.endList = rsp.data.result.endList;
          this.$store.state.runningList = rsp.data.result.runningList;
          this.$store.state.noTakeList = rsp.data.result.noTakeList;

          this.currentNode = currentNode;
          this.formData = formData;
          this.processInfo.formData = formData;
          this.processInfo.signFlag = rsp.data.result.signFlag;
          this.form = form;
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
  beforeMount() {
    this.processInstanceId = this.$route.query.processInstanceId;
    this.taskId = this.$route.query.taskId;
    const str = sessionStorage.getItem("user");
    const currentUserInfo = JSON.parse(str);
    currentUserInfo.id = currentUserInfo.id.toString();
    this.processInfo = {
      processInstanceId: this.processInstanceId,
      taskId: this.taskId,
      signFlag: null,
      currentUserInfo: currentUserInfo,
    };
  },
  mounted() {
    this.getProcessInfo();
  },
};
</script>

<style lang="less" scoped>
.process-view {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;

  &__header {
    margin-bottom: 10px;
  }

  &__tabs {
    flex: 1;
    /deep/ .el-tabs__content {
      height: calc(100vh - 150px);
      overflow-y: auto;
    }
  }
}
</style>
