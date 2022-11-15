<template>
  <div class="flow-setting-auth-table">
    <div class="flow-setting-auth-table-header">
      <div :class="{ 'flow-setting-auth-table-name-column-30': !readable, 'flow-setting-auth-table-name-column-25': readable }">
        <span>表单</span>
      </div>
      <div class="flow-setting-auth-table-option-column">
        <div :class="{ 'flow-setting-auth-table-option-column-item-50': !readable, 'flow-setting-auth-table-option-column-item-25': readable }" v-if="readable">
          <a-checkbox :checked="writableChecked" @change="onAllWritableChange">
            编辑
          </a-checkbox>
        </div>
        <div :class="{ 'flow-setting-auth-table-option-column-item-50': !readable, 'flow-setting-auth-table-option-column-item-25': readable }">
          <a-checkbox :checked="readableChecked" @change="onAllReadableChange">
            只读
          </a-checkbox>
        </div>
        <div :class="{ 'flow-setting-auth-table-option-column-item-50': !readable, 'flow-setting-auth-table-option-column-item-25': readable }">
          <a-checkbox :checked="displayableChecked" @change="onAllDisplayableChange">
            隐藏
          </a-checkbox>
        </div>
        <div :class="{ 'flow-setting-auth-table-option-column-item-50': !readable, 'flow-setting-auth-table-option-column-item-25': readable }" v-if="readable">
          <a-checkbox :checked="requiredChecked" @change="onAllRequiredChange">
            必填
          </a-checkbox>
        </div>
      </div>
    </div>
    <div class="flow-setting-auth-table-body">
      <div class="flow-setting-auth-table-name-column-row" v-for="(item, i) in fields" :key="i">
        <div :class="{ 'flow-setting-auth-table-name-column-30': !readable, 'flow-setting-auth-table-name-column-25': readable }">
          <span>{{ item.name }}</span>
        </div>
        <div class="flow-setting-auth-table-option-column">
          <div :class="{ 'flow-setting-auth-table-option-column-item-50': !readable, 'flow-setting-auth-table-option-column-item-25': readable }" v-if="readable">
            <a-checkbox :checked="item.writable" :v-model="item.writable" @change="writableChange($event, item)">
              编辑
            </a-checkbox>
          </div>
          <div :class="{ 'flow-setting-auth-table-option-column-item-50': !readable, 'flow-setting-auth-table-option-column-item-25': readable }">
            <a-checkbox :checked="item.readable" :v-model="item.readable" @change="readableChange($event, item)">
              只读
            </a-checkbox>
          </div>
          <div :class="{ 'flow-setting-auth-table-option-column-item-50': !readable, 'flow-setting-auth-table-option-column-item-25': readable }">
            <a-checkbox :checked="item.displayable" :v-model="item.displayable" @change="displayableChange($event, item)">
              隐藏
            </a-checkbox>
          </div>
          <div :class="{ 'flow-setting-auth-table-option-column-item-50': !readable, 'flow-setting-auth-table-option-column-item-25': readable }" v-if="readable">
            <a-checkbox :checked="item.required" :v-model="item.required" @change="displayableRequired($event, item)">
              必填
            </a-checkbox>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import { flowMixin } from '../mixins/flowMixin';
  export default {
    name: 'FlowDrawerAuthForm',
    mixins: [flowMixin],
    props: {
      readable: {
        type: Boolean,
        default: false,
      },
      node: {
        type: Object,
        default: function() {
          return {};
        },
      },
      value: {
        type: Array,
        default: function() {
          return [];
        },
      },
    },
    data() {
      return {
        fields: [
          {
            id: this.uuid(),
            name: '姓名',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '工号',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '部门',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '性别',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '职位',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '账号',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '学历',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '毕业证书',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '资格证书',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '身份证正面',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '身份证反面',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '银行信息',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
          {
            id: this.uuid(),
            name: '相关附件',
            writable: true,
            readable: false,
            displayable: false,
            required: false,
          },
        ],
      };
    },
    computed: {
      writableChecked: function() {
        let me = this;
        let checked = me.fields.filter((item) => item.writable == false).length == 0;
        return checked;
      },
      readableChecked: function() {
        let me = this;
        return me.fields.filter((item) => item.readable == false).length == 0;
      },
      displayableChecked: function() {
        let me = this;
        return me.fields.filter((item) => item.displayable == false).length == 0;
      },
      requiredChecked: function() {
        let me = this;
        return me.fields.filter((item) => item.required == false).length == 0;
      },
    },
    mounted() {
      console.log(this.readable);
    },
    methods: {
      /**
       *  全选编辑
       * @param {*} e
       */
      onAllWritableChange(e) {
        this.fields.forEach((item, i) => {
          this.writableChange(e, item);
        });
      },
      /**
       * 全选只读
       * @param {*} e
       */
      onAllReadableChange(e) {
        for (let item of this.fields) {
          this.readableChange(e, item);
        }
      },
      /**
       * 全选隐藏
       * @param {*} e
       */
      onAllDisplayableChange(e) {
        this.fields.forEach((item, i) => {
          this.displayableChange(e, item);
        });
      },
      /**
       * 全选必填
       * @param {*} e
       */
      onAllRequiredChange(e) {
        this.fields.forEach((item, i) => {
          this.displayableRequired(e, item);
        });
      },
      /**
       * 编辑
       * @param {*} e
       * @param {*} item
       */
      writableChange(e, item) {
        item.writable = e.target.checked;
        if (e.target.checked) {
          item.readable = !e.target.checked;
          item.displayable = !e.target.checked;
        } else {
          item.required = e.target.checked;
        }
        this.changePrivilege();
      },
      /**
       * 只读
       * @param {*} e
       * @param {*} item
       */
      readableChange(e, item) {
        item.readable = e.target.checked;
        if (e.target.checked) {
          item.writable = !e.target.checked;
          item.displayable = !e.target.checked;
          item.required = !e.target.checked;
        }
        this.changePrivilege();
      },
      /**
       * 隐藏
       * @param {*} e
       * @param {*} item
       */
      displayableChange(e, item) {
        item.displayable = e.target.checked;
        if (e.target.checked) {
          item.writable = !e.target.checked;
          item.readable = !e.target.checked;
          item.required = !e.target.checked;
        }
        this.changePrivilege();
      },
      /**
       * 必填
       * @param {*} e
       * @param {*} item
       */
      displayableRequired(e, item) {
        item.required = e.target.checked;
        if (e.target.checked) {
          item.readable = !e.target.checked;
          item.displayable = !e.target.checked;
          item.writable = e.target.checked;
        }
        this.changePrivilege();
      },
      changePrivilege() {
        this.$emit('input', this.fields);
      },
    },
  };
</script>
