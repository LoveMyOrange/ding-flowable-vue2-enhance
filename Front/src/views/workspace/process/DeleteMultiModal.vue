<template>
  <el-dialog
    title="减签"
    :visible.sync="show"
    width="500px"
    v-bind="$attrs"
    v-on="$listeners"
    @close="handleCancel"
    @opened="handleOpen"
  >
    <el-form
      v-loading="loading"
      ref="formRef"
      :model="formValue"
      :rules="rules"
    >
      <el-form-item label="给谁减签" prop="executionIds" required>
        <el-select v-model="formValue.executionIds" :multiple="true" placeholder="选择减签用户">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button size="mini" @click="handleCancel">取 消</el-button>
      <el-button
        size="mini"
        type="primary"
        :loading="loading"
        @click="handleConfirm"
        >确 定</el-button
      >
    </template>
  </el-dialog>
</template>

<script>
import { deleteMulti, queryMultiUsersInfo } from "@/api/design";

export default {
  name: "DeleteMultiModal",
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
      formValue: {
        executionIds: [],
      },
      rules: {
        executionIds: [
          { required: true, message: "请选择减签用户", trigger: "change" },
        ],
      },
      options: [],
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
    // 确认操作
    handleConfirm() {
      this.$refs.formRef.validate((valid) => {
        if (!valid) return;
        const params = {
          ...this.processInfo,
          ...this.formValue,
        }
        this.loading = true;
        deleteMulti(params)
          .then(() => {
            this.$message.success("操作成功");
            this.handleCancel();
            this.$emit("success");
          })
          .finally(() => {
            this.loading = false;
          });
      });
    },
    // 取消操作
    handleCancel() {
      this.options = [];
      this.$refs.formRef.resetFields();
      this.show = false;
    },
    // 打开弹框操作
    handleOpen() {
      const { processInstanceId, taskId } = this.processInfo;
      queryMultiUsersInfo({ processInstanceId, taskId }).then((res) => {
        this.options = (res.data?.result || []).map((item) => {
          return {
            label: item.userName || item.userId,
            value: item.executionId,
          };
        });
      });
    },
  },
};
</script>
