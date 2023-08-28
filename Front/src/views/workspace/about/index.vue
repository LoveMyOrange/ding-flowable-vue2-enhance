<template>
  <div>
    <el-table
      :data="tableData"
      :border="true"
      v-loading="loading"
      stripe
      @row-dblclick="handleRowDbClick"
    >
      <el-table-column type="index" label="#"  />
      <el-table-column
        prop="processDefinitionName"
        label="流程类型"
        width="200"
      />
      <el-table-column prop="startUser.name" label="发起人" width="200" />
      <el-table-column prop="taskName" label="任务节点名" width="200" />
      <el-table-column
        prop="startTime"
        label="任务开始时间"
        :formatter="formatTime"
        width="200"
      />
      <el-table-column
        prop="endTime"
        label="处理完成时间"
        :formatter="formatTime"
        width="200"
      />
      <el-table-column prop="duration" label="耗时" width="200" />
      <el-table-column
        prop="businessStatus"
        label="当前状态"
        :formatter="formatBusinessStatus"
        width="200"
      />
      <el-table-column label="操作" fixed="right" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleRowDbClick(scope.row)"
            >处理</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="text-align: right; margin-top: 10px;"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50]"
      :current-page.sync="pageNo"
      :page-size="pageSize"
      @current-change="getDoneList"
      :total="total"
    />
  </div>
</template>
<script>
import { doneList } from "@/api/design";
import { formatTime, formatBusinessStatus } from "../form";

export default {
  data() {
    return {
      tableData: [],
      total: 0,
      pageNo: 1,
      pageSize: 50,
      loading: false,
    };
  },
  methods: {
    formatTime,
    formatBusinessStatus,
    getDoneList() {
      const str = sessionStorage.getItem("user");
      const currentUserInfo = JSON.parse(str);
      currentUserInfo.id = currentUserInfo.id.toString();
      this.loading = true;
      doneList({
        currentUserInfo,
        pageNo: this.pageNo,
        pageSize: this.pageSize,
      })
        .then((rsp) => {
          this.tableData = rsp.data.result.records;
          this.total = rsp.data.result.total;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handleRowDbClick(row) {
      this.$router.push({
        path: "/workspace/process/instance/tabs",
        query: {
          processInstanceId: row.processInstanceId,
          taskId: row.taskId,
          type: "doneTask",
        },
      });
    },
  },
  mounted() {
    this.getDoneList();
  },
};
</script>
