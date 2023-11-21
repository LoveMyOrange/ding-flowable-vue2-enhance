<template>
  <div>
    <p class="desc">选择能发起该审批的人员/部门，不选则默认开放给所有人</p>
    <el-button @click="selectOrg" type="primary" round>
      <el-icon style="margin-right: 4px;"><Plus /></el-icon>
      请选择</el-button
    >
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
    <org-picker
      :show="showOrgSelect"
      @close="closeSelect"
      :selected="select"
      @selectOver="selected"
    ></org-picker>
  </div>
</template>

<script>
import orgPicker from "@/views/common/organizationPicker.vue";
export default {
  name: "RootConfig",
  components: { orgPicker },
  props: {
    config: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      showOrgSelect: false,
    };
  },
  computed: {
    select() {
      return this.config.assignedUser;
    },
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
.desc {
  font-size: small;
  color: #8c8c8c;
}
.org-item {
  margin: 5px;
}
</style>
