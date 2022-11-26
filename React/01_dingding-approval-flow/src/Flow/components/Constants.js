// 添加节点类型
export const OptionTypes = {
    APPROVER: 1,
    NOTIFIER: 2,
    BRANCH: 3,
    CONDITION: 4
}
export const NodeTypes = {...OptionTypes,START: 0}
// 节点类型默认标题名
export const OptionNames = {
    [OptionTypes.APPROVER]: '审批人',
    [OptionTypes.NOTIFIER]: '抄送人',
    [OptionTypes.CONDITION]:'条件分支'
}
// 节点模板
export const NodeTemplates = {
    [OptionTypes.APPROVER]: {
        "nodeName": "审核人",
        "error": true,
        "type": OptionTypes.APPROVER,
        "settype": 1,
        "selectMode": 0,
        "selectRange": 0,
        "directorLevel": 1,
        "replaceByUp": 0,
        "examineMode": 1,
        "noHanderAction": 1,
        "examineEndDirectorLevel": 0,
        "nodeUserList": []
    },
    [OptionTypes.NOTIFIER]: {
        "nodeName": "抄送人",
        "type": OptionTypes.NOTIFIER,
        "ccSelfSelectFlag": 1,
        "nodeUserList": []
    },
    [OptionTypes.CONDITION]: {
        "nodeName": "路由",
        "type": OptionTypes.CONDITION,
        "childNode": null,
        "conditionNodes": [
        ]
    },
    [OptionTypes.BRANCH]:{
        "nodeName": "条件1",
        "type": OptionTypes.BRANCH,
        "priorityLevel": 2,
        "conditionList": {},
        "nodeUserList": [],
        "childNode": null
    }
}

