package com.dingding.mid.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingding.mid.common.Result;
import com.dingding.mid.dto.*;
import com.dingding.mid.dto.json.ChildNode;
import com.dingding.mid.dto.json.FormOperates;
import com.dingding.mid.dto.json.SettingsInfo;
import com.dingding.mid.dto.json.UserInfo;
import com.dingding.mid.entity.ProcessTemplates;
import com.dingding.mid.exception.WorkFlowException;
import com.dingding.mid.service.ProcessTemplateService;
import com.dingding.mid.utils.MinioUploadUtil;
import com.dingding.mid.utils.SpringContextHolder;
import com.dingding.mid.vo.*;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.dingding.mid.common.CommonConstants.*;
import static com.dingding.mid.common.WorkFlowConstants.PROCESS_PREFIX;
import static com.dingding.mid.utils.BpmnModelUtils.getChildNode;

/**
 * @author : willian fu
 * @version : 1.0
 */
@RestController
@RequestMapping("/workspace")
@Api("和Flowable有关的接口")
public class WorkspaceProcessController {

    @Resource
    private ProcessTemplateService processTemplateService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private HistoryService historyService;
    @Resource
    private TaskService taskService;

    @ApiOperation("通过模板id查看流程信息 会附带流程定义id")
    @GetMapping("process/detail")
    public Result<ProcessTemplates> detail(@RequestParam("templateId") String templateId){
        ProcessTemplates processTemplates = processTemplateService.getById(templateId);
        processTemplates.setLogo(processTemplates.getIcon());
        processTemplates.setFormId(processTemplates.getTemplateId());
        processTemplates.setFormName(processTemplates.getTemplateName());
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(PROCESS_PREFIX + templateId).latestVersion().singleResult();
        if(processDefinition==null){
            throw  new WorkFlowException("该流程暂未接入Flowable,请重试");
        }
        processTemplates.setProcessDefinitionId(processDefinition.getId());
        return Result.OK(processTemplates);
    }

    @ApiOperation("通过流程定义id启动流程")
    @PostMapping("process/start")
    public Result<Object> start(@RequestBody StartProcessInstanceDTO startProcessInstanceDTO){
        try{
            JSONObject formData = startProcessInstanceDTO.getFormData();
            UserInfo startUserInfo = startProcessInstanceDTO.getStartUserInfo();
            Authentication.setAuthenticatedUserId(startUserInfo.getId());
            Map<String,Object> processVariables= new HashMap<>();
            processVariables.put(FORM_VAR,formData);
            processVariables.put(PROCESS_STATUS,BUSINESS_STATUS_1);
            processVariables.put(START_USER_INFO,JSONObject.toJSONString(startUserInfo));
            processVariables.put("root",startUserInfo.getId());
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            processVariables.putAll(formValue);
            ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
            ProcessInstance processInstance = processInstanceBuilder
                    .processDefinitionId(startProcessInstanceDTO.getProcessDefinitionId())
                    .variables(processVariables)
                    .businessStatus(BUSINESS_STATUS_1)
                    .start();
            return Result.OK(processInstance.getId());
        }
        catch (Exception e){
            Throwable cause = e.getCause();
            if(cause instanceof WorkFlowException){
                WorkFlowException workFlowException=(WorkFlowException)cause;
                return Result.error(workFlowException.getMessage());
            }
            e.printStackTrace();
            return Result.error("启动流程失败");
        }
    }


    @ApiOperation("查看我发起的流程")
    @PostMapping("process/applyList")
    public Result< Page<HistoryProcessInstanceVO>> applyList(@RequestBody ApplyDTO applyDTO){
        List<HistoricProcessInstance> historicProcessInstances =
                historyService.createHistoricProcessInstanceQuery()
                .includeProcessVariables()
                .startedBy(applyDTO.getCurrentUserInfo().getId())
                .orderByProcessInstanceStartTime().desc()
                .listPage((applyDTO.getPageNo() - 1) * applyDTO.getPageSize(), applyDTO.getPageSize());
        long count = historyService.createHistoricProcessInstanceQuery()
                .startedBy(applyDTO.getCurrentUserInfo().getId()).count();
        List<HistoryProcessInstanceVO> historyProcessInstanceVOS= new ArrayList<>();
        Page<HistoryProcessInstanceVO> page=new Page<>();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
            HistoryProcessInstanceVO historyProcessInstanceVO=new HistoryProcessInstanceVO();
            historyProcessInstanceVO.setProcessInstanceId(historicProcessInstance.getId());
            historyProcessInstanceVO.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            historyProcessInstanceVO.setStartUser(JSONObject.parseObject(MapUtil.getStr(processVariables,START_USER_INFO),new TypeReference<UserInfo>(){}));
            historyProcessInstanceVO.setStartTime(historicProcessInstance.getStartTime());
            historyProcessInstanceVO.setEndTime(historicProcessInstance.getEndTime());
            Boolean flag=historicProcessInstance.getEndTime()==null?false:true;
            historyProcessInstanceVO.setCurrentActivityName(getCurrentName(historicProcessInstance.getId(),flag,historicProcessInstance.getProcessDefinitionId()));
            historyProcessInstanceVO.setBusinessStatus(MapUtil.getStr(processVariables,PROCESS_STATUS));


            long totalTimes = historicProcessInstance.getEndTime()==null?
                    (Calendar.getInstance().getTimeInMillis()-historicProcessInstance.getStartTime().getTime()):
                    (historicProcessInstance.getEndTime().getTime()-historicProcessInstance.getStartTime().getTime());
            long dayCount = totalTimes /(1000*60*60*24);//计算天
            long restTimes = totalTimes %(1000*60*60*24);//剩下的时间用于计于小时
            long hourCount = restTimes/(1000*60*60);//小时
            restTimes = restTimes % (1000*60*60);
            long minuteCount = restTimes / (1000*60);

            String spendTimes = dayCount+"天"+hourCount+"小时"+minuteCount+"分";
            historyProcessInstanceVO.setDuration(spendTimes);
            historyProcessInstanceVOS.add(historyProcessInstanceVO);
        }
        page.setRecords(historyProcessInstanceVOS);
        page.setCurrent(applyDTO.getPageNo());
        page.setSize(applyDTO.getPageSize());
        page.setTotal(count);
        return Result.OK(page);
    }


    private   String getCurrentName(String processInstanceId,Boolean flag,String processDefinitionId){
        if(flag){
            return "流程已结束";
        }
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        String activityId = activeActivityIds.get(0);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        FlowElement flowElement = bpmnModel.getMainProcess().getFlowElement(activityId);
        return flowElement.getName();
    }

    @ApiOperation("查看我的待办")
    @PostMapping("process/toDoList")
    public Result<Page<TaskVO>> toDoList(@RequestBody TaskDTO taskDTO){
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(taskDTO.getCurrentUserInfo().getId())
                .includeProcessVariables()
                .orderByTaskCreateTime().desc()
                .listPage((taskDTO.getPageNo() - 1) * taskDTO.getPageSize(), taskDTO.getPageSize());
        long count = taskService.createTaskQuery().taskAssignee(taskDTO.getCurrentUserInfo().getId()).count();
        List<TaskVO> taskVOS= new ArrayList<>();
        Page<TaskVO> page =new Page<>();
        for (Task task : tasks) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            Map<String, Object> processVariables = task.getProcessVariables();
            TaskVO taskVO=new TaskVO();
            taskVO.setTaskId(task.getId());
            taskVO.setProcessInstanceId(task.getProcessInstanceId());
            taskVO.setProcessDefinitionName(bpmnModel.getMainProcess().getName());
            taskVO.setStartUser(JSONObject.parseObject(MapUtil.getStr(processVariables,START_USER_INFO),new TypeReference<UserInfo>(){}));
            taskVO.setStartTime(historicProcessInstance.getStartTime());
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(task.getProcessInstanceId());
            String activityId = activeActivityIds.get(0);
            FlowElement flowElement = bpmnModel.getMainProcess().getFlowElement(activityId);
            taskVO.setCurrentActivityName(flowElement.getName());
            taskVO.setBusinessStatus(MapUtil.getStr(processVariables,PROCESS_STATUS));
            taskVO.setTaskCreatedTime(task.getCreateTime());
            taskVOS.add(taskVO);
        }
        page.setRecords(taskVOS);
        page.setCurrent(taskDTO.getPageNo());
        page.setSize(taskDTO.getPageSize());
        page.setTotal(count);
        return Result.OK(page);
    }

    @ApiOperation("查看我的已办")
    @PostMapping("process/doneList")
    public Result<Page<TaskVO>> doneList(@RequestBody TaskDTO taskDTO){
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(taskDTO.getCurrentUserInfo().getId())
                .includeProcessVariables()
                .orderByTaskCreateTime().desc()
                .listPage((taskDTO.getPageNo() - 1) * taskDTO.getPageSize(), taskDTO.getPageSize());
        long count = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(taskDTO.getCurrentUserInfo().getId()).count();
        List<TaskVO> taskVOS= new ArrayList<>();
        Page<TaskVO> page =new Page<>();
        for (HistoricTaskInstance task : tasks) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            Boolean flag=historicProcessInstance.getEndTime()==null?false:true;
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            Map<String, Object> processVariables = task.getProcessVariables();
            TaskVO taskVO=new TaskVO();
            taskVO.setTaskId(task.getId());
            taskVO.setProcessInstanceId(task.getProcessInstanceId());
            taskVO.setProcessDefinitionName(bpmnModel.getMainProcess().getName());
            taskVO.setStartUser(JSONObject.parseObject(MapUtil.getStr(processVariables,START_USER_INFO),new TypeReference<UserInfo>(){}));
            taskVO.setStartTime(historicProcessInstance.getStartTime());
            taskVO.setCurrentActivityName(getCurrentName(task.getProcessInstanceId(),flag,task.getProcessDefinitionId()));
            taskVO.setBusinessStatus(MapUtil.getStr(processVariables,PROCESS_STATUS));
            taskVO.setEndTime(task.getEndTime());

            long totalTimes = task.getEndTime()==null?
                    (Calendar.getInstance().getTimeInMillis()-task.getStartTime().getTime()):
                    (task.getEndTime().getTime()-task.getStartTime().getTime());
            long dayCount = totalTimes /(1000*60*60*24);//计算天
            long restTimes = totalTimes %(1000*60*60*24);//剩下的时间用于计于小时
            long hourCount = restTimes/(1000*60*60);//小时
            restTimes = restTimes % (1000*60*60);
            long minuteCount = restTimes / (1000*60);
            String spendTimes = dayCount+"天"+hourCount+"小时"+minuteCount+"分";
            taskVO.setDuration(spendTimes);
            taskVOS.add(taskVO);
        }

        page.setRecords(taskVOS);
        page.setCurrent(taskDTO.getPageNo());
        page.setSize(taskDTO.getPageSize());
        page.setTotal(count);
        return Result.OK(page);
    }

    @ApiOperation("同意按钮")
    @PostMapping("/agree")
    public Result agree(@RequestBody HandleDataDTO handleDataDTO){
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }

        runtimeService.setVariables(task.getProcessInstanceId(),map);
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        if(StringUtils.isNotBlank(comments)){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"opinion",comments);
        }
        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment("option",taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

        if(StringUtils.isNotBlank(handleDataDTO.getSignInfo())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"sign",handleDataDTO.getSignInfo());
        }


        taskService.complete(task.getId());
        return Result.OK();
    }

    @ApiOperation("拒绝按钮")
    @PostMapping("/refuse")
    public Result refuse(@RequestBody HandleDataDTO handleDataDTO){
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }
        map.put(PROCESS_STATUS,BUSINESS_STATUS_3);
        runtimeService.setVariables(task.getProcessInstanceId(),map);
        if(StringUtils.isNotBlank(comments)){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"opinion",comments);
        }
        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment("option",taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

        if(StringUtils.isNotBlank(handleDataDTO.getSignInfo())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"sign",handleDataDTO.getSignInfo());
        }
        runtimeService.deleteProcessInstance(task.getProcessInstanceId(),"拒绝");
        return Result.OK();
    }

    @ApiOperation("撤销按钮")
    @PostMapping("/revoke")
    public Result revoke(@RequestBody HandleDataDTO handleDataDTO){
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }
        map.put(PROCESS_STATUS,BUSINESS_STATUS_2);
        runtimeService.setVariables(task.getProcessInstanceId(),map);
        if(StringUtils.isNotBlank(comments)){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"opinion",comments);
        }
        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment("option",taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

        if(StringUtils.isNotBlank(handleDataDTO.getSignInfo())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"sign",handleDataDTO.getSignInfo());
        }
        runtimeService.deleteProcessInstance(task.getProcessInstanceId(),"撤销");
        return Result.OK();
    }


    @ApiOperation("转办按钮")
    @PostMapping("/assignee")
    public Result assignee(@RequestBody HandleDataDTO handleDataDTO){
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }
        map.put(PROCESS_STATUS,BUSINESS_STATUS_3);
        runtimeService.setVariables(task.getProcessInstanceId(),map);
        if(StringUtils.isNotBlank(comments)){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"opinion",comments);
        }
        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment("option",taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

        if(StringUtils.isNotBlank(handleDataDTO.getSignInfo())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"sign",handleDataDTO.getSignInfo());
        }
        taskService.setAssignee(taskId,handleDataDTO.getTransferUserInfo().getId());
        return Result.OK();
    }


    @ApiOperation("退回按钮")
    @PostMapping("/rollback")
    public Result rollback(@RequestBody HandleDataDTO handleDataDTO){
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }
        map.put(PROCESS_STATUS,BUSINESS_STATUS_3);
        runtimeService.setVariables(task.getProcessInstanceId(),map);
        if(StringUtils.isNotBlank(comments)){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"opinion",comments);
        }
        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment("option",taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

        if(StringUtils.isNotBlank(handleDataDTO.getSignInfo())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"sign",handleDataDTO.getSignInfo());
        }
        //简易驳回 不写复杂驳回. 自行去实现去
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveActivityIdTo(task.getTaskDefinitionKey(),handleDataDTO.getRollbackId());
        return Result.OK();
    }


    @ApiOperation("加签按钮")
    @PostMapping("/addMulti")
    public Result addMulti(@RequestBody HandleDataDTO handleDataDTO){
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }
        map.put(PROCESS_STATUS,BUSINESS_STATUS_3);
        runtimeService.setVariables(task.getProcessInstanceId(),map);
        if(StringUtils.isNotBlank(comments)){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"opinion",comments);
        }
        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment("option",taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

        if(StringUtils.isNotBlank(handleDataDTO.getSignInfo())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"sign",handleDataDTO.getSignInfo());
        }

        Map<String,Object> variableMap= new HashMap<>();
        variableMap.put("assigneeName",handleDataDTO.getMultiAddUserInfo().getId());
//                    ExecutionEntity executionEntity = managementService.executeCommand(new GetRootExecutionEntityCmd(task.getExecutionId(), userTask.getId()));
        ExecutionEntity execution = (ExecutionEntity) runtimeService.addMultiInstanceExecution(task.getTaskDefinitionKey(), task.getProcessInstanceId(), variableMap);
        return Result.OK();
    }

    @ApiOperation("评论按钮")
    @PostMapping("/comments")
    public Result comments(@RequestBody HandleDataDTO handleDataDTO){
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }
        map.put(PROCESS_STATUS,BUSINESS_STATUS_3);
        runtimeService.setVariables(task.getProcessInstanceId(),map);
        if(StringUtils.isNotBlank(comments)){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"comments",comments);
        }
        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment("option",taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

        if(StringUtils.isNotBlank(handleDataDTO.getSignInfo())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"sign",handleDataDTO.getSignInfo());
        }
        return Result.OK();
    }

    @ApiOperation("通过流程实例id查看详情")
    @PostMapping("process/instanceInfo")
    public Result<HandleDataVO> instanceInfo(@RequestBody HandleDataDTO HandleDataDTO){
        String processInstanceId = HandleDataDTO.getProcessInstanceId();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId)
                .includeProcessVariables().singleResult();
        String processDefinitionKey = historicProcessInstance.getProcessDefinitionKey();
        ProcessTemplates processTemplates = processTemplateService.getById(processDefinitionKey.replace(PROCESS_PREFIX,""));
        processTemplates.setLogo(processTemplates.getIcon());
        processTemplates.setFormId(processTemplates.getTemplateId());
        processTemplates.setFormName(processTemplates.getTemplateName());
        processTemplates.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());

        HandleDataVO handleDataVO =new HandleDataVO();
        Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();

        handleDataVO.setProcessInstanceId(historicProcessInstance.getId());
        JSONObject jsonObject = (JSONObject) processVariables.get(FORM_VAR);
        handleDataVO.setFormData(jsonObject);
        String process = processTemplates.getProcess();
        ChildNode childNode = JSONObject.parseObject(process, new TypeReference<ChildNode>(){});
        SettingsInfo settingsInfo = JSONObject.parseObject(processTemplates.getSettings(), new TypeReference<SettingsInfo>() {});
        Boolean sign = settingsInfo.getSign();
        ChildNode currentNode=null;
        if(StringUtils.isNotBlank(HandleDataDTO.getTaskId())){
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(HandleDataDTO.getTaskId()).singleResult();
            currentNode = getChildNode(childNode, historicTaskInstance.getTaskDefinitionKey());
            List<FormOperates> formPerms = currentNode.getProps().getFormPerms();
            if(CollUtil.isNotEmpty(formPerms)){
                Iterator<FormOperates> iterator = formPerms.iterator();
                while (iterator.hasNext()){
                    FormOperates next = iterator.next();
                    if("H".equals(next.getPerm())){
                        iterator.remove();
                        if(jsonObject!=null){
                            jsonObject.remove(next.getId());
                        }
                    }
                }
            }
            handleDataVO.setCurrentNode(currentNode);
            handleDataVO.setTaskId(HandleDataDTO.getTaskId());
        }

        if(sign){
            handleDataVO.setSignFlag(true);
        }
        if(StringUtils.isNotBlank(HandleDataDTO.getTaskId())){
            if(currentNode!=null){
                if(currentNode.getProps().getSign()){
                    handleDataVO.setSignFlag(true);
                }
                else{
                    handleDataVO.setSignFlag(false);
                }
            }
        }




        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(historicProcessInstance.getId()).list();
        Map<String,List<HistoricActivityInstance>> historicActivityInstanceMap =new HashMap<>();
        for (HistoricActivityInstance historicActivityInstance : list) {
            List<HistoricActivityInstance> historicActivityInstances = historicActivityInstanceMap.get(historicActivityInstance.getActivityId());
            if(historicActivityInstances==null){
             historicActivityInstances =new ArrayList<>();
             historicActivityInstances.add(historicActivityInstance);
             historicActivityInstanceMap.put(historicActivityInstance.getActivityId(),historicActivityInstances);
            }
            else{
                historicActivityInstances.add(historicActivityInstance);
                historicActivityInstanceMap.put(historicActivityInstance.getActivityId(),historicActivityInstances);
            }
        }

        Process mainProcess = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId()).getMainProcess();
        Collection<FlowElement> flowElements = mainProcess.getFlowElements();

        List<String> runningList= new ArrayList<>();
        handleDataVO.setRunningList(runningList);
        List<String> endList=new ArrayList<>();
        handleDataVO.setEndList(endList);
        List<String> noTakeList=new ArrayList<>();
        handleDataVO.setNoTakeList(noTakeList);
        Map<String,List<TaskDetailVO>> deatailMap =new HashMap<>();
        List<Comment> processInstanceComments = taskService.getProcessInstanceComments(historicProcessInstance.getId());
        List<Attachment> processInstanceAttachments = taskService.getProcessInstanceAttachments(historicProcessInstance.getId());
        for (FlowElement flowElement : flowElements) {
            List<TaskDetailVO> detailVOList =new ArrayList<>();
            List<HistoricActivityInstance> historicActivityInstanceList = historicActivityInstanceMap.get(flowElement.getId());
            if(CollUtil.isNotEmpty(historicActivityInstanceList)){
                for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
                    if(historicActivityInstance.getEndTime()!=null){
                        if("startEvent".equalsIgnoreCase(historicActivityInstance.getActivityType()) ||"endEvent".equalsIgnoreCase(historicActivityInstance.getActivityType())){
                            TaskDetailVO taskDetailVO = new TaskDetailVO();
                            taskDetailVO.setActivityId(historicActivityInstance.getActivityId());
                            taskDetailVO.setName(historicActivityInstance.getActivityName());
                            taskDetailVO.setCreateTime(historicActivityInstance.getStartTime());
                            taskDetailVO.setEndTime(historicActivityInstance.getEndTime());
                            detailVOList.add(taskDetailVO);
                            deatailMap.put(historicActivityInstance.getActivityId(),detailVOList);
                            endList.add(historicActivityInstance.getActivityId());
                        }
                        else if ("userTask".equalsIgnoreCase(historicActivityInstance.getActivityType())){
                            List<TaskDetailVO> voList = deatailMap.get(historicActivityInstance.getActivityId());
                            List<HistoricActivityInstance> activityInstanceList = list.stream().filter(h -> h.getActivityId().equals(historicActivityInstance.getActivityId()) &&h.getEndTime()!=null).collect(Collectors.toList());
                            if(voList!=null){
                                collectUserTaskInfo(processInstanceComments, processInstanceAttachments, historicActivityInstance, voList, activityInstanceList);
                            }
                            else{
                                voList=new ArrayList<>();
                                collectUserTaskInfo(processInstanceComments, processInstanceAttachments, historicActivityInstance, voList, activityInstanceList);
                            }
                            deatailMap.put(historicActivityInstance.getActivityId(),voList);
                            endList.add(historicActivityInstance.getActivityId());
                        }
                        else if("serviceTask".equalsIgnoreCase(historicActivityInstance.getActivityType())){

                        }
                    }
                    else{
                        if ("userTask".equalsIgnoreCase(historicActivityInstance.getActivityType())){
                            List<TaskDetailVO> voList = deatailMap.get(historicActivityInstance.getActivityId());
                            List<HistoricActivityInstance> activityInstanceList = list.stream().filter(h -> h.getActivityId().equals(historicActivityInstance.getActivityId()) &&h.getEndTime()==null).collect(Collectors.toList());
                            if(voList!=null){
                                collectUserTaskInfo(processInstanceComments, processInstanceAttachments, historicActivityInstance, voList, activityInstanceList);
                            }
                            else{
                                voList=new ArrayList<>();
                                collectUserTaskInfo(processInstanceComments, processInstanceAttachments, historicActivityInstance, voList, activityInstanceList);
                            }
                            deatailMap.put(historicActivityInstance.getActivityId(),voList);
                            if(endList.contains(historicActivityInstance.getActivityId())){
                                endList.remove(historicActivityInstance.getActivityId());
                                runningList.add(historicActivityInstance.getActivityId());
                            }
                            else{
                                runningList.add(historicActivityInstance.getActivityId());
                            }
                        }
                        else if("serviceTask".equalsIgnoreCase(historicActivityInstance.getActivityType())){

                        }
                    }
                }
            }
            else{
                noTakeList.add(flowElement.getId());
            }
        }
        handleDataVO.setProcessTemplates(processTemplates);
        handleDataVO.setDetailVOList(deatailMap);
        return Result.OK(handleDataVO);
    }

    private void collectUserTaskInfo(List<Comment> processInstanceComments,
                                     List<Attachment> processInstanceAttachments,
                                     HistoricActivityInstance historicActivityInstance,
                                     List<TaskDetailVO> voList,
                                     List<HistoricActivityInstance> activityInstanceList) {
        for (HistoricActivityInstance activityInstance : activityInstanceList) {
            TaskDetailVO taskDetailVO =new TaskDetailVO();
            taskDetailVO.setTaskId(activityInstance.getTaskId());
            taskDetailVO.setActivityId(activityInstance.getActivityId());
            taskDetailVO.setName(activityInstance.getActivityName());
            taskDetailVO.setCreateTime(activityInstance.getStartTime());
            taskDetailVO.setEndTime(activityInstance.getEndTime());
            Comment signComment = processInstanceComments.stream().filter(h -> h.getTaskId().equals(historicActivityInstance.getTaskId()) && h.getType().equals("sign")).findFirst().orElse(null);
            if(signComment!=null){
                taskDetailVO.setSignImage(signComment.getFullMessage());
            }
            List<Attachment> attachments = processInstanceAttachments.stream().filter(h -> h.getTaskId().equals(historicActivityInstance.getTaskId())).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(attachments)){
                List<AttachmentVO> attachmentVOList = new ArrayList<>();
                for (Attachment attachment : attachments) {
                    AttachmentVO attachmentVO = new AttachmentVO();
                    attachmentVO.setId(attachment.getId());
                    attachmentVO.setName(attachment.getName());
                    attachmentVO.setUrl(attachment.getUrl());
                    attachmentVOList.add(attachmentVO);
                }
                taskDetailVO.setAttachmentVOList(attachmentVOList);
            }

            List<Comment> options = processInstanceComments.stream().filter(h -> h.getTaskId().equals(historicActivityInstance.getTaskId()) && h.getType().equals("opinion")).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(options)){
                List<OptionVO> optionVOList =new ArrayList<>();
                for (Comment option : options) {
                    OptionVO optionVO = new OptionVO();
                    optionVO.setComments(option.getFullMessage());
                    optionVO.setUserId(option.getUserId());
//                                        optionVO.setUserName();
                    optionVO.setCreateTime(option.getTime());
                    optionVOList.add(optionVO);
                }
                taskDetailVO.setOptionVOList(optionVOList);
            }

            List<Comment> comments = processInstanceComments.stream().filter(h -> h.getTaskId().equals(historicActivityInstance.getTaskId()) && h.getType().equals("comments")).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(comments)){
                List<CommentVO> commentsVOList =new ArrayList<>();
                for (Comment comment : comments) {
                    CommentVO commentVO = new CommentVO();
                    commentVO.setComments(comment.getFullMessage());
                    commentVO.setUserId(comment.getUserId());
//                                        commentVO.setUserName();
                    commentVO.setCreateTime(comment.getTime());
                    commentsVOList.add(commentVO);
                }
                taskDetailVO.setCommentVOList(commentsVOList);
            }

            voList.add(taskDetailVO);



        }
    }



    @ApiOperation("上传文件")
    @PostMapping("upLoadFile")
    public Result<AttachmentVO> upLoadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        MinioUploadUtil minioUploadUtil = SpringContextHolder.getBean(MinioUploadUtil.class);
        MinioClient minioClient = SpringContextHolder.getBean(MinioClient.class);
        AttachmentVO attachmentVO= new AttachmentVO();
        attachmentVO.setName(file.getOriginalFilename());
        minioUploadUtil.uploadFile(file, "java", file.getOriginalFilename());
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket("java")
                            .object(file.getOriginalFilename())
                            .expiry(7, TimeUnit.DAYS) // 设置5秒的超时时间。
                            .method(Method.GET)
                            .build());
            attachmentVO.setUrl(url);
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return Result.OK(attachmentVO);
    }
    @ApiOperation("下载文件")
    @PostMapping("downLoadFile")
    public void downLoadFile(@RequestParam("name") String name){
        MinioUploadUtil minioUploadUtil = SpringContextHolder.getBean(MinioUploadUtil.class);
        minioUploadUtil.downFile(name,"java",name);
    }

}
