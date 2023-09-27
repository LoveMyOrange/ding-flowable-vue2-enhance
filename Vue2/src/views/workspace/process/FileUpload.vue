<template>
  <el-upload
    class="file-upload"
    withCredentials
    action="http://106.13.16.28:10000/wflow/res"
    :on-success="handleSuccess"
    :on-remove="handleRemove"
    :data="params"
    :before-upload="beforeAvatarUpload"
    show-file-list
    :limit="5"
  >
    <el-button round size="small">
      <i class="el-icon-link"></i>
      选择文件
    </el-button>
    <div slot="tip" class="el-upload__tip">添加附件 | 单个附件不超过100MB</div>
  </el-upload>
</template>

<script>
export default {
  name: "FileUpload",
  props: {
    value: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      dialogImageUrl: "",
      dialogVisible: false,
      params: {
        isImg: false
      }
    };
  },
  methods: {
    beforeAvatarUpload(file) {
      if(this.value.length === 5) {
        this.$message.error('最多上传5个附件!');
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isLt2M) {
        this.$message.error('附件大小不能超过100MB!');
        return false
      }
      return isLt2M;
    },
    handleSuccess(_, __, fileList) {
      this.handleUpdateValue(fileList)
      this.$message.success('上传成功')
    },
    handleRemove(_, fileList) {
      this.handleUpdateValue(fileList)
    },
    handleUpdateValue(fileList) {
      const files = fileList.map(file => file.response);
      this.$emit('input', files)
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    }
  },
};
</script>

<style lang="less" scoped>
.image-upload {
}
</style>