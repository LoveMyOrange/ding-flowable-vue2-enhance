package com.dingding.mid.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dingding.mid.dto.json.ChildNode;
import com.dingding.mid.dto.json.Properties;
import com.dingding.mid.dto.json.UserInfo;
import com.dingding.mid.entity.Users;
import com.dingding.mid.enums.AssigneeTypeEnums;
import com.dingding.mid.exception.WorkFlowException;
import com.dingding.mid.service.DepartmentsService;
import com.dingding.mid.service.UserService;
import com.dingding.mid.utils.SpringContextHolder;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dingding.mid.common.CommonConstants.PROCESS_STATUS;
import static com.dingding.mid.common.CommonConstants.START_USER_INFO;
import static com.dingding.mid.common.WorkFlowConstants.*;
import static com.dingding.mid.utils.BpmnModelUtils.getChildNode;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 13:35
 */
@Component
public class CounterSignListener implements ExecutionListener {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private DepartmentsService departmentsService;
    @Resource
    private UserService userService;
    @Override
    public void notify(DelegateExecution execution) {
        String currentActivityId = execution.getCurrentActivityId();
        Process mainProcess = repositoryService.getBpmnModel(execution.getProcessDefinitionId()).getMainProcess();
        UserTask userTask = (UserTask) mainProcess.getFlowElement(currentActivityId);
        String dingDing = mainProcess.getAttributeValue(FLOWABLE_NAME_SPACE, FLOWABLE_NAME_SPACE_NAME);
        JSONObject jsonObject = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
        });
        String processJson = jsonObject.getString(VIEW_PROCESS_JSON_NAME);
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>(){});
        List<String> assigneeList= new ArrayList<>();
        String variable=currentActivityId+ASSIGNEE_LIST_SUFFIX;
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
                /**
                 endCondition: "TOP", //结束条件 TOP 直到最上级主管、
                 level 指定不超过多少级主管
                 endLevel: 1, //指定级别主管审批后结束本节点
                 */
                Map<String, Object> leaderTop = props.getLeaderTop();
                String endCondition = MapUtil.getStr(leaderTop, "endCondition");
                Integer endLevel = MapUtil.getInt(leaderTop, "endLevel");
                Integer level = MapUtil.getInt(leaderTop, "level");
                UserService userService = SpringContextHolder.getBean(UserService.class);
                String startUserJson = execution.getVariable(START_USER_INFO, String.class);
                UserInfo userInfo = JSONObject.parseObject(startUserJson, new TypeReference<UserInfo>() {
                });
                String id = userInfo.getId();
                Users users = userService.getById(id);
                //todo 因为此项目没有级联结构,自行递归获取1~10级某一级,不会有人这都不会吧,嘤嘤嘤
                Integer admin = users.getAdmin();
                if(admin!=null){
                    assigneeList.add(admin+"");
                }
            }
            else if(AssigneeTypeEnums.LEADER.getTypeName().equals(assignedType)){
                Map<String, Object> leaderTop = props.getLeader();
                Integer level = MapUtil.getInt(leaderTop, "level");
                UserService userService = SpringContextHolder.getBean(UserService.class);
                String startUserJson = execution.getVariable(START_USER_INFO, String.class);
                List<UserInfo> userInfos = JSONObject.parseObject(startUserJson, new TypeReference<List<UserInfo>>() {
                });
                UserInfo userInfo = userInfos.get(0);
                String id = userInfo.getId();
                Users users = userService.getById(id);
                //todo 因为此项目没有级联结构,自行递归获取1~10级某一级,不会有人这都不会吧,嘤嘤嘤
                Integer admin = users.getAdmin();
                if(admin!=null){
                    assigneeList.add(admin+"");
                }
            }
            else if(AssigneeTypeEnums.ROLE.getTypeName().equals(assignedType)){
                throw new WorkFlowException("前端现在角色选择器有问题,等待联调");
//                LambdaQueryWrapper<Users> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//                lambdaQueryWrapper.in(Users::getDepartmentIds,roleIds);
//                UserService userService = SpringContextHolder.getBean(UserService.class);
//                List<Users> list = userService.list(lambdaQueryWrapper);
//                for (Users users : list) {
//                    flwHisTaskActors.add(FlwTaskActor.ofUser(users.getUserId()+"",users.getUserName()));
//                }
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
                        assigneeList.add(assigneeUser.getString(VIEW_ID_NAME));
                    }
                }

            }

            if(CollUtil.isEmpty(assigneeList)){
                String handler = MapUtil.getStr(nobody, ASSIGNEE_NULL_ACTION_NAME);
                if(TO_PASS_ACTION.equals(handler)){
                    assigneeList.add(DEFAULT_NULL_ASSIGNEE);
                    execution.setVariable(variable,assigneeList);
                }
                else if(TO_REFUSE_ACTION.equals(handler)){
                    execution.setVariable(AUTO_REFUSE_STR,Boolean.TRUE);
                    assigneeList.add(DEFAULT_NULL_ASSIGNEE);
                    execution.setVariable(variable,assigneeList);
                }
                else if(TO_ADMIN_ACTION.equals(handler)){
                    assigneeList.add(DEFAULT_ADMIN_ASSIGNEE);
                    execution.setVariable(variable,assigneeList);
                }
                else if(TO_USER_ACTION.equals(handler)){
                    Object assignedUserObj = nobody.get(VIEW_ASSIGNEE_USER_NAME);
                    if(assignedUserObj!=null ){
                        List<JSONObject> assignedUser =(List<JSONObject>)assignedUserObj;
                        if(assignedUser.size()>0){
                            for (JSONObject object : assignedUser) {
                                assigneeList.add(object.getString(VIEW_ID_NAME));
                            }
                            execution.setVariable(variable,assigneeList);
                        }
                        else{
                            assigneeList.add(DEFAULT_NULL_ASSIGNEE);
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
