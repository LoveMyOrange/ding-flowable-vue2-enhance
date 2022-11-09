<template>

  <div>
    <el-table :data="tableData" border stripe @row-dblclick="handleRowDbClick">
      <el-table-column type="index" label="#" />
      <el-table-column prop="processDefinitionName" label="流程类型" width="200" />
      <el-table-column prop="startUser.name" label="发起人" />
      <el-table-column prop="startTime" label="提交时间" :formatter="formatTime" />
      <el-table-column prop="taskCreateTime" label="任务到达时间" :formatter="formatTime" />
      <el-table-column prop="currentActivityName" label="当前节点" />
      <el-table-column prop="businessStatus" label="当前状态" :formatter="formatBusinessStatus" />
      <el-table-column  label="操作" >
        <template  slot-scope="scope">
          <el-button type="primary"  @click="handleRowDbClick(scope.row)">处理</el-button>
                  </template>
      </el-table-column>
    </el-table>
    <el-pagination layout="prev, pager, next, total" :current-page.sync="pageNo" @current-change="getTodoList" :total="total" />
  </div>
</template>
<script>
import { todoList } from "@/api/design"
import { formatTime, formatBusinessStatus } from '../form'

export default {
  data() {
    return {
      tableData: [],
      total: 0,
      pageNo: 1,
      pageSize: 50,
    }
  },
  methods: {
    getTodoList() {
      const str = sessionStorage.getItem("user")
      const currentUserInfo = JSON.parse(str)
      currentUserInfo.id = currentUserInfo.id.toString()

      todoList({
        currentUserInfo,
        pageNo: this.pageNo,
        pageSize: this.pageSize,
      }).then(rsp => {
        this.tableData = rsp.data.result.records
        this.total = rsp.data.result.total
      })
    },
    formatTime, formatBusinessStatus,
    handleRowDbClick(row) {

      this.$router.push({
        path: '/workspace/process/instance/tabs',
        query: {
          processInstanceId: row.processInstanceId,
          taskId: row.taskId,
        }
      })
    },
  },
  mounted() {
    this.getTodoList()
  }
}

</script>