package com.dingding.mid.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dingding.mid.dto.json.ChildNode;
import com.dingding.mid.dto.json.Properties;
import com.dingding.mid.dto.json.UserInfo;
import com.dingding.mid.enums.AssigneeTypeEnums;
import com.dingding.mid.exception.WorkFlowException;
import jakarta.annotation.Resource;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dingding.mid.common.CommonConstants.PROCESS_STATUS;
import static com.dingding.mid.common.CommonConstants.START_USER_INFO;
import static com.dingding.mid.utils.BpmnModelUtils.getChildNode;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 13:35
 */
@Component
public class CounterSignListener implements ExecutionListener {
    @Resource
    private RepositoryService repositoryService;
    @Override
    public void notify(DelegateExecution execution) {
        String currentActivityId = execution.getCurrentActivityId();
        Process mainProcess = repositoryService.getBpmnModel(execution.getProcessDefinitionId()).getMainProcess();
        UserTask userTask = (UserTask) mainProcess.getFlowElement(currentActivityId);
        String dingDing = mainProcess.getAttributeValue("http://flowable.org/bpmn", "DingDing");
        JSONObject jsonObject = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
        });
        String processJson = jsonObject.getString("processJson");
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>(){});
        List<String> assigneeList= new ArrayList<>();
        String variable=currentActivityId+"assigneeList";
        List usersValue = (List) execution.getVariable(variable);
        if(usersValue==null){
            ChildNode currentNode = getChildNode(childNode, currentActivityId);
            if(currentNode==null){
                throw new WorkFlowException("查找审批人失败,请联系管理员重试");
            }
            Properties props = currentNode.getProps();
            String assignedType = props.getAssignedType();
            Map<String, Object> nobody = props.getNobody();
            if(AssigneeTypeEnums.ASSIGN_USER.getTypeName().equals(assignedType)){
                List<UserInfo> assignedUser = props.getAssignedUser();
                for (UserInfo userInfo : assignedUser) {
                    assigneeList.add(userInfo.getId());
                }
            }
            else if(AssigneeTypeEnums.SELF_SELECT.getTypeName().equals(assignedType)){
                List<String> assigneeUsers = (List<String>) execution.getVariable(currentActivityId);
                if(assigneeUsers!=null){
                    assigneeList.addAll(assigneeUsers);
                }
            }
            else if(AssigneeTypeEnums.LEADER_TOP.getTypeName().equals(assignedType)){
                //来自于users表的admin列
                throw new WorkFlowException("此项目没有RBAC功能,所以没法做这个功能,可以看一下我写的Ruoyi-Vue-Camunda的那个版本,里面有复杂的找人代码实现");
            }
            else if(AssigneeTypeEnums.LEADER.getTypeName().equals(assignedType)){
                //向上找就行了
                throw new WorkFlowException("此项目没有RBAC功能,所以没法做这个功能,可以看一下我写的Ruoyi-Vue-Camunda的那个版本,里面有复杂的找人代码实现");
            }
            else if(AssigneeTypeEnums.ROLE.getTypeName().equals(assignedType)){
                //向上找就行了
                throw new WorkFlowException("此项目没有RBAC功能,所以没法做这个功能,可以看一下我写的Ruoyi-Vue-Camunda的那个版本,里面有复杂的找人代码实现");
            }
            else if(AssigneeTypeEnums.SELF.getTypeName().equals(assignedType)){
                String startUserJson = execution.getVariable(START_USER_INFO, String.class);
                UserInfo userInfo = JSONObject.parseObject(startUserJson, new TypeReference<UserInfo>() {
                });
                assigneeList.add(userInfo.getId());
            }
            else if(AssigneeTypeEnums.FORM_USER.getTypeName().equals(assignedType)){
                String formUser = props.getFormUser();
                List<JSONObject> assigneeUsers = execution.getVariable(formUser, List.class);
                if(assigneeUsers!=null){
                    for (JSONObject assigneeUser : assigneeUsers) {
                        assigneeList.add(assigneeUser.getString("id"));
                    }
                }

            }

            if(CollUtil.isEmpty(assigneeList)){
                String handler = MapUtil.getStr(nobody, "handler");
                if("TO_PASS".equals(handler)){
                    assigneeList.add("100000");
                    execution.setVariable(variable,assigneeList);
                }
                else if("TO_REFUSE".equals(handler)){
                    execution.setVariable("autoRefuse",Boolean.TRUE);
                    assigneeList.add("100000");
                    execution.setVariable(variable,assigneeList);
                }
                else if("TO_ADMIN".equals(handler)){
                    assigneeList.add("381496");
                    execution.setVariable(variable,assigneeList);
                }
                else if("TO_USER".equals(handler)){
                    Object assignedUserObj = nobody.get("assignedUser");
                    if(assignedUserObj!=null ){
                        List<JSONObject> assignedUser =(List<JSONObject>)assignedUserObj;
                        if(assignedUser.size()>0){
                            for (JSONObject object : assignedUser) {
                                assigneeList.add(object.getString("id"));
                            }
                            execution.setVariable(variable,assigneeList);
                        }
                        else{
                            assigneeList.add("100000");
                            execution.setVariable(variable,assigneeList);
                        }

                    }

                }
                else{
                    throw new WorkFlowException("找不到审批人,请检查配置!!!");
                }
            }
            else{
                execution.setVariable(variable,assigneeList);
            }
        }
        else{
        }
    }
}
