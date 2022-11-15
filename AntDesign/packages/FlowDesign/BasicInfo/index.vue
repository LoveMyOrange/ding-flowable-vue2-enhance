<template>
  <div class="designer-wrap">
    <FlowNav v-if="navable && !readable" :currentNav="1" :buttonName="buttonName" @click="publish" @change="change" />
    <div class="designer-base-info">
      <div class="base-info-panel">
        <a-form :layout="formLayout">
          <a-form-item label="图标">
            <a-avatar shape="square" size="large" icon="red-envelope" />
          </a-form-item>
          <a-form-item label="名称">
            <a-input v-model="flowName" placeholder="请输入名称" :size="size" />
          </a-form-item>
          <a-form-item label="分组">
            <FlowSimpleSelect v-model="flowGroup" :datas="flowGroups" labelName="label" placeholder="请选择分组" />
          </a-form-item>
          <a-form-item label="绑定表单">
            <FlowSelect v-model="bindForm" :datas="forms" mode="multiple" labelName="label" placeholder="请选择表单" />
          </a-form-item>
          <a-form-item v-if="bindForm.length > 1" label="多表单显示模式">
            <a-radio-group :size="size" class="w-fill">
              <a-radio value="1">
                <span>标签栏</span>
              </a-radio>
              <a-radio value="2">
                <span>步进式</span>
              </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="谁可以管理这个审批">
            <!-- <UserSelector type="button" /> -->
          </a-form-item>
          <a-form-item label="说明">
            <a-textarea :size="size" :rows="4" placeholder="说明" />
          </a-form-item>
        </a-form>
      </div>
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import FlowNav from '../../Common/FlowNav.vue';
  import FlowSelect from '../../Component/FlowSelect.vue';
  import FlowSimpleSelect from '../../Component/FlowSimpleSelect.vue';
  export default {
    name: 'BasicInfo',
    mixins: [flowMixin],
    components: { FlowNav, FlowSelect, FlowSimpleSelect },
    props: {
      navable: {
        type: Boolean,
        default: true,
      },
      readable: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        buttonName: '保存',
        formLayout: 'vertical',
        flowName: '',
        flowGroup: '',
        bindForm: [],
        flowGroups: [
          { label: '人事', value: '人事' },
          { label: '考勤', value: '考勤' },
          { label: '行政', value: '行政' },
          { label: '财务', value: '财务' },
          { label: '薪酬', value: '薪酬' },
          { label: '资产', value: '资产' },
        ],
        forms: [
          { label: '请假表单', value: '人事表单' },
          { label: '加班表单', value: '考勤表单' },
          { label: '调休表单', value: '调休表单' },
          { label: '补卡表单', value: '补卡表单' },
          { label: '出差表单', value: '出差表单' },
          { label: '销假表单', value: '销假表单' },
        ],
      };
    },
    computed: {},
    mounted() {},
    methods: {
      toReturn() {},
      change(type) {},
      handleSave() {},
      getData() {
        return this.nodeData;
      },
      publish() {
        this.$emit('publish', this.nodeData);
      },
    },
  };
</script>
