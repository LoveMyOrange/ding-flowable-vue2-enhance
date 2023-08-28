<template>
  <div>
    <div class="el-row" style="margin-left: -10px; margin-right: -10px">
      <div
        class="el-col el-col-15"
        style="
          padding-left: 10px;
          padding-right: 10px;
          border-right: 1px solid rgb(232, 232, 232);
        "
      >
        <form-render
          class="process-form"
          ref="form"
          :forms="forms"
          v-model="formData"
        />
      </div>
      <div
        class="el-col el-col-9"
        style="padding-left: 10px; padding-right: 10px"
      >
        <div>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in cc"
              :key="index"
              :icon="activity.icon"
              size="large"
              class="task"
            >
              <div
                class="process-node-render"
                v-if="!activity.hasOwnProperty('options')"
              >
                <div>
                  <div style="font-size: 16px">{{ activity.title }}</div>
                  <span style="color: rgb(168, 173, 175)">{{
                    activity.desc
                  }}</span>
                </div>
                <div style="display: flex">
                  <div
                    class="avatar show-y"
                    v-for="(user, index) in activity.users"
                    :key="index"
                  >
                    <div class="a-img">
                      <el-avatar
                        style="height: 38px; width: 38px; line-height: 38px"
                        :src="user.avatar"
                      ></el-avatar>
                      <i
                        class="close el-icon-close"
                        v-if="activity.isEdit"
                        @click="delUser(activity.users, user)"
                      ></i>
                      <i class="status" style="display: none"></i>
                    </div>
                    <span class="name line">{{ user.name }}</span>
                  </div>
                  <span
                    class="add-user"
                    v-if="
                      activity.isEdit &&
                      (activity.multiple || 0 === activity.users.length)
                    "
                    @click="addUser(activity)"
                    ><i class="el-icon-plus"></i>
                    <div>添加</div></span
                  >
                </div>
              </div>
              <el-radio-group v-model="activity.id" size="mini" v-else>
                <el-radio-button
                  :label="d.title"
                  :value="d.id"
                  v-for="(d, index) in activity.options"
                  :key="index"
                ></el-radio-button>
              </el-radio-group>
            </el-timeline-item>
          </el-timeline>
        </div>
        <org-picker
          :type="this.selectedNode.type || 'user'"
          :multiple="this.selectedNode.multiple || !1"
          ref="orgPicker"
          :selected="this.selectedNode.users || []"
          @ok="selected"
        ></org-picker>
      </div>
    </div>
    <div></div>
  </div>
</template>

<script>
import FormRender from "@/views/common/form/FormRender";
import { getFormDetailV2 } from "@/api/design";
import OrgPicker from "@/components/common/OrgPicker";

export default {
  name: "SponsorProcess",
  components: { FormRender, OrgPicker },
  props: {
    code: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      loading: false,
      formData: {},
      select: [],
      selectedNode: {},
      processUsers: {},
      form: {
        formId: "",
        formName: "",
        logo: {},
        formItems: [],
        process: {},
        remark: "",
      },
      loginUser: {},
      conditionFormItem: new Set(),
      activities: [],
      cc: [],
    };
  },
  mounted() {
    this.loadLoginUser();
    this.loadFormInfo(this.code);
  },
  watch: {
    // 被侦听的变量count
    formData: {
      // 对对象进行深度监听
      handler(nv) {
        this.startProcess(nv, (this.cc = []));
      },
      immediate: true,
      deep: true,
    },
  },
  computed: {
    forms() {
      return this.$store.state.design.formItems;
    },
    _value: {
      get: function () {
        return this.value;
      },
      set: function (e) {
        this.$emit("input", e);
      },
    },
  },
  methods: {
    loadLoginUser() {
      this.loginUser = JSON.parse(sessionStorage.getItem("user"));
    },
    loadFormInfo(formId) {
      this.loading = true;
      getFormDetailV2(formId)
        .then((rsp) => {
          this.loading = false;
          let form = rsp.data.result;
          form.logo = JSON.parse(form.logo);
          form.settings = JSON.parse(form.settings);
          form.formItems = JSON.parse(form.formItems);
          form.process = JSON.parse(form.process);
          this.form = form;
          //构建表单及校验规则
          this.$store.state.design = form;
          this.startProcess(form.process, this.formData);
        })
        .catch((err) => {
          this.loading = false;
          this.$message.error(err);
        });
    },
    validate(call) {
      this.$refs.form.validate(call);
    },
    getFormData() {
      return this.formData;
    },
    getProcessUser() {
      return this.processUsers;
    },
    getForm() {
      return this.form;
    },
    addUser(e) {
      (this.selectedNode = e), this.$refs.orgPicker.show();
    },
    delUser(e, t) {
      e.splice(t, 1);
      this.processUsers = e;
    },
    startProcess() {
      this.cc = [];
      this.getProcess(this.form.process, this.activities, this.cc);

      this.cc.push({
        title: "结束",
        name: "END",
        icon: "el-icon-success",
        isEdit: false,
      });
    },
    selected(e) {
      var t = this;
      t.processUsers[t.selectedNode.id] = [];

      e.forEach((user) => {
        var ddd = t.selectedNode.users.findIndex((t) => {
          return t.id === user.id;
        });
        if (ddd === -1) {
          t.selectedNode.users.push(user);
          t.processUsers[t.selectedNode.id].push(user);
          t.$set(e, "isEdit", true);
        }
      });
    },
    getProcess(process, data, cc) {
      if (null != process && undefined != process) {
        if ("ROOT" === process.type) {
          //发起人节点
          this.getRootNode(cc, process);
        } else if ("APPROVAL" === process.type) {
          //审批节点
          this.getApprovalNode(cc, process);
        } else if ("CC" === process.type) {
          this.getCcNode(cc, process);
        } else if ("CONDITIONS" === process.type) {
          //判断是否符合条件符合走条件分支,否则继续递归子分支
          if (null != process.branchs && undefined != process.branchs) {
            this.getConditionNode(cc, process);
          }
        } else if ("CONCURRENTS" === process.type) {
          this.getConcurrentNode(cc, process);
        }
        if (null != process.children && undefined != process.children) {
          this.getProcess(process.children, data, cc);
        }
      }
    },
    //封装开始节点
    getRootNode(cc, process) {
      cc.push({
        id: process.id,
        title: process.name,
        name: "发起人",
        icon: "el-icon-user-solid",
        isEdit: false,
        users: [this.loginUser],
      });
    },
    //封装审批节点
    getApprovalNode(cc, process) {
      var data = {
        id: process.id,
        title: process.name,
        name: "审批人",
        icon: "el-icon-s-check",
        isEdit: false,
        multiple: false,
        mode: process.props.mode,
        users: [],
        desc: "",
      };
      //判断审批人类型
      switch (process.props.assignedType) {
        case "ASSIGN_USER":
          data.users = this.$deepCopy(process.props.assignedUser);
          data.desc = "指定审批人";
          break;
        case "ASSIGN_LEADER":
          data.desc = "指定部门的领导";
          break;
        case "SELF":
          data.users = [this.loginUser];
          data.desc = "发起人自己审批";
          break;
        case "SELF_SELECT":
          data.isEdit = true;
          data.multiple = process.props.selfSelect.multiple || false;
          data.desc = "自选审批人";
          break;
        case "LEADER_TOP":
          data.desc = "连续多级主管审批";
          break;
        case "LEADER":
          data.desc =
            1 === process.props.leader.level
              ? "直接主管审批"
              : "第".concat(process.props.leader.level, "级主管审批");
          break;
        case "ROLE":
          data.desc = "由角色[".concat(
            (process.props.role || []).map(function (e) {
              return e.name;
            }),
            "]审批"
          );
          break;
        case "REFUSE":
          data.desc = "流程此处将被自动驳回";
          break;
      }
      cc.push(data);
    },
    getCcNode(cc, process) {
      var data = {
        id: process.id,
        title: process.name,
        icon: "el-icon-s-promotion",
        name: "抄送人",
        isEdit: process.props.shouldAdd,
        type: "org",
        multiple: true,
        desc: process.props.shouldAdd ? "可添加抄送人" : "",
        users: this.$deepCopy(process.props.assignedUser),
      };
      cc.push(data);
    },
    getConditionNode(cc, process) {
      for (var r = null, s = 0; s < process.branchs.length; s++) {
        for (
          var a = process.branchs[s], n = false, o = 0, i = 0;
          i < a.props.groups.length;
          i++
        ) {
          if (
            ((n = this.getConditionResultByGroup(a.props.groups[i])),
            "OR" === a.props.groupsType && n)
          ) {
            r = a;
            break;
          }
          "AND" === a.props.groupsType && n && o++;
        }

        if (r) {
          break;
        }
        if (o === a.props.groups.length) {
          r = a;
          break;
        }
      }
      var d = [];
      console.log(
        "符合分支条件,继续执行递归,获取符合条件下节点下的子节点!" +
          JSON.stringify(r)
      );
      r
        ? this.getProcess(r, d, cc)
        : console.log(
            "条件节点 "
              .concat(process.id, " => ")
              .concat(process.name, " 均不满足，无法继续"),
            process
          );
    },
    getConcurrentNode(cc, process) {
      var data = {
        id: process.id,
        title: process.name,
        name: "并行分支",
        icon: "el-icon-s-operation",
        isEdit: false,
        active: process.branchs[0].id,
        options: [],
        desc: "切换分支可显示对应执行流程",
        branchs: {},
      };
      cc.push(data);
      process.branchs.forEach((b) => {
        data.options.push({ id: b.id, title: b.name });
        this.$set(data.branchs, b.id, []);
        var d = [];
        this.getProcess(b.children, d, data.branchs[b.id]);
      });
    },
    getConditionResultByGroup: function (e) {
      var t = this,
        r = !1,
        s = 0;
      e.conditions.forEach(function (e) {
        return t.conditionFormItem.add(e.id);
      });

      for (var a = 0; a < e.conditions.length; a++) {
        var n = e.conditions[a];
        switch (n.valueType) {
          case "Number":
            console.log("这是number类型判断");
            r = this.numberCompare(n);
            break;
          case "String":
            console.log("这是string类型判断");
            break;
          case "Date":
            console.log("这是data类型判断");

            break;
          case "User":
            console.log("这是user类型判断");

            break;
          case "Array":
            console.log("这是array类型判断");

            break;
        }
        if (r && "OR" === e.groupType) break;
        r && "AND" === e.groupType && s++;
      }
      return "AND" === e.groupType && (r = s === e.conditions.length), r;
    },
    numberCompare: function (e) {
      var t = this.formData[e.id];
      switch (e.compare) {
        case ">":
          return t > parseFloat(e.value[0]);
        case "<":
          return t < parseFloat(e.value[0]);
        case "=":
          return t === parseFloat(e.value[0]);
        case ">=":
          return t >= parseFloat(e.value[0]);
        case "<=":
          return t <= parseFloat(e.value[0]);
        case "B":
          return t > parseFloat(e.value[0]) && t < parseFloat(e.value[1]);
        case "AB":
          return t >= parseFloat(e.value[0]) && t < parseFloat(e.value[1]);
        case "BA":
          return t > parseFloat(e.value[0]) && t <= parseFloat(e.value[1]);
        case "ABA":
          return t >= parseFloat(e.value[0]) && t <= parseFloat(e.value[1]);
        case "IN":
          return e.value.indexOf(String(t)) > -1;
        default:
          return !1;
      }
    },
  },
};
</script>

<style lang="less" scoped>
.process-form {
  /deep/ .el-form-item__label {
    padding: 0 0;
  }
}

.task {
  height: 60px;
}

.el-timeline-item .el-timeline-item__content .process-node-render > div {
  position: absolute;
  display: inline-block;
}

.el-timeline-item
  .el-timeline-item__content
  .process-node-render
  > div:last-child {
  right: 0;
  top: -10px;
}

.avatar {
  display: flex;
  flex-direction: row;
  position: relative;
  align-items: center;
}

.show-y {
  justify-content: center;
  flex-direction: column !important;
}

.show-y,
.w-h-center {
  display: flex;
  align-items: center;
}

.avatar .a-img {
  display: flex;
  border-radius: 50%;
  flex-direction: column;
  justify-content: center;
  background: #fff;
  position: relative;
}

.close {
  position: absolute;
  top: 0;
  right: 0;
  color: #fff;
  cursor: pointer;
  border-radius: 50%;
  background: #000;
}

.status {
  position: absolute;
  bottom: -4px;
  right: -8px;
  border-radius: 50%;
  font-size: 15px;
  background: #fff;
  border: 2px solid #fff;
}

.avatar .name {
  text-align: center;
  color: #19191a;
  font-size: 14px;
  margin-left: 10px;
}

.show-y .name {
  margin-left: 0 !important;
}

.line {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.add-user {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.add-user i {
  padding: 10px;
  font-size: 1.1rem;
  border-radius: 50%;
  border: 1px dashed #8c8c8c;
  cursor: pointer;
}

.el-timeline-item .el-timeline-item__icon {
  color: #babcc1;
  font-size: 16px;
}

.border .el-dialog__header {
  border-bottom: 1px solid #e8e8e8;
}
</style>
