<template>
  <el-dialog title="委派人完成" :visible.sync="show" width="500px" v-bind="$attrs" v-on="$listeners" @close="handleCancel" :close-on-click-modal="false">
    <el-form v-loading="loading" ref="formRef" :model="formValue">
      <el-form-item prop="comments">
        <el-input type="textarea" v-model="formValue.comments" placeholder="意见" maxlength="255" rows="4"  show-word-limit />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button size="mini" @click="handleCancel">取 消</el-button>
      <el-button size="mini" type="primary" :loading="loading" @click="handleConfirm">提 交</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { resolveTask } from '@/api/design';

export default {
  name: 'ResolveModal',
  props: {
    // 是否显示
    visible: {
      type: Boolean,
      default: false
    },
    processInfo: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      formValue: {
        comments: ''
      },
    }
  },
  computed: {
    show: {
      get() {
        return this.visible
      },
      set(visible) {
        this.$emit('update:visible', visible)
      }
    }
  },
  methods: {
    // 确认操作
    handleConfirm() {
      this.$refs.formRef.validate(valid => {
        if(!valid) return;
        const params = {
          ...this.processInfo,
          ...this.formValue,
        }
        this.loading = true
        resolveTask(params).then(() => {
          this.$message.success('操作成功');
          this.handleCancel();
          this.$emit('success')
        }).finally(() => {
          this.loading = false
        })
      })
    },
    // 取消操作
    handleCancel() {
      this.$refs.formRef.resetFields();
      this.show = false;
    }
  }
}
</script>
