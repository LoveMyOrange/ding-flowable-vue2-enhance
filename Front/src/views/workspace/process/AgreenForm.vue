<template>
  <WDialog title="审批" @ok="onOk" @cancel="onCancel" v-model="showDialog">
    <el-form ref="form" :model="formData" label-width="80px">
      <el-form-item label="输入意见">
        <el-input v-model="formData.comments"></el-input>
      </el-form-item>

      <el-form-item label="上传附件:">
        <el-upload ref="upfile"
                   style="display: inline"
                   :auto-upload="false"
                   :on-change="handleChange"
                   :file-list="fileList"
                   action="#">
          <el-button type="primary">选择文件</el-button>
        </el-upload>
      </el-form-item>

      <el-form-item label="签名画板" v-if="formData.signFlag">
        <vue-esign ref="esign" :width="800" :height="300" :isCrop="isCrop" :lineWidth="lineWidth" :lineColor="lineColor"
                   :bgColor.sync="bgColor"/>

        <button @click="handleReset">清空画板</button>

        <button @click="handleGenerate">生成图片</button>
      </el-form-item>
    </el-form>
  </WDialog>
</template>

<script>
import {agree} from '@/api/design'

export default {
  data() {
    return {
      formData: {
        comments: "同意",
        signFlag: null,
      },
      processInfo: {},
      showDialog: false,
      file: null,

    }
  },
  methods: {
    handleReset() {
      this.$refs['esign'].reset() //清空画布
    },
    upload() {
      let fd = new FormData();
      fd.append("name", this.name);
      this.fileList.forEach(item => {
        //文件信息中raw才是真的文件
        fd.append("files", item.raw);
        //console.log(item.raw)
      })
      this.$axios.post('/uploadUi', fd).then(res => {
        if (res.data.code === 200) {
          //console.log(res);
          this.file = res;
          this.$message('上传成功')
        } else {
          this.$message('失败')
        }
      })
    },
    handleGenerate() {
      this.$refs['esign'].generate().then(res => {
       // this.resultImg = res // 得到了签字生成的base64图片
        this.$message("生成签名成功")
      }).catch(err => { //  没有签名，点击生成图片时调用
        this.$message("未签名！")
      })
    },
    initFrom(processInfo, callback) {
      this.callback = callback
      this.processInfo = processInfo
      this.showDialog = true
    },
    onOk() {
      const data = {...this.fromData, ...this.processInfo,}
      agree(data).then(res => {
        console.log("onOk", res)
        this.showDialog = false
        this.$message.success("审批成功")
        // this.callback()
      })
    },
    onCancel() {
      this.showDialog = false
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
