import { reactive } from "vue";

//审批节点默认属性
export const APPROVAL_PROPS = {
  assignedType: "ASSIGN_USER",
  mode: "AND",
  sign: false,
  nobody: {
    handler: "TO_PASS",
    assignedUser: []
  },
  timeLimit: {
    timeout: {
      unit: "H",
      value: 0
    },
    handler: {
      type: "REFUSE",
      notify: {
        once: true,
        hour: 1
      }
    }
  },
  assignedUser: [],
  formPerms: [],
  selfSelect: {
    multiple: false
  },
  leaderTop: {
    endCondition: "TOP",
    endLevel: 1,
  },
  leader: {
    level: 1
  },
  role: [],
  refuse: {
    toEnd: true, //驳回直接结束流程
    target: 'end' //驳回到指定ID的节点
  },
  formUser: ''
}

//根节点默认属性
export const ROOT_PROPS = {
  assignedUser: [],
  formPerms: []
}

//条件节点默认属性
export const CONDITION_PROPS = {
  groupsType: "OR", //条件组逻辑关系 OR、AND
  groups: [
    {
      groupType: "AND", //条件组内条件关系 OR、AND
      cids: [], //条件ID集合
      conditions: [] //组内子条件
    }
  ],
  expression: "" //自定义表达式，灵活构建逻辑关系
}

//抄送节点默认属性
export const CC_PROPS = {
  shouldAdd: false,
  assignedUser: []
}

//触发器节点默认属性
export const TRIGGER_PROPS = {
  type: 'WEBHOOK',
  http: {
    method: 'GET', //请求方法 支持GET/POST
    url: '', //URL地址，可以直接带参数
    headers: [ //http header
      {
        name: '',
        isField: true,
        value: '' //支持表达式 ${xxx} xxx为表单字段名称
      }
    ],
    contentType: 'FORM', //请求参数类型
    params: [ //请求参数
      {
        name: '',
        isField: true, //是表单字段还是自定义
        value: '' //支持表达式 ${xxx} xxx为表单字段名称
      }
    ],
    retry: 1,
    handlerByScript: false,
    success: 'function handlerOk(res) {\n  return true;\n}',
    fail: 'function handlerFail(res) {\n  return true;\n}'
  },
  email: {
    subject: '',
    to: [],
    content: ''
  }
}

//延时节点默认属性
export const DELAY_PROPS = {
  type: "FIXED", //延时类型 FIXED:到达当前节点后延时固定时长 、AUTO:延时到 dateTime设置的时间
  time: 0, //延时时间
  unit: "M", //时间单位 D天 H小时 M分钟
  dateTime: "" //如果当天没有超过设置的此时间点，就延时到这个指定的时间，到了就直接跳过不延时
}


export const store = reactive({
  nodeMap: new Map(),
  isEdit: null,
  selectedNode: {},
  design: {
    formItems: [],
    process: {
      //节点ID，当前流程内唯一，由前端随机生成
      id: "root",
      parentId: "7d89sf7d8sasf", //父级节点ID，用来向上搜索
      //节点类型，ROOT(发起人，根节点)、APPROVAL(审批)、CC(抄送)、CONDITION(条件)、CONCURRENT（并行节点）、DELAY(延时节点)、EMPTY(空节点，占位)
      type: "ROOT",
      name: "发起人",
      props: {
        assignedUser: []
      }, //节点属性，见下方props说明
      children: {}, //子节点项，内部字段与当前结构相同，为条件/并行 节点的组合末端节点
    }
  },
})


export default {
  APPROVAL_PROPS, CC_PROPS, DELAY_PROPS, CONDITION_PROPS, ROOT_PROPS, TRIGGER_PROPS
}
