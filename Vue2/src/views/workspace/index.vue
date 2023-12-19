<template>
  <div class="workspace">
    <div class="workspace__header">
      <el-button icon="el-icon-back" class="back" type="info" size="mini" plain @click="$router.push('/')">返回主页</el-button>
    </div>
    <div class="workspace__tabs">
      <route-tab :routes="routes" type="border-card">
        <router-view></router-view>
      </route-tab>
    </div>
  </div>
</template>

<script>
import RouteTab from '@/components/common/route-tab'

export default {
  name: 'workSpace',
  components: {
    RouteTab
  },
  data() {
    return {
      routes: [
        {
          label: '审批列表',
          path: '/workspace/approval'
        },
        {
          label: '待我处理',
          path: '/workspace/todo'
        },
        {
          label: '我发起的',
          path: '/workspace/apply'
        },
        {
          label: '关于我的',
          path: '/workspace/about'
        }
        ,
                {
                  label: '抄送我的',
                  path: '/workspace/cc'
                }
        ,
                {
                  label: '数据管理',
                  path: '/workspace/submit'
                }                
      ]
    }
  },
  methods: {
    handleClick(tab) {
      this.$router.push({
        path: tab.name
      })
    }
  }
}
</script>

<style lang="less" scoped>
.workspace {
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
    .route-tab {
      /deep/ .el-tabs__content {
        height: calc(100vh - 150px);
        overflow-y: auto;
      }
    }
  }
}
</style>
