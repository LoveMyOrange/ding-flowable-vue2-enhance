<template>
  <div class="flow-setting-module">
    <div class="flow-setting-content">
      <div class="flow-setting-block">
        <div class="flow-block-title">标题设置</div>
        <div class="flow-block-desc">通过设置数据的显示标题，快速识别一条数据</div>
        <div class="flow-block-content">
          <a-radio-group name="radioGroup" :size="size" :default-value="1">
            <a-radio :value="1">
              默认
            </a-radio>
            <a-radio :value="2">
              自定义模式
            </a-radio>
          </a-radio-group>
          <div class="flow-block-tabContent">
            <div class="flow-block-defaultTab">
              <div class="flow-block-defaultTpl">
                <span class="flow-block-var">提交人</span>
                <span>的</span>
                <span class="flow-block-var">表单名称</span>
              </div>
              <div class="flow-block-defaultSample">示例：陈德言的业绩上报、陆宜的请假</div>
            </div>
          </div>
        </div>
      </div>
      <div class="flow-setting-block" style="margin-top: 32px;">
        <div class="flow-block-title">摘要设置</div>
        <div class="flow-block-desc">自定义表单摘要展示信息，灵活设置提升工作效率</div>
        <div class="flow-block-content">
          <a-radio-group name="radioGroup" :size="size" :default-value="1">
            <a-radio :value="1">
              默认
            </a-radio>
            <a-radio :value="2">
              自定义模式
            </a-radio>
          </a-radio-group>
          <div class="flow-block-tabContent">
            <div class="flow-block-defaultTab">
              <div class="flow-block-defaultTpl">
                <span>默认设置（展示表单中前3个字段，必填字段优先）</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  export default {
    name: 'Exhibition',
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
