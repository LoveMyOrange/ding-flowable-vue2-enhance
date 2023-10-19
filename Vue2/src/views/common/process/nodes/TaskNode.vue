<template>
  <node :title="config.name" :show-error="showError" :content="content" :error-info="errorInfo"
        @selected="$emit('selected')" @delNode="$emit('delNode')" @insertNode="type => $emit('insertNode', type)"
        placeholder="请设置办理人" :header-bgc="headerBgc" header-icon="el-icon-s-check"/>
</template>

<script>
import Node from './Node'

export default {
  name: "TaskNode",
  props:{
    config:{
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: {Node},
  data() {
    return {
      showError: false,
      errorInfo: '',
    }
  },
  computed:{
    headerBgc() {
      if (this.$store.state.diagramMode === 'viewer') {
        return this.config.props.headerBgc
      } else {
        return '#e6b039'
      }
    },
    content(){
      const config = this.config.props
      switch (config.assignedType){
        case "ASSIGN_USER":
          if (config.assignedUser.length > 0){
            let texts = []
            config.assignedUser.forEach(org => texts.push(org.name))
            return String(texts).replaceAll(',', '、')
          }else {
            return '请指定办理人'
          }
        case "SELF":
          return '发起人自己'
        case "SELF_SELECT":
          return config.selfSelect.multiple ? '发起人自选多人':'发起人自选一人'
        case "LEADER_TOP":
          return '多级主管依次办理'
        case "LEADER":
          return config.leader.level > 1 ? '发起人的第 ' + config.leader.level + ' 级主管' : '发起人的直接主管'
        case "FORM_USER":
          if (!config.formUser || config.formUser === ''){
            return '表单内联系人（未选择）'
          }else {
            let text = this.getFormItemById(config.formUser)
            if (text && text.title){
              return `表单（${text.title}）内的人员`
            }else {
              return '该表单已被移除😥'
            }
          }
        case "ROLE":
          if (config.role.length > 0){
            return String(config.role).replaceAll(',', '、')
          }else {
            return '指定角色（未设置）'
          }
          default: return '未知设置项😥'
      }
    }
  },
  methods: {
    getFormItemById(id){
      return this.$store.state.design.formItems.find(item => item.id === id)
    },
    //校验数据配置的合法性
    validate(err){
      try {
        this.showError = !this[`validate_${this.config.props.assignedType}`](err)

        if (this.config.props.nobody.handler === 'TO_USER' && this.config.props.nobody.assignedUser.length === 0) {
          this.errorInfo = '办理人为空时， 转交给指定人员：【请指定一个具体的人】'
          err.push('办理人为空时， 转交给指定人员：【请指定一个具体的人】')
          this.showError = true
        }

        return this.showError
      } catch (e) {
        return true;
      }
    },
    validate_ASSIGN_USER(err){
      if(this.config.props.assignedUser.length > 0){
        return true;
      }else {
        this.errorInfo = '请指定办理人员'
        err.push(`${this.config.name} 未指定办理人员`)
        return false
      }
    },
    validate_SELF_SELECT(err){
      return true;
    },
    validate_LEADER_TOP(err){
      return true;
    },
    validate_LEADER(err){
      return true;
    },
    validate_ROLE(err){
      if (this.config.props.role.length <= 0){
        this.errorInfo = '请指定负责办理的系统角色'
        err.push(`${this.config.name} 未指定办理角色`)
        return false
      }
      return true;
    },
    validate_SELF(err){
      return true;
    },
    validate_FORM_USER(err){
     if (this.config.props.formUser === ''){
       this.errorInfo = '请指定表单中的人员组件'
       err.push(`${this.config.name} 办理人为表单中人员，但未指定`)
       return false
     }
      return true;
    },
    validate_REFUSE(err){
      return true;
    },
  }
}
</script>

<style scoped>

</style>
