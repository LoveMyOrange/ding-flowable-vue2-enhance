<template>
  <!-- 审批人 -->
  <a-space direction="vertical">
    <a-card v-for="(group, k) in groups" :key="k" :headStyle="headStyle" class="w-fill">
      <template slot="title">
        <span class="flow-setting-approval-title">
          <span>{{ title }}</span>
          <a-icon type="delete" class="del-icon" @click="delApproval(group)" />
        </span>
      </template>
      <div class="flow-setting-item">
        <!-- 审批人类型 -->
        <a-radio-group v-model="group.approverType" class="w-fill" :size="size" @change="changeApproverType(group)">
          <a-radio v-for="(approval, i) in approvals" :key="i" :style="approvalRadioStyle" :value="approval.value" :disabled="approval.disabled && groups.length > 1">
            <span>{{ approval.name }}</span>
            <a-popover v-if="approval.popovers && approval.popovers.length > 0" placement="topLeft" trigger="click">
              <template slot="content">
                <div class="approver-tip-content">
                  <div class="approver-tip-main-content">
                    <div v-for="(popover, k) in approval.popovers" :key="k">
                      <p class="main-title">{{ popover.title }}</p>
                      <p class="content">{{ popover.content }}</p>
                    </div>
                  </div>
                  <a v-if="approval.href" :href="approval.href" target="_blank">{{ approval.hrefName }}</a>
                </div>
              </template>
              <a-icon style="margin-left: 5px;" type="question-circle" />
            </a-popover>
          </a-radio>
        </a-radio-group>
        <!-- 上级 -->
        <div v-if="group.approverType == 1">
          <p class="flow-setting-item-title">
            <span>指定层级</span>
          </p>
          <a-radio-group :size="size" v-model="group.levelMode" class="w-fill">
            <a-radio v-for="(higherLevel, i) in higherLevelModes" :key="i" :style="radioStyle" :value="higherLevel.value">
              <span>{{ higherLevel.name }}</span>
              <a-popover v-if="higherLevel.popovers && higherLevel.popovers.length > 0" placement="topLeft" trigger="click">
                <template slot="content">
                  <div class="approver-tip-content">
                    <div class="approver-tip-main-content">
                      <div v-for="(popover, k) in higherLevel.popovers" :key="k">
                        <p class="main-title">{{ popover.title }}</p>
                        <p class="content">{{ popover.content }}</p>
                      </div>
                    </div>
                    <a v-if="higherLevel.href" :href="higherLevel.href" target="_blank">{{ higherLevel.hrefName }}</a>
                  </div>
                </template>
                <a-icon style="margin-left: 5px;" type="question-circle" />
              </a-popover>
            </a-radio>
          </a-radio-group>
          <FlowSelect v-model="group.approverIds" :name.sync="group.approverNames" :datas="higherLevels" />
        </div>
        <!-- 部门负责人 -->
        <div v-if="group.approverType == 2">
          <p class="flow-setting-item-title">
            <span>指定层级</span>
          </p>
          <a-radio-group v-model="group.levelMode" :size="size" class="w-fill">
            <a-radio v-for="(departmentHead, i) in departmentHeadModes" :key="i" :style="radioStyle" :value="departmentHead.value">
              <span>{{ departmentHead.name }}</span>
              <a-popover v-if="departmentHead.popovers && departmentHead.popovers.length > 0" placement="topLeft" trigger="click">
                <template slot="content">
                  <div class="approver-tip-content">
                    <div class="approver-tip-main-content">
                      <div v-for="(popover, k) in departmentHead.popovers" :key="k">
                        <p class="main-title">{{ popover.title }}</p>
                        <p class="content">{{ popover.content }}</p>
                      </div>
                    </div>
                    <a v-if="departmentHead.href" :href="departmentHead.href" target="_blank">{{ departmentHead.hrefName }}</a>
                  </div>
                </template>
                <a-icon style="margin-left: 5px;" type="question-circle" />
              </a-popover>
            </a-radio>
          </a-radio-group>
          <FlowSelect v-model="group.approverIds" :name.sync="group.approverNames" :datas="departmentHeads" />
        </div>
        <!-- 部门审批人 -->
        <div v-if="group.approverType == 3">
          <p class="flow-setting-item-title">
            <span>部门审批人</span>
          </p>
          <FlowSelect v-model="group.approverIds" :name.sync="group.approverNames" :datas="departmentApprovals" />
        </div>
        <!-- 编码审批人 -->
        <div v-if="group.approverType == 4">
          <p class="flow-setting-item-title">
            <span>编码对应部门审批人</span>
          </p>
          <FlowSelect v-model="group.approverIds" :name.sync="group.approverNames" :datas="departmentApprovals" />
        </div>
        <!-- 角色 -->
        <div v-if="group.approverType == 5">
          <p class="flow-setting-item-title">
            <span>选择角色</span>
          </p>
          <FlowSelect v-model="group.approverIds" :name.sync="group.approverNames" :datas="roles" />
        </div>
        <!-- 岗位 -->
        <div v-if="group.approverType == 6">
          <p class="flow-setting-item-title">
            <span>选择岗位</span>
          </p>
          <FlowSelect v-model="group.approverIds" :name.sync="group.approverNames" :datas="posts" />
        </div>
        <!-- 用户组 -->
        <div v-if="group.approverType == 7">
          <p class="flow-setting-item-title">
            <span>选择用户组</span>
          </p>
          <FlowSelect v-model="group.approverIds" :name.sync="group.approverNames" :datas="userGroups" />
        </div>
        <!-- 指定成员 -->
        <div v-if="group.approverType == 8">
          <p class="flow-setting-item-title">
            <span>指定成员</span>
            <span class="light-text">(不能超过 25 人)</span>
            <UserSelector v-model="group.approverIds" mode="multiple" type="button" />
          </p>
        </div>
        <!-- 发起人自选 -->
        <div v-if="group.approverType == 9">
          <p class="flow-setting-item-title">
            <span>选择方式</span>
          </p>
          <a-radio-group :size="size" class="w-fill">
            <a-radio value="1">
              <span>多选</span>
            </a-radio>
            <a-radio value="2">
              <span>单选</span>
            </a-radio>
          </a-radio-group>
          <p class="flow-setting-item-title margin-top-10">
            <span>选择范围</span>
          </p>
          <a-radio-group :size="size" class="w-fill">
            <a-radio value="1">
              <span>全公司</span>
            </a-radio>
            <a-radio value="2">
              <span>指定成员</span>
            </a-radio>
            <a-radio value="3">
              <span>角色成员</span>
            </a-radio>
          </a-radio-group>
          <p class="flow-setting-item-title margin-top-10">
            <span>指定成员</span>
            <span class="light-text">(不能超过 25 人)</span>
            <UserSelector type="button" />
          </p>
        </div>
        <!-- 节点审批人 -->
        <div v-if="group.approverType == 11">
          <p class="flow-setting-item-title">
            <span>选择节点</span>
          </p>
          <FlowSelect v-model="group.approverIds" :name.sync="group.approverNames" :datas="approveNodes" valueName="id" labelName="name" />
          <p class="flow-setting-item-title">
            <span class="light-text">你可以选择前序节点名称，如果名称重复建议先修改审批节点的节点名称</span>
          </p>
        </div>
        <!-- 连续多级上级审批 -->
        <div v-if="group.approverType == 12">
          <p class="flow-setting-item-title">
            <span>审批终点</span>
          </p>
          <FlowSelect v-model="group.approverIds" :name.sync="group.approverNames" :datas="higherLevels" />
        </div>
        <!-- 表单内人员 -->
        <div v-if="group.approverType == 13">
          <p class="flow-setting-item-title">
            <span>人员控件</span>
          </p>
          <a-select :size="size" class="w-fill" default-value="1">
            <a-select-option :value="higherLevel.value" v-for="(higherLevel, i) in higherLevels" :key="i">
              {{ higherLevel.name }}
            </a-select-option>
          </a-select>
          <p class="flow-setting-item-title margin-top-10">
            <span>审批类型</span>
          </p>
          <a-radio-group :size="size" class="w-fill">
            <a-radio value="1">
              <span>人员自己</span>
            </a-radio>
            <a-radio value="2">
              <span>人员上级</span>
            </a-radio>
            <a-radio value="3">
              <span>人员部门负责人</span>
            </a-radio>
          </a-radio-group>
        </div>
        <!-- 表单内部门 -->
        <div v-if="group.approverType == 14">
          <p class="flow-setting-item-title">
            <span>部门控件</span>
          </p>
          <a-select :size="size" class="w-fill" default-value="1">
            <a-select-option :value="higherLevel.value" v-for="(higherLevel, i) in higherLevels" :key="i">
              {{ higherLevel.name }}
            </a-select-option>
          </a-select>
          <p class="flow-setting-item-title margin-top-10">
            <span>指定层级</span>
          </p>
          <a-radio-group v-model="group.levelMode" :size="size" class="w-fill">
            <a-radio v-for="(departmentHead, i) in departmentHeadModes" :key="i" :style="radioStyle" :value="departmentHead.value">
              <span>{{ departmentHead.name }}</span>
              <a-popover v-if="departmentHead.popovers && departmentHead.popovers.length > 0" placement="topLeft" trigger="click">
                <template slot="content">
                  <div class="approver-tip-content">
                    <div class="approver-tip-main-content">
                      <div v-for="(popover, k) in departmentHead.popovers" :key="k">
                        <p class="main-title">{{ popover.title }}</p>
                        <p class="content">{{ popover.content }}</p>
                      </div>
                    </div>
                    <a v-if="departmentHead.href" :href="departmentHead.href" target="_blank">{{ departmentHead.hrefName }}</a>
                  </div>
                </template>
                <a-icon style="margin-left: 5px;" type="question-circle" />
              </a-popover>
            </a-radio>
          </a-radio-group>
          <FlowSelect v-model="group.approverIds" :name.sync="group.approverNames" :datas="departmentHeads" />
        </div>
      </div>
    </a-card>
    <a-button v-if="show" type="link" icon="plus" block @click="addApproval">添加{{ title }}</a-button>
  </a-space>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import { getApproveNodes } from '../../util/nodeUtil';
  import FlowSelect from '../../Component/FlowSelect.vue';
  export default {
    name: 'FlowNodeApproval',
    components: {
      FlowSelect,
    },
    mixins: [flowMixin],
    props: {
      groups: {
        type: Array,
        default: function() {
          return [];
        },
      },
      node: {
        type: Object,
        default: function() {
          return {};
        },
      },
      title: {
        type: String,
        default: '审批人',
      },
    },
    data() {
      return {
        headStyle: {
          background: '#f5f6f7',
          'min-height': '40px',
        },
        approvals: [
          {
            name: '上级',
            code: 'higherLevel',
            value: 1,
            // 多个组时需要disabled
            disabled: false,
            // 是否可以多个组
            addable: true,
          },
          {
            name: '部门负责人',
            value: 2,
            disabled: false,
            // 是否可以多个组
            addable: true,
            popovers: [
              {
                title: '什么是部门负责人审批？',
                content: '这里指在管理后台 - 组织架构中所设置的部门负责人',
              },
              {
                title: '什么是部门负责人审批？',
                content:
                  '部门负责人审批与上级审批的区别？一个部门内可能存在多层的上下级关系，但通常有指定的部门负责人。由部门负责人审批 ，则不涉及上下级关系，直接由该固定人员进行审批',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '如何配置部门负责人？',
          },
          {
            name: '部门审批人',
            value: 3,
            disabled: false,
            // 是否可以多个组
            addable: true,
            popovers: [
              {
                title: '什么是部门负责人审批？',
                content: '这里指在管理后台 - 组织架构中所设置的部门负责人',
              },
              {
                title: '什么是部门负责人审批？',
                content:
                  '部门负责人审批与上级审批的区别？一个部门内可能存在多层的上下级关系，但通常有指定的部门负责人。由部门负责人审批 ，则不涉及上下级关系，直接由该固定人员进行审批',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '如何配置部门负责人？',
          },
          {
            name: '编码审批人',
            value: 4,
            popovers: [
              {
                title: '什么是部门负责人审批？',
                content: '这里指在管理后台 - 组织架构中所设置的部门负责人',
              },
              {
                title: '什么是部门负责人审批？',
                content:
                  '部门负责人审批与上级审批的区别？一个部门内可能存在多层的上下级关系，但通常有指定的部门负责人。由部门负责人审批 ，则不涉及上下级关系，直接由该固定人员进行审批',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '如何配置部门负责人？',
          },
          {
            name: '角色',
            value: 5,
            disabled: false,
            // 是否可以多个组
            addable: true,
            popovers: [
              {
                title: '什么是角色？',
                content: '角色指团队成员的专业分工类别，如人事、行政、财务等，每类角色可由 1 位或多位成员组成',
              },
              {
                title: '如何使用？',
                content: '用角色作为审批人，当有成员离职变动时，该角色中的其他成员可继续完成审批，从而避免审批流程失效的情况',
              },
              {
                content: '提示：若选择的角色中包含多名成员，则按照设置“多人审批时采用的审批方式”来处理',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '如何配置角色？',
          },
          {
            name: '岗位',
            value: 6,
            disabled: false,
            // 是否可以多个组
            addable: true,
            popovers: [
              {
                title: '什么是岗位？',
                content: '岗位指团队成员的所处工作职务，一般指的是行政职务，如总监、经理、人事专员、行政助理、财务专员等，每类岗位可由 1 位或多位成员组成',
              },
              {
                title: '如何使用？',
                content: '用岗位作为审批人，当有成员离职变动时，该岗位中的其他成员可继续完成审批，从而避免审批流程失效的情况',
              },
              {
                content: '提示：若选择的岗位中包含多名成员，则按照设置“多人审批时采用的审批方式”来处理',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '如何配置岗位？',
          },
          {
            name: '用户组',
            value: 7,
            disabled: false,
            // 是否可以多个组
            addable: true,
            popovers: [
              {
                title: '什么是用户组？',
                content: '用户组主要用于权限管控，设置某个用户组作为审批人，则只有该用户组中的成员可进行审批。',
              },
              {
                title: '如何使用？',
                content: '用户组主要用于权限管控，设置某个用户组作为审批人，则只有该用户组中的成员可进行审批。',
              },
              {
                content: '提示：若选择的用户组中包含多名成员，则按照设置“多人审批时采用的审批方式”来处理',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '如何配置用户组？',
          },
          {
            name: '指定成员',
            value: 8,
            disabled: false,
            // 是否可以多个组
            addable: true,
          },
          {
            name: '发起人自选',
            value: 9,
            // 多个组时需要disabled
            disabled: true,
            // 是否可以多个组
            addable: false,
          },
          {
            name: '发起人自己',
            value: 10,
            disabled: true,
            // 是否可以多个组
            addable: true,
            popovers: [
              {
                title: '什么是发起人审批？',
                content: '将发起人自己设置为审批人，可用于需要发起人进行信息复核的场景',
              },
            ],
          },
          {
            name: '节点审批人',
            value: 11,
            disabled: true,
            // 是否可以多个组
            addable: true,
            popovers: [
              {
                title: '什么是节点审批人？',
                content: '通过选择前面已经配置的节点进行关联，流程执行时自动获取所有关联审批节点中的实际审批人作为当前节点的审批人',
              },
              {
                content: '节点审批人默认为多人审批，不会从关联的审批节点继承，需单独设置审批方式',
              },
              {
                content: '当前节点为节点审批人时，同一审批人重复审批将不会触发自动通过',
              },
              {
                title: '如何关联节点？',
                content: '可以选择前序节点的名称进行关联',
              },
            ],
          },
          {
            name: '连续多级上级审批',
            value: 12,
            disabled: true,
            // 是否可以多个组
            addable: true,
            popovers: [
              {
                title: '什么是连续多级上级审批？',
                content: '从发起人的直属上级开始，依次逐级向上审批，直到所设置的审批终点为止。是手动逐个添加多级上级审批的一种便捷设置',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '查看和设置上级信息',
          },
          {
            name: '表单内人员',
            value: 13,
            disabled: true,
            // 是否可以多个组
            addable: true,
            popovers: [
              {
                title: '如何配置表单内人员？',
                content: '在表单设计中添加人员控件后，该人员/其上级/部门负责人将可以配置为本节点的审批人。',
              },
            ],
          },
          {
            name: '表单内部门',
            value: 14,
            disabled: true,
            // 是否可以多个组
            addable: true,
            popovers: [
              {
                title: '何配置表单内部门？',
                content: '在表单设计中添加部门控件后，其部门负责人可以配置为本节点的审批人。',
              },
            ],
          },
        ],
        // 上级方式
        higherLevelModes: [
          {
            name: '自下而上（以发起人的直属上级为第一级）',
            value: 1,
            popovers: [
              {
                title: '什么是上级 - 自下而上？',
                content: '以发起人的直属上级为第一级，向更高管理层级递增',
              },
              {
                content: '图示：若小王为发起人，则小张是小王的“直属上级”，小李是小王的“第二级上级”',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '查看和设置上级信息',
          },
          {
            name: '自上而下（以公司的最高上级为第一级）',
            code: 'higherLevel',
            value: 2,
            popovers: [
              {
                title: '什么是上级 - 自上而下？',
                content: '以公司组织架构中的最高上级为第一级，向更低管理层级递增',
              },
              {
                content: '图示：若小王为发起人，则小赵是小王的“最高上级”，小周是小王的“第二级上级”',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '查看和设置上级信息',
          },
        ],
        // 上级层级
        higherLevels: [
          {
            name: '直属上级',
            value: '1',
          },
          {
            name: '第二级上级',
            value: '2',
          },
          {
            name: '第三级上级',
            value: '3',
          },
          {
            name: '第四级上级',
            value: '4',
          },
          {
            name: '第五级上级',
            value: '5',
          },
          {
            name: '第六级上级',
            value: '6',
          },
          {
            name: '第七级上级',
            value: '7',
          },
          {
            name: '第八级上级',
            value: '8',
          },
          {
            name: '第九级上级',
            value: '9',
          },
          {
            name: '第十级上级',
            value: '10',
          },
          {
            name: '第十一级上级',
            value: '11',
          },
          {
            name: '第十二级上级',
            value: '12',
          },
        ],
        // 部门负责人方式
        departmentHeadModes: [
          {
            name: '自下而上（以发起人的直接部门负责人为第一级）',
            value: 1,
            popovers: [
              {
                title: '什么是部门负责人 - 自下而上？',
                content: '以发起人的直接部门负责人为第一级，向更高管理层级递增',
              },
              {
                content: '图示：若小王为发起人，则小张是小王的“直接部门负责人”，小李是小王的“第二级部门负责人”',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '如何配置部门负责人？',
          },
          {
            name: '自上而下（以公司的最高部门负责人为第一级）',
            code: 'higherLevel',
            value: 2,
            popovers: [
              {
                title: '什么是部门负责人 - 自上而下？',
                content: '以公司组织架构中的最高部门负责人为第一级，向更低管理层级递增',
              },
              {
                content: '图示：若小王为发起人，则小赵是小王的“最高部门负责人”，小周是小王的“第二级部门负责人”',
              },
            ],
            href: 'https://www.feishu.cn/hc/zh-CN/articles/360044810913',
            hrefName: '查看和设置上级信息',
          },
        ],
        // 部门负责人层级
        departmentHeads: [
          {
            name: '直属部门负责人',
            value: '1',
          },
          {
            name: '第二级部门负责人',
            value: '2',
          },
          {
            name: '第三级部门负责人',
            value: '3',
          },
          {
            name: '第四级部门负责人',
            value: '4',
          },
          {
            name: '第五级部门负责人',
            value: '5',
          },
          {
            name: '第六级部门负责人',
            value: '6',
          },
          {
            name: '第七级部门负责人',
            value: '7',
          },
          {
            name: '第八级部门负责人',
            value: '8',
          },
          {
            name: '第九级部门负责人',
            value: '9',
          },
          {
            name: '第十级部门负责人',
            value: '10',
          },
          {
            name: '第十一级部门负责人',
            value: '11',
          },
          {
            name: '第十二级部门负责人',
            value: '12',
          },
        ],
        // 部门审批人
        departmentApprovals: [
          {
            name: '部长',
            value: '101',
          },
          {
            name: '体系负责人',
            value: '102',
          },
          {
            name: 'HRBP',
            value: '103',
          },
          {
            name: '部门助理',
            value: '104',
          },
          {
            name: '资产助理',
            value: '105',
          },
          {
            name: '办公账号员',
            value: '106',
          },
          {
            name: '门禁员',
            value: '107',
          },
          {
            name: '转岗须知员',
            value: '108',
          },
          {
            name: '编码责任人',
            value: '109',
          },
          {
            name: '项目负责人',
            value: '110',
          },
        ],
        // 角色
        roles: [
          {
            name: '人事',
            value: '1',
          },
          {
            name: '行政',
            value: '2',
          },
          {
            name: '招聘',
            value: '3',
          },
          {
            name: '财务',
            value: '4',
          },
          {
            name: '法务',
            value: '5',
          },
          {
            name: '经理',
            value: '6',
          },
        ],
        // 岗位
        posts: [
          {
            name: '技术顾问',
            value: '1',
          },
          {
            name: 'HRBP',
            value: '2',
          },
          {
            name: '部门助理',
            value: '3',
          },
          {
            name: '行政专员',
            value: '4',
          },
          {
            name: '商务专员',
            value: '5',
          },
          {
            name: '现场助理',
            value: '6',
          },
          {
            name: '项目经理',
            value: '7',
          },
          {
            name: '薪酬专员',
            value: '8',
          },
          {
            name: '招聘专员',
            value: '9',
          },
          {
            name: '考勤专员',
            value: '10',
          },
          {
            name: '副总经理',
            value: '11',
          },
        ],
        // 用户组
        userGroups: [
          {
            name: '采购组',
            value: '1',
          },
          {
            name: '报销组',
            value: '2',
          },
          {
            name: '资产组',
            value: '3',
          },
        ],
      };
    },
    computed: {
      /**
       * 显示添加按钮
       */
      show: function() {
        // 发起人自选/发起人自己只能一个组
        return this.groups.filter((group) => [9, 10].includes(group.approverType)).length == 0;
      },
      approveNodes: function() {
        // 审批人节点
        const approveNodes = [];
        getApproveNodes(this.$store.state.flow.node, approveNodes);
        // 过滤自己
        return approveNodes.filter((approveNode) => approveNode.id != this.node.id);
      },
    },
    mounted() {},
    methods: {
      /**
       * 改变审批人类型
       */
      changeApproverType(group) {
        group.approverIds = [];
        group.approverNames = [];
      },
      // 添加审批人
      addApproval() {
        this.groups.push({
          id: this.uuid(),
          // 审批人模式
          approverType: 1,
          // 层级模式
          levelMode: 1,
          // 审批人ID
          approverIds: [],
          // 审批人名称
          approverNames: [],
        });
      },
      // 删除审批人
      delApproval(group) {
        this.groups.forEach((element, i) => {
          if (element.id == group.id) {
            this.groups.splice(i, 1);
          }
        });
      },
    },
  };
</script>
