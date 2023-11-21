<template>
  <div>
    <el-button type="primary" @click="selectOrg" round>
      <el-icon><Plus /></el-icon>
      选择抄送人</el-button
    >
    <div class="option">
      允许发起人添加抄送人:
      <el-switch v-model="config.shouldAdd" />
    </div>
    <div style="margin-top: 20px">
      <el-tag
        class="org-item"
        :type="org.type === 'dept' ? '' : 'info'"
        v-for="(org, index) in select"
        :key="index + '_org'"
        closable
        size="small"
        @close="removeOrgItem(index)"
      >
        {{ org.name }}
      </el-tag>
    </div>
    <orgPicker
      v-model="showOrgSelect"
      @close="closeSelect"
      :selected="select"
      @selectOver="selected"
    ></orgPicker>
  </div>
</template>

<script>
import orgPicker from "@/views/common/organizationPicker.vue";
export default {
  name: "CNodeConfig.vue",
  components: { orgPicker },
  props: {
    config: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  computed: {
    select() {
      return this.config.assignedUser || [];
    },
  },
  data() {
    return {
      showOrgSelect: false,
    };
  },
  methods: {
    closeSelect() {
      this.showOrgSelect = false;
    },
    selectOrg() {
      this.showOrgSelect = true;
    },
    selected(select) {
      console.log(select);
      this.showOrgSelect = false;
      select.forEach((val) => this.select.push(val));
    },
    removeOrgItem(index) {
      this.select.splice(index, 1);
    },
  },
};
</script>

<style lang="less" scoped>
.option {
  color: #606266;
  margin-top: 20px;
  font-size: small;
}

.desc {
  font-size: small;
  color: #8c8c8c;
}
.org-item {
  margin: 5px;
}
</style>
