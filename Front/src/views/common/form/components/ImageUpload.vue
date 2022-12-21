<template>
  <div>

    <div v-if="mode === 'DESIGN'">
      <div class="design">
        <i class="el-icon-plus"></i>
      </div>
      <p>{{ placeholder }} {{ sizeTip }}</p>
    </div>
 
    <div v-else>
      <el-upload
        :file-list="_value"
        action="#"
        :limit="maxSize"
        with-credentials
        :multiple="maxSize > 0"
        :data="uploadParams"
        list-type="picture-card"

        :before-upload="beforeUpload"

      >
  
        <i slot="default" class="el-icon-plus"></i>
        <div slot="file" slot-scope="{ file }">
          <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
          <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
              <i class="el-icon-zoom-in"></i>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleDownload(file)">
              <i class="el-icon-download"></i>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
              <i class="el-icon-delete"></i>
            </span>
          </span>
        </div>

        <div slot="tip" class="el-upload__tip">{{ placeholder }} {{ sizeTip }}</div>
      </el-upload>
    </div>
  </div>
</template>

<script>
import componentMinxins from '../ComponentMinxins';
import { upLoadFileApi, downLoadFileApi } from '@/api/design';
import {  downloadFileBlob } from '@/utils/index';

export default {
  mixins: [componentMinxins],
  name: 'ImageUpload',
  components: {},
  props: {
    value: {
      type: Array,
      default: () => {
        return [];
      },
    },
    placeholder: {
      type: String,
      default: '请选择图片',
    },
    maxSize: {
      type: Number,
      default: 5,
    },
    maxNumber: {
      type: Number,
      default: 10,
    },
    enableZip: {
      type: Boolean,
      default: true,
    },
  },
  computed: {
    sizeTip() {
      return this.maxSize > 0 ? `| 每张图不超过${this.maxSize}MB` : '';
    },
  },
  data() {
    return {
      disabled: false,
      uploadParams: {},
    };
  },
  methods: {
       // 覆盖默认的上传行为
       requestUpload() {

       },
    beforeUpload(file) {
      const alows = ['image/jpeg', 'image/png', 'image/gif', 'image/jpg'];
      if (alows.indexOf(file.type) === -1) {
        this.$message.warning('存在不支持的图片格式');
      } else if (this.maxSize > 0 && file.size / 1024 / 1024 > this.maxSize) {
        this.$message.warning(`单张图片最大不超过 ${this.maxSize}MB`);
      } else {
         //上传文件的需要formdata类型;所以要转
        let FormDatas = new FormData();
        FormDatas.append('file', file);
        upLoadFileApi(FormDatas).then(res => {
        console.log('uploadFile', res);

        if (res.data.result) {
            
            this._value.push(res.data.result); //成功过后手动将文件添加到展示列表里
            console.log("   {{_value}}",this._value)
            this.$emit('input', this._value);
          }
        });
        return true;
      }
      return false;
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      console.log(file);
    },
    handleDownload(file) {
        //上传文件的需要formdata类型;所以要转
      let FormDatas = new FormData();
      FormDatas.append('name', file.name);
      downLoadFileApi(FormDatas).then(res => {
        if (res.data) {
          downloadFileBlob(res.data,file.name)
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
.design {
  i {
    padding: 10px;
    font-size: xx-large;
    background: white;
    border: 1px dashed #8c8c8c;
  }
}
/deep/ .el-upload--picture-card {
  width: 80px;
  height: 80px;
  line-height: 87px;
}
/deep/ .el-upload-list__item {
  width: 80px;
  height: 80px;
  .el-upload-list__item-actions {
    & > span + span {
      margin: 1px;
    }
  }
}
</style>
