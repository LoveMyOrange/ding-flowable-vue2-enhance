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
      <img :src="noticeIcon" class="anticon" />
      <span class="flow-ant-drawer-title">
        <EditName v-model="node.name" />
      </span>
    </template>
    <div class="flow-setting-module">
      <a-tabs>
        <a-tab-pane key="1" tab="通知设置">
          <div class="flow-setting-content">
            <div class="flow-setting-item">
              <p class="flow-setting-item-title">节点名称</p>
              <a-input v-model="node.name" :size="size" class="w-full" placeholder="节点名称" />
            </div>
            <div class="flow-setting-item">
              <p class="flow-setting-item-title">通知类型</p>
              <a-checkbox-group v-model="noticeType">
                <a-row :gutter="[24, 24]">
                  <a-col :span="8" v-for="(notice, i) in notices" :key="i">
                    <a-checkbox :value="notice.value">{{ notice.name }}</a-checkbox>
                  </a-col>
                </a-row>
              </a-checkbox-group>
            </div>
            <div class="flow-setting-item">
              <p class="flow-setting-item-title">发送通知人</p>
              <FlowNodeApproval :groups="node.approverGroups" :node="node" title="通知人" />
            </div>
            <div v-if="noticeType.includes(2)" class="flow-setting-item">
              <p class="flow-setting-item-title">外部手机号</p>
              <a-button type="link" icon="plus" block>
                添加手机号
              </a-button>
            </div>
            <div v-if="noticeType.includes(3)" class="flow-setting-item">
              <p class="flow-setting-item-title">外部邮箱账号</p>
              <a-button type="link" icon="plus" block>
                添加邮箱
              </a-button>
            </div>
            <div v-if="noticeType.includes(3)" class="flow-setting-item">
              <a-checkbox-group v-model="emailExt">
                <a-row :gutter="[12, 12]">
                  <a-col :span="12" v-for="(item, i) in emailItems" :key="i">
                    <a-checkbox :value="item.value">{{ item.name }}</a-checkbox>
                  </a-col>
                </a-row>
              </a-checkbox-group>
            </div>
            <div v-if="noticeType.includes(3) && emailExt.includes(1)" class="flow-setting-item">
              <p class="flow-setting-item-title">抄送人</p>
              <FlowNodeApproval :groups="node.approverGroups" :node="node" title="抄送人" />
            </div>
            <div v-if="noticeType.includes(3) && emailExt.includes(2)" class="flow-setting-item">
              <p class="flow-setting-item-title">密送人</p>
              <FlowNodeApproval :groups="node.approverGroups" :node="node" title="密送人" />
            </div>
          </div>
        </a-tab-pane>
        <a-tab-pane key="2" tab="内容设置">
          <div class="flow-setting-content">
            <div class="flow-setting-item">
              <p class="flow-setting-item-title">选择已审核模板</p>
              <div class="tpl-flex-box">
                <a-button type="link" icon="plus-circle">
                  创建新模板
                </a-button>
              </div>
              <a-select :size="size" style="width: 100%;margin-bottom: 20px;" placeholder="请选择模板"></a-select>
            </div>
            <div class="flow-setting-item">
              <p class="flow-setting-item-title">主题</p>
              <a-input v-model="noticeTitle" :size="size" :rows="4" placeholder="主题" />
            </div>
            <div class="flow-setting-item">
              <p class="flow-setting-item-title">通知内容</p>
              <a-textarea v-model="noticeContext" :size="size" :rows="4" placeholder="通知内容" />
            </div>
          </div>
        </a-tab-pane>
        <a-tab-pane key="3" tab="高级设置"></a-tab-pane>
      </a-tabs>
    </div>
    <FlowDrawerFooter @close="onClose" />
  </a-drawer>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import EditName from '../../Common/EditName.vue';
  import FlowNodeApproval from '../Approver/Approval.vue';
  import FlowDrawerFooter from '../../Common/DrawerFooter.vue';
  export default {
    name: 'FlowNoticeSetting',
    mixins: [flowMixin],
    components: { EditName, FlowNodeApproval, FlowDrawerFooter },
    data() {
      return {
        node: {},
        visible: false,
        headerStyle: {
          'background-color': '#498ff2',
          'border-radius': '0px 0px 0 0',
        },
        noticeContext: '',
        noticeType: [],
        // 邮件选择项
        emailExt: [],
        notices: [
          {
            name: '站内通信',
            value: 1,
          },
          {
            name: '短信通知',
            value: 2,
          },
          {
            name: '邮件通知',
            value: 3,
          },
          {
            name: '钉钉通知',
            value: 4,
          },
          {
            name: '企业微信',
            value: 5,
          },
          {
            name: 'WeLink',
            value: 6,
          },
        ],
        emailItems: [
          {
            name: '添加抄送',
            value: 1,
          },
          {
            name: '添加密送',
            value: 2,
          },
        ],
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
