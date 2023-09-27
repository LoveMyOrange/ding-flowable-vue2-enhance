<template>
  <el-dialog
    destroy-on-close
    title="转办"
    :visible.sync="show"
    width="500px"
    v-bind="$attrs"
    v-on="$listeners"
    @close="handleCancel"
  >
    <el-form
      v-loading="loading"
      ref="formRef"
      :model="formValue"
      :rules="rules"
    >
      <el-form-item label="转办人" prop="transferUserInfo" required>
        <el-button size="mini" @click="handleShowOrgPicker"
          ><i class="el-icon-user"></i> 选择人员</el-button
        >
        <el-tag
          v-if="transferUserInfoUserName"
          :style="{ marginLeft: '10px' }"
          size="small"
          >{{ transferUserInfoUserName }}</el-tag
        >
      </el-form-item>
      <el-form-item prop="comments">
        <el-input
          type="textarea"
          v-model="formValue.comments"
          placeholder="转办意见"
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
        >确 定</el-button
      >
    </template>
    <org-picker
      ref="orgPicker"
      title="请选择转办的人员"
      type="user"
      :selected="formValue.transferUserInfo ? [formValue.transferUserInfo] : []"
      @ok="handleSelectedOrg"
    />
  </el-dialog>
</template>

<script>
import OrgPicker from "@/components/common/OrgPicker";
import ImageUpload from "./ImageUpload";
import FileUpload from "./FileUpload";
import { assignee } from "@/api/design";

export default {
  name: "AssigneeModal",
  components: { OrgPicker, ImageUpload, FileUpload },
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
        comments: "",
        transferUserInfo: null,
        imageList: [],
        fileList: [],
      },
      rules: {
        transferUserInfo: [
          { required: true, message: "请选择转办人", trigger: "change" },
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
    // 用户名展示
    transferUserInfoUserName() {
      return this.formValue.transferUserInfo?.name || "";
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
        assignee(params)
          .then(() => {
            this.$message.success("转办成功");
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
        comments: "",
        transferUserInfo: null,
        imageList: [],
        fileList: [],
      };
      this.show = false;
    },
    // 显示用户选择器
    handleShowOrgPicker() {
      this.$refs.orgPicker.show();
    },
    // 选择用户确认操作
    handleSelectedOrg(selected) {
      if (!selected.length) {
        this.formValue.transferUserInfo = null;
        return;
      }
      const [item] = selected;
      if (`${item.id}` === this.processInfo.currentUserInfo.id) {
        this.$message.error("转办人不能给自己");
        return;
      }
      this.formValue.transferUserInfo = item;
    },
  },
};
</script>
