<template>
  <WDialog  title="审批" @ok="onOk" @cancel="onCancel" v-model="showDialog" >
    <el-form ref="form" :model="formData" label-width="80px">
        <el-form-item label="审批内容">
            <el-input v-model="formData.comments"></el-input>
        </el-form-item>
    </el-form>
   </WDialog>
</template>

<script>
import { agree } from '@/api/design'

export default {
    data(){
        return{
            formData:{ 
                comments:"同意"
            },
            processInfo:{},
            showDialog:false,
        }
    },
    methods:{
        initFrom(processInfo,callback){
            this.callback = callback
           this.processInfo = processInfo
           this.showDialog = true
        },
        onOk(){
          
            const data =   {...this.fromData,... this.processInfo,}
            agree(data).then(res=>{
                console.log("onOk",res)
                this.showDialog =false
                this.$message.success("审批成功")
                // this.callback()
            }) 
        },
        onCancel(){
            this.showDialog = false
        }
    }
}
</script>

<style lang="scss" scoped>

</style>