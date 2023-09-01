<template>
  <el-dialog
    destroy-on-close
    title="退回到之前节点"
    :visible.sync="show"
    width="500px"
    v-bind="$attrs"
    v-on="$listeners"
    @close="handleCancel"
    :close-on-click-modal="false"
    @opened="handleOpen"
  >
    <el-form
      v-loading="loading"
      ref="formRef"
      :model="formValue"
      :rules="rules"
    >
      <el-form-item label="回退节点" prop="rollbackId" required>
        <el-select
          size="small"
          v-model="formValue.rollbackId"
          placeholder="选择要回退到的节点"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="comments">
        <el-input
          type="textarea"
          v-model="formValue.comments"
          placeholder="退回意见"
          maxlength="255"
          rows="4"
          show-word-limit
        />
      </el-form-item>
      <el-form-item>
        <image-upload v-model="formValue.imageList" />
      </el-form-item>
      <el-form-item>
        <file-upload v-model="formValue.fileList" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button size="mini" @click="handleCancel">取 消</el-button>
      <el-button
        size="mini"
        type="primary"
        :loading="loading"
        @click="handleConfirm"
        >提 交</el-button
      >
    </template>
  </el-dialog>
</template>

<script>
import { rollback, getRollbackNodes } from "@/api/design";
import ImageUpload from "./ImageUpload";
import FileUpload from "./FileUpload";

export default {
  name: "RollbackModal",
  components: { ImageUpload, FileUpload },
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
        rollbackId: "",
        comments: "",
        imageList: [],
        fileList: [],
      },
      options: [],
      rules: {
        rollbackId: [
          {
            required: true,
            message: "请选择要回退到的节点",
            trigger: "change",
          },
        ],
      },
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
        const { imageList, fileList, ...restParams } = this.formValue;
        const params = {
          ...this.processInfo,
          ...restParams,
          attachments: [...imageList, ...fileList],
        };
        this.loading = true;
        rollback(params)
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
      this.$refs.formRef.resetFields();
      this.formValue = {
        rollbackId: "",
        comments: "",
        imageList: [],
        fileList: [],
      };
      this.options = [];
      this.show = false;
    },
    // 打开弹框操作
    handleOpen() {
      const { processInstanceId, taskId } = this.processInfo;
      getRollbackNodes({ processInstanceId, taskId }).then((res) => {
        const nodes = res.data.result || {};
        this.options = Object.keys(nodes).map((key) => {
          return {
            label: nodes[key],
            value: key,
          };
        });
      });
    },
  },
};
</script>
