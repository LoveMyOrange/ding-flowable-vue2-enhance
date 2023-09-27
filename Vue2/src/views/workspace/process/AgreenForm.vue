<template>
  <WDialog title="审批" @ok="onOk" @cancel="onCancel" v-model="showDialog">
    <el-form ref="form" :model="formData" label-width="80px">
      <el-form-item label="输入意见">
        <el-input v-model="formData.comments"></el-input>
      </el-form-item>

      <el-form-item label="上传附件:">
        <FileUpload :value="fileList"></FileUpload>
      </el-form-item>

      <el-form-item label="签名画板" v-if="processInfo.signFlag">
        <vue-esign ref="esign2" :width="800" :height="300" />

        <el-button type="warning" @click="handleReset">清空画板</el-button>

        <el-button type="primary" @click="handleGenerate">生成图片</el-button>
      </el-form-item>
    </el-form>
  </WDialog>
</template>

<script>
import { agree } from "@/api/design";
import FileUpload from "@/views/workspace/process/FileUpload.vue";
export default {
  components: { FileUpload },
  props: {
    fileList: [],
  },
  data() {
    return {
      formData: {
        comments: "同意",
        signFlag: null,
      },
      processInfo: {},
      showDialog: false,
      signInfo: "",
    };
  },
  methods: {
    httpRequest() {},
    handleReset() {
      this.$refs["esign2"].reset(); //清空画布
    },
    handleGenerate() {
      this.$refs["esign2"]
        .generate()
        .then((res) => {
          // this.resultImg = res // 得到了签字生成的base64图片
          this.signInfo = res;
          console.log(res);
          this.showDialog = true;
          this.$message("生成成功");
        })
        .catch(() => {
          //  没有签名，点击生成图片时调用
          this.$message("未签名！");
        });
    },
    initFrom(processInfo, callback) {
      this.callback = callback;
      this.processInfo = processInfo;
      console.log("processInfo", processInfo);
      this.showDialog = true;
    },
    onOk() {
      let data = { ...this.fromData, ...this.processInfo };
      data.signInfo = this.signInfo;
      console.log("data", data);
      agree(data).then((res) => {
        console.log("onOk", res);
        this.showDialog = false;
        this.$message.success("审批成功");
        // this.callback()
      });
    },
    onCancel() {
      this.showDialog = false;
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
