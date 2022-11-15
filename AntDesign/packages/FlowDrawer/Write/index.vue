<template>
  <a-drawer
    :width="drawerWidth()"
    :headerStyle="headerStyle"
    :bodyStyle="bodyStyle"
    placement="right"
    :closable="true"
    :visible="visible"
    :after-visible-change="afterVisibleChange"
    @close="onClose"
  >
    <template slot="title">
      <img :src="ccIcon" class="anticon" />
      <span class="flow-ant-drawer-title">
        <EditName v-model="node.name" />
      </span>
    </template>
    <div class="flow-setting-module">
      <div class="flow-setting-content">
        <div class="flow-setting-item">
          <p class="flow-setting-item-title">表单权限</p>
          <AuthForm v-model="node.privileges" readable />
        </div>
      </div>
    </div>
    <FlowDrawerFooter @close="onClose" @save="onSave" />
  </a-drawer>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import FlowDrawerFooter from '../../Common/DrawerFooter.vue';
  import EditName from '../../Common/EditName.vue';
  import AuthForm from '../../Common/AuthForm.vue';
  export default {
    name: 'FlowWriteSetting',
    components: { FlowDrawerFooter, EditName, AuthForm },
    mixins: [flowMixin],
    data() {
      return {
        node: {},
        visible: false,
        headerStyle: {
          background: 'linear-gradient(90.04deg,#268bfb -16.37%,#33e1ae 137.34%)',
          // 'background-color': '#6da4f2',
          'border-radius': '0px 0px 0 0',
        },
      };
    },
    methods: {
      afterVisibleChange(val) {
        console.log('visible', val);
      },
      showDrawer(node) {
        this.visible = true;
        this.node = node;
      },
      onClose() {
        this.visible = false;
        this.$emit('close');
      },
      /**
       * 保存配置
       */
      onSave() {
        // 更新节点显示信息
        if (this.node.privileges.length > 0) {
          this.$store.dispatch('flow/updateNode', { currNode: this.node, field: 'content', value: '已设置' });
          this.$store.dispatch('flow/updateNode', { currNode: this.node, field: 'error', value: false });
          this.onClose();
        } else {
          this.$store.dispatch('flow/updateNode', { currNode: this.node, field: 'content', value: null });
          this.$store.dispatch('flow/updateNode', { currNode: this.node, field: 'error', value: false });
        }
      },
    },
  };
</script>
