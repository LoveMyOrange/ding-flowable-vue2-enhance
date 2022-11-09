<template>
  <div style="padding: 10px">
    <div>
      <el-button icon="el-icon-back" class="back" type="info" size="mini" plain @click="$router.go(-1)">返回主页</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-tabs type="border-card">
        <el-tab-pane label="表单详情">
          <process-form :processInfo="processInfo"></process-form>
          <form-render class="process-form" mode="PC" ref="form" :forms="forms" v-model="formData"/>
        </el-tab-pane>
        <el-tab-pane label="操作记录">

        </el-tab-pane>
        <el-tab-pane label="流程图">
          <process-diagram-viewer/>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>

</template>

<script>
import { getProcessInstanceInfo } from '@/api/design'
import FormRender from "@/views/common/form/FormRender";
import { flatFormItem } from '../form'
import ProcessDiagramViewer from "@/views/admin/layout/ProcessDiagramViewer";
import ProcessForm from "./ProcessForm"
export default {
  components: { FormRender, ProcessDiagramViewer ,ProcessForm},
  data() {
    return {
      processInstanceId: '',
      taskId: '',
      form: null,
      formData: {},
      currentNode: {},
      processInfo:""
    }
  },
  computed: {
    forms() {
      return this.$store.state.design.formItems;
    }
  },
  methods: {
    getProcessInfo() {
      getProcessInstanceInfo(this.processInstanceId, this.taskId).then(rsp => {
        console.log('流程详情', rsp.data)
        let form = rsp.data.result.processTemplates
   
        form.logo = JSON.parse(form.logo)
        form.settings = JSON.parse(form.settings)
        form.formItems = JSON.parse(form.formItems)
        form.process = JSON.parse(form.process)

        const formItems = flatFormItem(form.formItems)
        console.log('formItems 1', formItems)

        const perms = rsp.data.result?.currentNode?.props?.formPerms || []

        const map = new Map(perms.map(it => [it.id, it.perm]))
        const removeIndices = []
        for (let i = 0; i < formItems.length; i++) {
          const formItem = formItems[i]
          const perm = map.get(formItem.id)
          if (perm === 'E') {

          }
          else if (perm === 'R') {
            formItem.props.readerMode = true
          }
          else if (perm === 'H') {
            removeIndices.push(i)
          }
        }

        removeIndices.reverse().forEach(it => formItems.splice(it, 1))
        console.log('formItems 2', formItems)
        form.formItems = formItems

        this.$store.state.design = form
        this.$store.state.endList = rsp.data.result.endList
        this.$store.state.runningList = rsp.data.result.runningList
        this.$store.state.noTakeList = rsp.data.result.noTakeList

        this.formData = rsp.data.result.formData
        this.currentNode = rsp.data.result.currentNode
        this.processInfo.formData = this.formData
        this.form = form

      })
    },
  },
  beforeMount() {
    this.processInstanceId = this.$route.query.processInstanceId
    this.taskId = this.$route.query.taskId
    const str = sessionStorage.getItem("user")
      const currentUserInfo = JSON.parse(str)
      currentUserInfo.id = currentUserInfo.id.toString()
    this.processInfo = {
      processInstanceId: this.processInstanceId,
      taskId: this.taskId,
      currentUserInfo:currentUserInfo
    }
  },
  mounted() {
    this.getProcessInfo()
  }
}
</script>