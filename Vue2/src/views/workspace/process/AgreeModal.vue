<template>
  <el-dialog
    destroy-on-close
    title="同意"
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
      <el-form-item prop="comments" required>
        <el-input
          type="textarea"
          v-model="formValue.comments"
          placeholder="审批意见"
          maxlength="255"
          rows="4"
          show-word-limit
        />
      </el-form-item>
      <el-form-item v-if="processInfo.signFlag" label="签名画板" prop="signInfo" :required="processInfo.signFlag">
        <vue-esign ref="esignRef" :width="800" :height="300" />
        <el-button
          v-if="!formValue.signInfo"
          size="mini"
          type="primary"
          @click="handleGenerate"
          >生成图片</el-button
        >
        <el-button size="mini" type="warning" @click="handleReset"
          >清空画板</el-button
        >
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
  </el-dialog>
</template>

<script>
import { agree } from "@/api/design";
import ImageUpload from "./ImageUpload";
import FileUpload from "./FileUpload";

export default {
  name: "AgreeModal",
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
        comments: "",
        imageList: [],
        fileList: [],
        signInfo: "",
      },
      rules: {
        comments: [
          { required: true, message: "请填写审批意见", trigger: "blur" },
        ],
        signInfo: [{ required: this.processInfo.signFlag, message: "请签名", trigger: "blur" }],
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
    handleReset() {
      //清空画布
      this.$refs.esignRef.reset();
      this.formValue.signInfo = "";
    },
    handleGenerate() {
      this.$refs.esignRef
        .generate()
        .then((res) => {
          // 得到了签字生成的base64图片
          this.formValue.signInfo = res;
          this.$message("生成成功");
        })
        .catch(() => {
          //  没有签名，点击生成图片时调用
          this.$message("未签名！");
        });
    },
    // 确认操作
    handleConfirm() {
      this.$refs.formRef.validate((valid) => {
        if (!valid) return;
        const { imageList, fileList, signInfo, ...restParams } = this.formValue;
        const params = {
          ...this.processInfo,
          ...restParams,
          attachments: [...imageList, ...fileList],
        };
        if(this.processInfo.signFlag) {
          params.signInfo = signInfo;
        }
        this.loading = true;
        agree(params)
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
        comments: "",
        imageList: [],
        fileList: [],
        signInfo: "",
      };
      this.show = false;
    },
  },
};
</script>
