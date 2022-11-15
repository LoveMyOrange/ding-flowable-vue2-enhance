<template>
  <a-drawer
    v-if="node.attr"
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
      <img :src="branchIcon2" class="anticon" />
      <span class="flow-ant-drawer-title">
        <EditName v-model="node.name" />
      </span>
    </template>
    <div class="flow-setting-module">
      <div class="flow-setting-content">
        <div v-if="node.attr.showPriorityLevel" class="flow-setting-item">
          <p class="flow-setting-item-title">分支等级</p>
          <a-select v-model="node.attr.priorityLevel" :size="size" placeholder="请选择等级" :options="levelOptions" class="w-fill"></a-select>
        </div>
        <div class="flow-setting-item">
          <p class="flow-setting-item-title">分支类型</p>
          <a-radio-group v-model="node.attr.branchType">
            <a-radio :value="branchType.value" v-for="(branchType, i) in branchTypes" :key="i">
              {{ branchType.label }}
            </a-radio>
          </a-radio-group>
        </div>
        <div v-if="node.attr.branchType == 1" class="flow-setting-item">
          <p class="flow-setting-item-title">条件规则</p>
          <div class="flow-setting-condition-box">
            <div v-for="(group, i) in node.conditionGroup" :key="i">
              <div class="flow-setting-condition-group">
                <div class="flow-setting-condition-item" v-for="(condition, k) in group.conditions" :key="k">
                  <a-select v-model="condition.columnValue" :size="size" style="width: 40%" placeholder="字段" @change="handleChange">
                    <a-select-opt-group label="基础字段">
                      <a-select-option :value="column.value" v-for="(column, i) in columns" :key="i">{{ column.label }}</a-select-option>
                    </a-select-opt-group>
                    <a-select-opt-group label="表单字段">
                      <a-select-option :value="column.value" v-for="(column, i) in formColumns" :key="i">{{ column.label }}</a-select-option>
                    </a-select-opt-group>
                  </a-select>
                  <div class="flow-setting-condition-option">
                    <!-- 判断(操作)符 -->
                    <FlowSimpleSelect v-model="condition.optType" :name.sync="condition.optTypeName" :datas="optTypes" labelName="label" style="width: 26%" />
                    <!-- 值类型 -->
                    <FlowSimpleSelect v-model="condition.valueType" :datas="valueTypes" labelName="label" style="width: 26%" @change="condition.conditionValue = []" />
                    <!-- 值 -->
                    <div class="flow-setting-condition-value">
                      <!-- 动态值 -->
                      <FlowSelect
                        v-if="condition.valueType == 2"
                        v-model="condition.conditionValue"
                        :name.sync="condition.conditionValueName"
                        :datas="dynamicValueTypes"
                        labelName="label"
                      />
                      <!-- 流程值 -->
                      <FlowSelect
                        v-else-if="condition.valueType == 3"
                        v-model="condition.conditionValue"
                        :name.sync="condition.conditionValueName"
                        :datas="flowValueTypes"
                        labelName="label"
                      />
                      <!-- 数据源 -->
                      <FlowSelect
                        v-else-if="condition.valueType == 4"
                        v-model="condition.conditionValue"
                        :name.sync="condition.conditionValueName"
                        :datas="columns"
                        labelName="label"
                      />
                      <!-- 固定 -->
                      <FlowInput v-else v-model="condition.conditionValue" :name.sync="condition.conditionValueName" :size="size" />
                    </div>
                  </div>
                  <div class="flow-setting-condition-del" @click="delCondition(1, group, condition)">
                    <a-icon type="delete" theme="filled" />
                  </div>
                </div>
                <div class="flow-setting-condition-add" @click="addCondition(1, group)">
                  <a-icon type="plus-circle" theme="filled" />
                  <span style="margin-left: 5px">且条件</span>
                </div>
              </div>
              <div v-if="node.conditionGroup.length > 1 && i != node.conditionGroup.length - 1" class="flow-setting-condition-group-connector">或</div>
            </div>
            <div class="flow-setting-condition-add" @click="addGroup(1)">
              <a-icon type="plus-circle" theme="filled" />
              <span style="margin-left: 5px">或条件</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    {{ node }}
    <FlowDrawerFooter @close="onClose" @save="onSave" />
  </a-drawer>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  import EditName from '../../Common/EditName.vue';
  import FlowSelect from '../../Component/FlowSelect.vue';
  import FlowSimpleSelect from '../../Component/FlowSimpleSelect.vue';
  import FlowInput from '../../Component/FlowInput.vue';
  import FlowDrawerFooter from '../../Common/DrawerFooter.vue';
  export default {
    name: 'FlowBranchSetting',
    components: { EditName, FlowSelect, FlowSimpleSelect, FlowInput, FlowDrawerFooter },
    mixins: [flowMixin],
    data() {
      return {
        node: {},
        visible: false,
        headerStyle: {
          'background-color': '#5ccc98',
          'border-radius': '0px 0px 0 0',
        },
        // 等级
        levelOptions: [],
        branchTypes: [
          { label: '规则', value: 1 },
          { label: '公式', value: 2 },
          { label: '其他', value: 3 },
        ],
        columns: [
          { label: '姓名', value: '姓名' },
          { label: '工号', value: '工号' },
          { label: '部门', value: '部门' },
          { label: 'Base地', value: 'Base地' },
          { label: '所属体系', value: '所属体系' },
          { label: '归属地', value: '归属地' },
        ],
        formColumns: [{ label: '加班类型', value: '加班类型' }],
        optTypes: [
          { label: '等于', value: 'eq' },
          { label: '不等于', value: 'ne' },
          { label: '大于', value: 'gt' },
          { label: '大于等于', value: 'ge' },
          { label: '小于', value: 'lt' },
          { label: '小于等于', value: 'le' },
          /*  { label: '为空', value: '7' },
          { label: '不为空', value: '8' }, */
        ],
        // 值类型
        valueTypes: [
          { label: '固定', value: '1' },
          { label: '动态值', value: '2' },
          { label: '流程值', value: '3' },
          /* { label: '数据源', value: '4' }, */
        ],
        // 动态值类型
        dynamicValueTypes: [
          { label: '当前员工', value: '1' },
          { label: '当前员工工号', value: '2' },
          { label: '当前部门', value: '3' },
          { label: '当前组织', value: '4' },
          { label: '下级部门', value: '5' },
          { label: '上级部门', value: '6' },
          { label: '当前日期', value: '7' },
          { label: '当前时间', value: '8' },
        ],
        // 流程值类型
        flowValueTypes: [
          { label: '流程状态', value: '1' },
          { label: '流程创建人', value: '2' },
        ],
        // 表单数据
        dataSourceOptions: [
          {
            value: '1',
            label: '本表单',
            children: [
              { label: '姓名', value: '姓名' },
              { label: '工号', value: '工号' },
              { label: '部门', value: '部门' },
              { label: 'Base地', value: 'Base地' },
              { label: '所属体系', value: '所属体系' },
              { label: '归属地', value: '归属地' },
            ],
          },
        ],
      };
    },
    methods: {
      afterVisibleChange(val) {
        console.log('visible', val);
      },
      showDrawer(node, routeNode) {
        this.visible = true;
        this.node = node;
        // 等级
        if (node.attr.showPriorityLevel) {
          this.levelOptions = [];
          routeNode.conditionNodes.forEach((item, index) => {
            let priorityLevel = index + 1;
            this.levelOptions.push({ label: '优先' + priorityLevel, value: priorityLevel });
          });
        }
      },
      onClose() {
        this.visible = false;
        this.$emit('close');
      },
      handleChange() {},
      addGroup(type) {
        if (type == 1) {
          this.node.conditionGroup.push({
            id: this.uuid(),
            condition: 'OR',
            conditions: [
              {
                id: this.uuid(),
                columnId: '姓名',
                columnName: '姓名',
                columnValue: '姓名',
                columnType: undefined,
                optType: 'eq',
                optTypeName: '等于',
                valueType: '1',
                conditionValue: [],
                conditionValueName: [],
              },
            ],
          });
        }
      },
      addCondition(type, currGroup) {
        if (type == 1) {
          this.node.conditionGroup.forEach((group) => {
            if (currGroup.id == group.id) {
              group.conditions.push({
                id: this.uuid(),
                columnId: undefined,
                columnName: undefined,
                columnValue: undefined,
                columnType: undefined,
                optType: undefined,
                optTypeName: undefined,
                valueType: undefined,
                conditionValue: [],
                conditionValueName: [],
              });
            }
          });
        }
      },
      delCondition(type, currGroup, CurrCondition) {
        if (type == 1) {
          this.node.conditionGroup.forEach((group, k) => {
            if (currGroup.id == group.id) {
              group.conditions.forEach((condition, index) => {
                if (CurrCondition.id == condition.id) {
                  group.conditions.splice(index, 1);
                  // 当前组没有条件了，当前组也需要删除
                  if (group.conditions.length == 0) {
                    this.node.conditionGroup.splice(k, 1);
                  }
                }
              });
            }
          });
        }
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
        let content = '';
        if (this.node.attr.branchType == 1) {
          this.node.conditionGroup.forEach((group, j) => {
            if (j != 0) {
              content += ' 或 ';
            }
            if (group.conditions.length > 0) {
              group.conditions.forEach((condition, i) => {
                const conditionValueName = condition.conditionValueName[0];
                if (conditionValueName) {
                  if (i != 0) {
                    content += ' 且 ';
                  }
                  content += '[' + condition.columnValue + ' ' + condition.optTypeName + ' ' + conditionValueName + ']';
                }
              });
            }
          });
        } else {
          content += '任意(其他)';
        }
        this.$store.dispatch('flow/updateNode', { currNode: this.node, field: 'content', value: null });
        this.$store.dispatch('flow/updateNode', { currNode: this.node, field: 'error', value: true });
        if (content) {
          console.info('content', content);
          this.$store.dispatch('flow/updateNode', { currNode: this.node, field: 'error', value: false });
          this.$store.dispatch('flow/updateNode', { currNode: this.node, field: 'content', value: content });
          this.onClose();
        }
      },
    },
  };
</script>
