<template>
  <div class="flow-helper" id="flow-helper">
    <div class="flow-helper-template" @click="showHelperDrawer">
      <svg class="flow-helper-icon" viewBox="0 0 1024 1024" width="1em" height="1em" fill="#0089ff" aria-hidden="true">
        <defs></defs>
        <path
          d="M896 512a42.667 42.667 0 0142.667 42.667V896A42.667 42.667 0 01896 938.667H597.333A42.667 42.667 0 01554.667 896V554.667A42.667 42.667 0 01597.333 512H896zM426.667 682.667a42.667 42.667 0 0142.368 37.674l.298 4.992V896a42.667 42.667 0 01-37.674 42.368l-4.992.299H128a42.667 42.667 0 01-42.368-37.675L85.333 896V725.333a42.667 42.667 0 0137.675-42.368l4.992-.298h298.667zm426.666-85.334H640v256h213.333v-256zM384 768H170.667v85.333H384V768zm42.667-682.667A42.667 42.667 0 01469.333 128v426.667a42.667 42.667 0 01-42.666 42.666H128a42.667 42.667 0 01-42.667-42.666V128A42.667 42.667 0 01128 85.333h298.667zM384 170.667H170.667V512H384V170.667zm512-85.334A42.667 42.667 0 01938.667 128v256A42.667 42.667 0 01896 426.667H597.333A42.667 42.667 0 01554.667 384V128a42.667 42.667 0 0142.666-42.667H896zm-42.667 85.334H640v170.666h213.333V170.667z"
        ></path>
      </svg>
      <span class="flow-helper-title">流程助手</span>
      <svg class="flow-helper-right-icon" viewBox="0 0 1024 1024" width="1em" height="1em" fill="currentColor" aria-hidden="true">
        <defs></defs>
        <path
          d="M324.907 815.68a53.333 53.333 0 0075.52 75.307L741.76 548.5a53.333 53.333 0 00-.107-75.434L400.32 132.907a53.333 53.333 0 10-75.307 75.52l303.574 302.528-303.68 304.725z"
        ></path>
      </svg>
    </div>
    <div class="flow-helper-strengthen" @click="showAdviceDrawer">
      <svg class="flow-helper-icon" viewBox="0 0 1024 1024" width="1em" height="1em" fill="#0089ff" aria-hidden="true">
        <path
          d="M176 48c50.752 0 93.76 33.728 107.52 80h436.48a208 208 0 0 1 11.776 415.68l-11.776 0.32h-68.672a112 112 0 0 1-214.656 0.128L304 544a144 144 0 0 0-9.856 287.68L304 832h436.48a112.256 112.256 0 0 1 107.52-80 112 112 0 0 1 0 224A112.256 112.256 0 0 1 740.48 896H304a208 208 0 0 1-11.776-415.68L304 480h132.672a112 112 0 0 1 214.72 0h68.608a144 144 0 0 0 9.856-287.68L720 192H283.52a112.256 112.256 0 0 1-107.52 80 112 112 0 0 1 0-224z m672 768a48 48 0 1 0 0 96 48 48 0 0 0 0-96zM544 464a48 48 0 0 0-40.704 22.592l-1.792 3.072a47.808 47.808 0 0 0-4.608 31.68l1.6 5.952 1.792 4.48 3.2 5.888 3.968 5.44-2.624-3.392a48 48 0 0 0 79.744-2.048l1.92-3.392 1.28-2.496 1.728-4.48a48 48 0 0 0-1.728-35.072l-1.28-2.56-1.792-3.072-1.6-2.432A48 48 0 0 0 544 464z m-368-352a48 48 0 1 0 0 96 48 48 0 0 0 0-96z"
        ></path>
      </svg>
      <span class="flow-helper-title">流程增强</span>
      <svg class="flow-helper-right-icon" viewBox="0 0 1024 1024" width="1em" height="1em" fill="currentColor" aria-hidden="true">
        <defs></defs>
        <path
          d="M324.907 815.68a53.333 53.333 0 0075.52 75.307L741.76 548.5a53.333 53.333 0 00-.107-75.434L400.32 132.907a53.333 53.333 0 10-75.307 75.52l303.574 302.528-303.68 304.725z"
        ></path>
      </svg>
    </div>

    <a-drawer :placement="placement" getContainer=".flow-helper" :wrapStyle="wrapStyle" :width="drawerWidth('300px')" :visible="visibleHelper" @close="onClose">
      <div class="flow-helper-drawer-header">
        <div class="flow-helper-drawer-header-title">流程助手</div>
        <div class="flow-helper-drawer-header-content">一键启用精选流程模板，提升效率</div>
      </div>

      <a-popover placement="right" trigger="click" v-for="item in simpleItems" :key="item.id">
        <template slot="content">
          <img class="flow-helper-drawer-item-popover-img" :src="item.img" />
        </template>
        <div class="flow-helper-drawer-item" @click="popoverVisible = true">
          <div class="flow-helper-drawer-item-icon">
            <svg class="sw__icon" viewBox="0 0 1024 1024" width="1em" height="1em" fill="#0089ff" aria-hidden="true">
              <defs></defs>
              <path
                d="M320 554.667A85.333 85.333 0 01405.333 640v170.667A85.333 85.333 0 01320 896H149.333A85.333 85.333 0 0164 810.667V640a85.333 85.333 0 0185.333-85.333H320zM768 768a42.667 42.667 0 110 85.333H512A42.667 42.667 0 11512 768h256zM277.333 640H192a42.667 42.667 0 00-42.56 39.467l-.107 3.2V768a42.667 42.667 0 0039.467 42.56l3.2.107h85.333a42.667 42.667 0 0042.56-39.467L320 768v-85.333A42.667 42.667 0 00277.333 640zm576-42.667a42.667 42.667 0 110 85.334H512a42.667 42.667 0 110-85.334h341.333zM320 128a85.333 85.333 0 0185.333 85.333V384A85.333 85.333 0 01320 469.333H149.333A85.333 85.333 0 0164 384V213.333A85.333 85.333 0 01149.333 128H320zm448 213.333a42.667 42.667 0 110 85.334H512a42.667 42.667 0 110-85.334h256zm-490.667-128H192a42.667 42.667 0 00-42.56 39.467l-.107 3.2v85.333a42.667 42.667 0 0039.467 42.56l3.2.107h85.333a42.667 42.667 0 0042.56-39.467l.107-3.2V256a42.667 42.667 0 00-42.667-42.667zm576-42.666a42.667 42.667 0 110 85.333H512a42.667 42.667 0 110-85.333h341.333z"
              ></path>
            </svg>
          </div>
          <div>
            <div class="flow-helper-drawer-item-title">{{ item.name }}</div>
            <div class="flow-helper-drawer-item-content">{{ item.des }}</div>
          </div>
        </div>
      </a-popover>
      <a-divider />
      <a-popover placement="right" trigger="click" v-for="item in integratedItems" :key="item.id">
        <template slot="content">
          <img class="flow-helper-drawer-item-popover-img" :src="item.img" />
        </template>
        <div class="flow-helper-drawer-item" @click="popoverVisible = true">
          <div class="flow-helper-drawer-item-icon">
            <svg class="sw__icon" viewBox="0 0 1024 1024" width="1em" height="1em" fill="#0089ff" aria-hidden="true">
              <defs></defs>
              <path
                d="M320 554.667A85.333 85.333 0 01405.333 640v170.667A85.333 85.333 0 01320 896H149.333A85.333 85.333 0 0164 810.667V640a85.333 85.333 0 0185.333-85.333H320zM768 768a42.667 42.667 0 110 85.333H512A42.667 42.667 0 11512 768h256zM277.333 640H192a42.667 42.667 0 00-42.56 39.467l-.107 3.2V768a42.667 42.667 0 0039.467 42.56l3.2.107h85.333a42.667 42.667 0 0042.56-39.467L320 768v-85.333A42.667 42.667 0 00277.333 640zm576-42.667a42.667 42.667 0 110 85.334H512a42.667 42.667 0 110-85.334h341.333zM320 128a85.333 85.333 0 0185.333 85.333V384A85.333 85.333 0 01320 469.333H149.333A85.333 85.333 0 0164 384V213.333A85.333 85.333 0 01149.333 128H320zm448 213.333a42.667 42.667 0 110 85.334H512a42.667 42.667 0 110-85.334h256zm-490.667-128H192a42.667 42.667 0 00-42.56 39.467l-.107 3.2v85.333a42.667 42.667 0 0039.467 42.56l3.2.107h85.333a42.667 42.667 0 0042.56-39.467l.107-3.2V256a42.667 42.667 0 00-42.667-42.667zm576-42.666a42.667 42.667 0 110 85.333H512a42.667 42.667 0 110-85.333h341.333z"
              ></path>
            </svg>
          </div>
          <div>
            <div class="flow-helper-drawer-item-title">{{ item.name }}</div>
            <div class="flow-helper-drawer-item-content">{{ item.des }}</div>
          </div>
        </div>
      </a-popover>
    </a-drawer>

    <a-drawer :placement="placement" getContainer=".flow-helper" :wrapStyle="wrapStyle" :width="drawerWidth('300px')" :visible="visibleAdvice" @close="onClose">
      <div class="flow-helper-drawer-header">
        <div class="flow-helper-drawer-header-title">流程增强</div>
        <div class="flow-helper-drawer-header-content">提升审批流程灵活性，满足复杂业务场景</div>
      </div>
      <a-divider />
      <div class="flow-helper-drawer-row">
        <span class="flow-helper-drawer-row-title">动态流程</span>
        <a-switch />
      </div>
      <a-popover placement="right" trigger="click" v-for="item in dynamicItems" :key="item.id">
        <template slot="content">
          <img class="flow-helper-drawer-item-popover-img" :src="item.img" />
        </template>
        <div class="flow-helper-drawer-item" @click="popoverVisible = true">
          <div class="flow-helper-drawer-item-icon">
            <a-icon :type="item.icon" :style="item.style" />
          </div>
          <div>
            <div class="flow-helper-drawer-item-title">{{ item.name }}</div>
            <div class="flow-helper-drawer-item-content">{{ item.des }}</div>
          </div>
        </div>
      </a-popover>

      <div class="flow-helper-drawer-row" style="margin-top: 10px;">
        <span class="flow-helper-drawer-row-title">意见分支</span>
        <a-switch v-model="$store.state.flow.suggestBranchEnable" />
      </div>
      <a-popover placement="right" trigger="click" v-for="item in branchItems" :key="item.id">
        <template slot="content">
          <img class="flow-helper-drawer-item-popover-img" :src="item.img" />
        </template>
        <div class="flow-helper-drawer-item" @click="popoverVisible = true">
          <div class="flow-helper-drawer-item-icon">
            <a-icon :type="item.icon" :style="item.style" />
          </div>
          <div>
            <div class="flow-helper-drawer-item-title">{{ item.name }}</div>
            <div class="flow-helper-drawer-item-content">{{ item.des }}</div>
          </div>
        </div>
      </a-popover>

      <div class="flow-helper-drawer-row" style="margin-top: 10px;">
        <span class="flow-helper-drawer-row-title">并行节点</span>
        <a-switch v-model="$store.state.flow.parallelBranchEnable" />
      </div>
      <div class="flow-helper-drawer-row">
        <p class="flow-helper-drawer-row-foot-text">并行节点可在添加节点时使用</p>
      </div>
    </a-drawer>
  </div>
</template>
<script>
  import { flowMixin } from '../mixins/flowMixin';
  export default {
    name: 'FlowHelper',
    components: {},
    mixins: [flowMixin],
    data() {
      return {
        visibleHelper: false,
        visibleAdvice: false,
        placement: 'left',
        wrapStyle: {
          top: '50px',
        },
        simpleItems: [
          {
            id: this.uuid(),
            name: '自定义简单流程',
            des: '默认流程，所有人的审批人都一样',
            icon: '',
            img: 'https://img.alicdn.com/tfs/TB1uIc2iqNj0u4jSZFyXXXgMVXa-1230-1258.png',
          },
          {
            id: this.uuid(),
            name: '通用分条件流程',
            des: '不同部门的审批人不同',
            icon: '',
            img: 'https://img.alicdn.com/tfs/TB1uIc2iqNj0u4jSZFyXXXgMVXa-1230-1258.png',
          },
        ],
        integratedItems: [
          {
            id: this.uuid(),
            name: '假勤流程',
            des: '适用于请假、外出、加班等审批场景',
            icon: '',
            img: 'https://img.alicdn.com/tfs/TB1Yr3KRKL2gK0jSZFmXXc7iXXa-1456-1288.png',
          },
          {
            id: this.uuid(),
            name: '财务流程',
            des: '适用于付款、报销等审批场景',
            icon: '',
            img: 'https://img.alicdn.com/tfs/TB1tMgnfPMZ7e4jSZFOXXX7epXa-1522-1340.png',
          },
          {
            id: this.uuid(),
            name: '行政流程',
            des: '适用于物品领用/用印申请等审批场景',
            icon: '',
            img: 'https://img.alicdn.com/tfs/TB1U0MzRHr1gK0jSZFDXXb9yVXa-1242-1272.png',
          },
          {
            id: this.uuid(),
            name: '人事流程',
            des: '适用于转部门/调岗/离职等审批',
            icon: '',
            img: 'https://img.alicdn.com/tfs/TB1fnkgeQcx_u4jSZFlXXXnUFXa-1270-1266.png',
          },
        ],
        dynamicItems: [
          {
            id: this.uuid(),
            name: '动态流程',
            des: '审批人可修改审批单内容改变流程走向',
            icon: 'check',
            style: 'color:green',
            img: 'https://img.alicdn.com/imgextra/i2/O1CN01VRwp9P1S347GBhjEr_!!6000000002190-2-tps-562-438.png',
          },
          {
            id: this.uuid(),
            name: '默认流程',
            des: '被设置成分支条件的字段不允许审批人修改',
            icon: 'close',
            style: 'color:red',
            img: 'https://img.alicdn.com/imgextra/i1/O1CN01FaJaK226cPJIubkuC_!!6000000007682-2-tps-540-438.png',
          },
        ],
        branchItems: [
          {
            id: this.uuid(),
            name: '意见分支',
            des: '分支固定为“同意”和“不同意”。如果你同时需要对“不同意”审批的数据进行处理时选择此分支',
            icon: 'select',
            style: 'color:green',
            img: '',
          },
          {
            id: this.uuid(),
            name: '默认分支',
            des: '只对“通过”审批的数据进行分支处理',
            icon: 'carry-out',
            style: 'color:red',
            img: 'https://img.alicdn.com/imgextra/i2/O1CN01VRwp9P1S347GBhjEr_!!6000000002190-2-tps-562-438.png',
          },
        ],
      };
    },
    methods: {
      showHelperDrawer() {
        this.visibleHelper = true;
      },
      showAdviceDrawer() {
        this.visibleAdvice = true;
      },
      onClose() {
        this.visibleHelper = false;
        this.visibleAdvice = false;
      },
      onChange(e) {
        this.placement = e.target.value;
      },
    },
  };
</script>
