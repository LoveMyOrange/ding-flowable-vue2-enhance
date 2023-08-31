<template>
  <div>
    <el-upload
      class="image-upload"
      withCredentials
      action="http://106.13.16.28:10000/wflow/res"
      list-type="picture-card"
      :on-success="handleSuccess"
      :on-remove="handleRemove"
      :data="params"
      :on-preview="handlePictureCardPreview"
      :before-upload="beforeAvatarUpload"
      accept="image/*"
    >
      <i class="el-icon-plus"></i>
      <div slot="tip" class="el-upload__tip">添加图片 | 每张图不超过10MB</div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ImageUpload",
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
        isImg: true
      }
    };
  },
  methods: {
    beforeAvatarUpload(file) {
      const isImg = ['image/png', 'image/jpeg','image/jpg'].includes(file.type);
      const isLt2M = file.size / 1024 / 1024 < 10;

      if (!isImg) {
        this.$message.error('只能是图片格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过10MB!');
      }
      return isImg && isLt2M;
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
  /deep/ .el-upload--picture-card {
    width: 80px;
    height: 80px;
    line-height: 87px;
  }

  /deep/ .el-upload-list__item {
    width: 80px;
    height: 80px;
  }

}
</style>