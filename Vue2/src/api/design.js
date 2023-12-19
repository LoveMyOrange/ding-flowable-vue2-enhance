import request from '@/api/request.js';

// 查询表单组
export function getFormGroups(param) {
  return request({
    url: 'admin/form/group',
    method: 'get',
    params: param,
  });
}

// 表单排序
export function groupItemsSort(param) {
  return request({
    url: 'admin/form/group/sort',
    method: 'put',
    data: param,
  });
}

// 更新表单组
export function updateGroup(param, method) {
  return request({
    url: 'admin/form/group',
    method: method,
    params: param,
  });
}

// 获取表单分组
export function getGroup() {
  return request({
    url: 'admin/form/group/list',
    method: 'get',
  });
}

// 更新表单
export function updateForm(param) {
  return request({
    url: 'admin/form',
    method: 'put',
    params: param,
  });
}

export function createForm(param) {
  return request({
    url: 'admin/form',
    method: 'post',
    data: param,
  });
}

// 查询表单详情
export function getFormDetail(id) {
  return request({
    url: 'admin/form/detail/' + id,
    method: 'get',
  });
}

export function getFormDetailV2(templateId) {
  return request({
    url: 'workspace/process/detail',
    method: 'get',
    params: {
      templateId,
    },
  });
}

// 更新表单详情
export function updateFormDetail(param) {
  return request({
    url: 'admin/form/detail',
    method: 'put',
    data: param,
  });
}

// 发起流程
export function startProcess(param) {
  return request({
    url: 'workspace/process/start',
    method: 'POST',
    data: param,
  });
}

// 查询我发起的
export function applyList(data) {
  return request({
    url: 'workspace/process/applyList',
    method: 'POST',
    data,
  });
}

// 查看我的待办
export function todoList(data) {
  return request({
    url: 'workspace/process/toDoList',
    method: 'POST',
    data,
  });
}
export function ccList(data) {
  return request({
    url: 'workspace/process/ccList',
    method: 'POST',
    data,
  });
}

export function submitedTaskList(data) {
  return request({
    url: 'workspace/process/submitedTaskList',
    method: 'POST',
    data,
  });
}

export function deleteProcessInstance(data) {
  return request({
    url: 'workspace/process/deleteProcessInstance',
    method: 'POST',
    data : { data}
  });
}

// 查看我的已办
export function doneList(data) {
  return request({
    url: 'workspace/process/doneList',
    method: 'POST',
    data,
  });
}
// 查询流程详情
export function getProcessInstanceInfo(processInstanceId, taskId) {
  return request({
    url: 'workspace/process/instanceInfo',
    method: 'POST',
    data: { processInstanceId, taskId },
  });
}

// 同意
export function agree(data) {
  return request({
    url: 'workspace/agree',
    method: 'POST',
    data: data,
  });
}
// 委派人
export function delegateTask(data) {
  return request({
    url: 'workspace/delegateTask',
    method: 'POST',
    data: data,
  });
}
// 委派人完成的按钮
export function resolveTask(data) {
  return request({
    url: 'workspace/resolveTask',
    method: 'POST',
    data: data,
  });
}

//  拒绝,驳回
export function refuse(data) {
  return request({
    url: 'workspace/refuse',
    method: 'POST',
    data: data,
  });
}

// 撤回
export function revoke(data) {
  return request({
    url: 'workspace/revoke',
    method: 'POST',
    data: data,
  });
}

// 转办
export function assignee(data) {
  return request({
    url: 'workspace/assignee',
    method: 'POST',
    data: data,
  });
}

// 回退
export function rollback(data) {
  return request({
    url: 'workspace/rollback',
    method: 'POST',
    data: data,
  });
}

// 加签
export function addMulti(data) {
  return request({
    url: 'workspace/addMulti',
    method: 'POST',
    data: data,
  });
}

// 查询加签人信息
export function queryMultiUsersInfo(data) {
  return request({
    url: 'workspace/queryMultiUsersInfo',
    method: 'POST',
    data: data,
  });
}
// 减签
export function deleteMulti(data) {
  return request({
    url: 'workspace/deleteMulti',
    method: 'POST',
    data: data,
  });
}
// 评论
export function comments(data) {
  return request({
    url: 'workspace/comments',
    method: 'POST',
    data: data,
  });
}

// 获取历史任务信息列表
export function historyTaskList(param) {
  return request({
    url: 'workspace/process/historyTaskList',
    method: 'GET',
    params: param,
  });
}

// 上传文件
export function upLoadFileApi(data) {
  return request({
    url: 'workspace/upLoadFile',
    method: 'POST',
    data: data,
  });
}

// 下载文件
export function downLoadFileApi(data) {
  return request({
    url: 'workspace/downLoadFile',
    method: 'POST',
    data: data,
    responseType: 'blob', //必须加，否则可能出现乱码或者文件错误，导致文件无法打开
  });
}

// 查询可退回的节点
export function getRollbackNodes(data) {
  return request({
    url: 'workspace/rollbackNodes',
    method: 'POST',
    data,
  });
}
