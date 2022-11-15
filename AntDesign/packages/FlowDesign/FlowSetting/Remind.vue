<template>
  <div class="flow-setting-module">
    <div class="flow-setting-content">
      <div class="flow-setting-block">
        <div class="flow-block-title">提醒填写</div>
        <div class="flow-block-desc">通过设置推送提醒指定成员填写表单</div>
        <div class="flow-block-content">
          <a-row type="flex" :gutter="[16, 16]">
            <a-col v-for="(item, i) in items" :key="i" :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <a-card hoverable class="w-fill" @click="setSetting(item)">
                <div class="flowSetting-card-header">
                  <div class="flowSetting-card-header-title">
                    <img :src="settingBaseIcon" />
                    <span>{{ item.name }}</span>
                  </div>
                  <div @click.stop>
                    <!-- 阻止a-switch冒泡事件, 在父添加@click.stop-->
                    <a-icon type="delete" theme="filled" />
                  </div>
                </div>
                <div class="flowSetting-card-context">
                  <!--  <a-form :form="form" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
                    <a-form-item label="Note">
                      
                    </a-form-item>
                    <a-form-item label="Gender">
                      
                    </a-form-item>
                    <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
                     
                    </a-form-item>
                  </a-form> -->

                  <!-- <a-row :gutter="16">
                    <a-col class="gutter-row" :span="6">
                      <div class="gutter-box">
                        col-6
                      </div>
                    </a-col>
                    <a-col class="gutter-row" :span="18">
                      <div class="gutter-box">
                        col-6
                      </div>
                    </a-col>
                  </a-row>
                  <a-row :gutter="16">
                    <a-col class="gutter-row" :span="6">
                      <div class="gutter-box">
                        col-6
                      </div>
                    </a-col>
                    <a-col class="gutter-row" :span="18">
                      <div class="gutter-box">
                        col-6
                      </div>
                    </a-col>
                  </a-row>
                  <a-row :gutter="16">
                    <a-col class="gutter-row" :span="6">
                      <div class="gutter-box">
                        col-6
                      </div>
                    </a-col>
                    <a-col class="gutter-row" :span="18">
                      <div class="gutter-box">
                        col-6
                      </div>
                    </a-col>
                  </a-row> -->
                </div>
              </a-card>
            </a-col>
          </a-row>
          <a-button icon="plus-circle" block style="margin-top: 20px;">
            新增提醒
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  export default {
    name: 'Remind',
    mixins: [flowMixin],
    components: {},
    props: {},
    data() {
      return {
        bodyStyle: {
          padding: '12px 12px 0',
          'margin-bottom': '8px',
          border: '1px solid rgba(17, 31, 44, 0.08)',
          'box-shadow': '0 2px 8px 0 rgb(17 31 44 / 4%)',
          'border-radius': '8px',
        },
        items: [
          {
            id: '',
            name: '提醒方式',
            checked: true,
            des: 'xxxxxxxxxxxx',
            icon: '',
          },
          {
            id: '',
            name: '提醒方式',
            checked: true,
            icon: '',
          },
          /* {
            id: '',
            name: '提醒方式',
            checked: true,
            icon: '',
          },
          {
            id: '',
            name: '提醒方式',
            checked: false,
            icon: '',
          }, */
        ],
      };
    },
    computed: {},
    mounted() {},
    methods: {},
  };
</script>
<style lang="less"></style>
