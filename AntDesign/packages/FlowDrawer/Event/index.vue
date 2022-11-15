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
      <img :src="webhookIcon2" class="anticon" />
      <span class="flow-ant-drawer-title">
        <EditName v-model="node.name" />
      </span>
    </template>
    <div class="flow-setting-module">
      <div class="flow-setting-content">
        <a-form>
          <div class="flow-setting-item">
            <p class="flow-setting-item-title">节点名称</p>
            <a-form-item name="name">
              <a-input v-model="node.name" :size="size" class="w-full" placeholder="节点名称" />
            </a-form-item>
          </div>
          <div class="flow-setting-item">
            <p class="flow-setting-item-title">事件类型</p>
            <div class="flow-setting-option">
              <div class="flow-setting-option-item">
                <div class="flow-setting-option-item-left">
                  <img :src="optionIcon" />
                  <div class="flow-setting-option-desc">
                    <p class="setting-option-title">前置事件</p>
                    <p class="setting-option-desc">用于节点前,常用于数据校验</p>
                  </div>
                </div>
                <div class="flow-setting-option-item-switch">
                  <a-switch checked-children="开" un-checked-children="关" />
                </div>
              </div>
            </div>
            <div class="flow-setting-option">
              <div class="flow-setting-option-item">
                <div class="flow-setting-option-item-left">
                  <img :src="optionIcon" />
                  <div class="flow-setting-option-desc">
                    <p class="setting-option-title">后置事件</p>
                    <p class="setting-option-desc">用于节点后,常用于数据落地保存</p>
                  </div>
                </div>
                <div class="flow-setting-option-item-switch">
                  <a-switch checked-children="开" un-checked-children="关" />
                </div>
              </div>
            </div>
            <div class="flow-setting-option">
              <div class="flow-setting-option-item">
                <div class="flow-setting-option-item-left">
                  <img :src="optionIcon" />
                  <div class="flow-setting-option-desc">
                    <p class="setting-option-title">WebHook</p>
                    <p class="setting-option-desc">远程API调用</p>
                  </div>
                </div>
                <div class="flow-setting-option-item-switch">
                  <a-switch checked-children="开" un-checked-children="关" />
                </div>
              </div>
            </div>
          </div>
          <div class="flow-setting-item">
            <p class="flow-setting-item-title">前置事件配置</p>
          </div>
          <div class="flow-setting-item">
            <p class="flow-setting-item-title">后置事件配置</p>
            <!-- <a-textarea v-model="noticeContext" :size="size" class="w-full" placeholder="通知内容" /> -->
          </div>
          <div class="flow-setting-item">
            <p class="flow-setting-item-title">WebHook配置</p>
            <!-- <a-textarea v-model="noticeContext" :size="size" class="w-full" placeholder="通知内容" /> -->
          </div>
        </a-form>
      </div>
    </div>
    <FlowDrawerFooter @close="onClose" />
  </a-drawer>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import EditName from '../../Common/EditName.vue';
  import FlowDrawerFooter from '../../Common/DrawerFooter.vue';
  export default {
    name: 'FlowEventSetting',
    mixins: [flowMixin],
    components: { EditName, FlowDrawerFooter },
    data() {
      return {
        node: {},
        visible: false,
        headerStyle: {
          'background-color': '#ff4056',
          'border-radius': '0px 0px 0 0',
        },
        noticeContext: '',
        noticeType: [],
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
    },
  };
</script>
