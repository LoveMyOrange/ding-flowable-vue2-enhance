package cn.iocoder.yudao.module.bpm.framework.flowable.core.listener;

import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.module.bpm.constants.WorkFlowConstants;
import cn.iocoder.yudao.module.bpm.service.definition.BpmTaskAssignRuleService;
import cn.iocoder.yudao.module.bpm.service.message.dto.CamundaProcessInstanceDTO;
import cn.iocoder.yudao.module.bpm.service.message.dto.CamundaTaskDTO;
import cn.iocoder.yudao.module.bpm.service.task.BpmProcessInstanceService;
import cn.iocoder.yudao.module.bpm.service.task.BpmTaskService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.model.bpmn.instance.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class CamundaGlobalListenerDelegate implements ExecutionListener, TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        BpmTaskService bpmTaskService = SpringUtil.getBean(BpmTaskService.class);
        Map<String, Object> variables = delegateTask.getVariables();
        String processInstanceName = MapUtil.getStr(variables, WorkFlowConstants.PROCESS_INSTANCE_NAME);
        Long startUserId = MapUtil.getLong(variables, WorkFlowConstants.PROCESS_INSTANCE_STARTER_USER_ID);
        if(TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())){
            CamundaTaskDTO camundaTaskDTO = new CamundaTaskDTO();
            camundaTaskDTO.setTaskId(delegateTask.getId());
            camundaTaskDTO.setProcessInstanceId(delegateTask.getProcessInstanceId());
            camundaTaskDTO.setAssigneeUserId(Long.valueOf(delegateTask.getAssignee()));
            camundaTaskDTO.setCreateTime(delegateTask.getCreateTime());
            camundaTaskDTO.setName(delegateTask.getName());
            bpmTaskService.createTaskExt(camundaTaskDTO);
        }
        else if(TaskListener.EVENTNAME_ASSIGNMENT.equals(delegateTask.getEventName())){
            CamundaTaskDTO camundaTaskDTO = new CamundaTaskDTO();
            camundaTaskDTO.setTaskId(delegateTask.getId());
            camundaTaskDTO.setProcessInstanceId(delegateTask.getProcessInstanceId());
            camundaTaskDTO.setAssigneeUserId(Long.valueOf(delegateTask.getAssignee()));
            camundaTaskDTO.setCreateTime(delegateTask.getCreateTime());
            camundaTaskDTO.setName(delegateTask.getName());
            camundaTaskDTO.setProcessStartUserId(startUserId);
            camundaTaskDTO.setProcessDefinitionName(processInstanceName);
            bpmTaskService.updateTaskExtAssign(camundaTaskDTO);
        }
        else if(TaskListener.EVENTNAME_COMPLETE.equals(delegateTask.getEventName())){
            CamundaTaskDTO camundaTaskDTO = new CamundaTaskDTO();
            camundaTaskDTO.setTaskId(delegateTask.getId());
            camundaTaskDTO.setProcessInstanceId(delegateTask.getProcessInstanceId());
            camundaTaskDTO.setAssigneeUserId(Long.valueOf(delegateTask.getAssignee()));
            camundaTaskDTO.setCreateTime(delegateTask.getCreateTime());
            camundaTaskDTO.setName(delegateTask.getName());
            camundaTaskDTO.setProcessStartUserId(startUserId);
            camundaTaskDTO.setProcessDefinitionName(processInstanceName);
            bpmTaskService.updateTaskExtComplete(camundaTaskDTO);
        }
    }


    @Override
    public void notify(DelegateExecution execution) throws Exception {
        BpmProcessInstanceService bpmProcessInstanceService = SpringUtil.getBean(BpmProcessInstanceService.class);
        BpmTaskAssignRuleService bpmTaskAssignRuleService = SpringUtil.getBean(BpmTaskAssignRuleService.class);
        FlowElement bpmnModelElementInstance = execution.getBpmnModelElementInstance();
        Map<String, Object> variables = execution.getVariables();
        String processInstanceName = MapUtil.getStr(variables, WorkFlowConstants.PROCESS_INSTANCE_NAME);
        Long startUserId = MapUtil.getLong(variables, WorkFlowConstants.PROCESS_INSTANCE_STARTER_USER_ID);
        if(bpmnModelElementInstance instanceof UserTask){
            UserTask userTask =(UserTask)bpmnModelElementInstance;
            LoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
            if(loopCharacteristics==null){
                Set<Long> users = bpmTaskAssignRuleService.calculateTaskCandidateUsers(execution);
                List<Long> userList= new ArrayList<>(users);
                execution.setVariable("assignee",userList.get(0));
            }
        }
        else if(bpmnModelElementInstance instanceof StartEvent){
            CamundaProcessInstanceDTO camundaProcessInstanceDTO = new CamundaProcessInstanceDTO();
            camundaProcessInstanceDTO.setProcessInstanceId(execution.getProcessInstanceId());
            camundaProcessInstanceDTO.setProcessDefinitionName(processInstanceName);
            camundaProcessInstanceDTO.setProcessStartUserId(startUserId);
            camundaProcessInstanceDTO.setProcessDefinitionId(execution.getProcessDefinitionId());
            bpmProcessInstanceService.createProcessInstanceExt(camundaProcessInstanceDTO);
        }
        else if(bpmnModelElementInstance instanceof EndEvent) {
            CamundaProcessInstanceDTO camundaProcessInstanceDTO = new CamundaProcessInstanceDTO();
            camundaProcessInstanceDTO.setProcessInstanceId(execution.getProcessInstanceId());
            camundaProcessInstanceDTO.setProcessDefinitionName(processInstanceName);
            camundaProcessInstanceDTO.setProcessStartUserId(startUserId);
            camundaProcessInstanceDTO.setProcessDefinitionId(execution.getProcessDefinitionId());
            bpmProcessInstanceService.updateProcessInstanceExtComplete(camundaProcessInstanceDTO);
        }


    }

}
