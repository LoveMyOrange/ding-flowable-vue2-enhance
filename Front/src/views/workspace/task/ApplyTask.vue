<template>

  <div>
    <el-table :data="tableData" :border="true" v-loading="loading" stripe @row-dblclick="handleRowDbClick">
      <el-table-column type="index" label="#" />
      <el-table-column prop="processDefinitionName" label="流程类型" width="200" />
      <el-table-column prop="startUser.name" label="发起人" />
      <el-table-column prop="startTime" label="提交时间" :formatter="formatTime" />
      <el-table-column prop="endTime" label="结束时间" :formatter="formatTime" />
      <el-table-column prop="currentActivityName" label="当前节点" />
      <el-table-column prop="businessStatus" label="审批状态" :formatter="formatBusinessStatus" />
      <el-table-column prop="duration" label="已耗时"  />
      <el-table-column  label="操作" >
        <template  slot-scope="scope">
        <el-button type="primary"  @click="handleRowDbClick(scope.row)">处理</el-button>
        </template>
      </el-table-column>

    </el-table>
    <el-pagination layout="prev, pager, next, total" :current-page.sync="pageNo" @current-change="getApplyList" :total="total" />

  </div>
</template>
<script>
import { applyList } from "@/api/design"
import { formatTime, formatBusinessStatus } from '../form'


export default {
  data() {
    return {
      tableData: [],
      total: 0,
      pageNo: 1,
      pageSize: 50,
      loading: false,
    }
  },
  methods: {
    formatTime, formatBusinessStatus,
    getApplyList() {
      const str = sessionStorage.getItem("user")
      const currentUserInfo = JSON.parse(str)
      currentUserInfo.id = currentUserInfo.id.toString()
      this.loading = true;
      applyList({
        currentUserInfo,
        pageNo: this.pageNo,
        pageSize: this.pageSize,
      }).then(rsp => {
        this.tableData = rsp.data.result.records
        this.total = rsp.data.result.total
      })
      .finally(() => {
        this.loading = false
      })
    },
    handleRowDbClick(row) {

      this.$router.push({
        path: '/workspace/process/instance/tabs',
        query: {
          processInstanceId: row.processInstanceId
        }
      })
    },
  },
  mounted() {
    this.getApplyList()
  }
}

</script>
