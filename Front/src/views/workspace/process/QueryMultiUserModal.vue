<template>
  <el-dialog
    title="查到签上的人"
    :visible.sync="show"
    width="500px"
    v-bind="$attrs"
    v-on="$listeners"
    @close="handleCancel"
    :close-on-click-modal="false"
    @opened="handleOpen"
  >
    <el-table v-loading="loading" :border="true" :data="dataList">
      <el-table-column v-for="(column, index) in columns" :key="index" v-bind="column" />
    </el-table>
    <template #footer>
      <el-button size="mini" @click="handleCancel">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { queryMultiUsersInfo } from "@/api/design";

export default {
  name: "QueryMultiUserModal",
  props: {
    // 是否显示
    visible: {
      type: Boolean,
      default: false,
    },
    processInfo: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      loading: false,
      columns: [
        {
          prop: "userId",
          label: "用户ID",
        },
        {
          prop: "userName",
          label: "用户名",
        },
      ],
      dataList: []
    };
  },
  computed: {
    show: {
      get() {
        return this.visible;
      },
      set(visible) {
        this.$emit("update:visible", visible);
      },
    },
  },
  methods: {
    // 取消操作
    handleCancel() {
      this.dataList = [];
      this.show = false;
    },
    // 打开弹框操作
    handleOpen() {
      const { processInstanceId, taskId } = this.processInfo;
      this.loading = true;
      queryMultiUsersInfo({ processInstanceId, taskId }).then((res) => {
        this.dataList = res.data.result || [];
      }).finally(() => {
        this.loading = false
      });
    },
  },
};
</script>
