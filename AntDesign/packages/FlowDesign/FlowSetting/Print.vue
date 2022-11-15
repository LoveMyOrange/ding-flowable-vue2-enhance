<template>
  <div class="flow-setting-module">
    <div class="flow-setting-content">
      <div class="flow-setting-block">
        <div class="flow-block-title">打印模板</div>
        <div class="flow-block-desc">通过设置推送提醒指定成员填写表单</div>
        <div class="flow-block-content">
          <a-radio-group name="radioGroup" :size="size" :default-value="1">
            <a-radio :value="1">
              默认
            </a-radio>
            <a-radio :value="2">
              自定义打印模板
            </a-radio>
          </a-radio-group>
          <!-- <div class="flow-block-tabContent">
            <div class="flow-block-defaultTab">
              <div class="flow-block-defaultTpl">
                <span class="flow-block-var">提交人</span>
                <span>的</span>
                <span class="flow-block-var">表单名称</span>
              </div>
              <div class="flow-block-defaultSample">示例：陈德言的业绩上报、陆宜的请假</div>
            </div>
          </div> -->
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  export default {
    name: 'Print',
    mixins: [flowMixin],
    components: {},
    props: {},
    data() {
      return {};
    },
    computed: {},
    mounted() {},
    methods: {},
  };
</script>
<style lang="less"></style>
