package com.dingding.mid.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingding.mid.common.Result;
import com.dingding.mid.dto.*;
import com.dingding.mid.dto.json.ChildNode;
import com.dingding.mid.dto.json.FormOperates;
import com.dingding.mid.dto.json.SettingsInfo;
import com.dingding.mid.dto.json.UserInfo;
import com.dingding.mid.entity.ProcessTemplates;
import com.dingding.mid.entity.Users;
import com.dingding.mid.exception.WorkFlowException;
import com.dingding.mid.service.ProcessTemplateService;
import com.dingding.mid.service.UserService;
import com.dingding.mid.utils.MinioUploadUtil;
import com.dingding.mid.utils.SpringContextHolder;
import com.dingding.mid.vo.*;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
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

@Tag( name="Vue2版本 的数据列表,待办,已办,我发起等接口")
@ApiSort(3)
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
    @Resource
    private UserService userService;

    @Operation(summary = "通过模板id查看流程信息 会附带流程定义id")
    @ApiOperationSupport(order = 1)
    @Parameter(required = true,name = "模板id",description = "该值从form/groupps接口 里面去取")
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


    @ApiOperationSupport(order = 2)
    @Operation(summary = "通过流程定义id启动流程")
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
            ArrayList<UserInfo> userInfos = CollUtil.newArrayList(startUserInfo);
            processVariables.put("root",JSONObject.toJSONString(userInfos));
            Map<String, List<UserInfo>> processUsers = startProcessInstanceDTO.getProcessUsers();
            if(CollUtil.isNotEmpty(processUsers)){
                Set<String> strings = processUsers.keySet();
                for (String string : strings) {
                    List<UserInfo> selectUserInfo = processUsers.get(string);
                    List<String> users=new ArrayList<>();
                    for (UserInfo userInfo : selectUserInfo) {
                        users.add(userInfo.getId());
                    }
                    processVariables.put(string,users);
                }
            }

            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            processVariables.putAll(formValue);
            ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
            ProcessInstance processInstance = processInstanceBuilder
                    .processDefinitionId(startProcessInstanceDTO.getProcessDefinitionId())
                    .variables(processVariables)
                    .businessStatus(BUSINESS_STATUS_1)
                    .start();
            //手动完成第一个任务
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            if(task!=null){
                taskService.complete(task.getId());
            }
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


    @Operation(summary = "查看我发起的流程")
    @ApiOperationSupport(order = 3)
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
        List<String> applyUserIds= new ArrayList<>();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
            String id = JSONObject.parseObject(MapUtil.getStr(processVariables, START_USER_INFO), new TypeReference<UserInfo>() {
            }).getId();
            applyUserIds.add(id);
        }
        Map<Long, Users> collect=new HashMap<>();
        if(CollUtil.isNotEmpty(applyUserIds)){
            LambdaQueryWrapper<Users> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Users::getUserId,applyUserIds);
            List<Users> list = userService.list(lambdaQueryWrapper);
            collect = list.stream().collect(Collectors.toMap(Users::getUserId, Function.identity()));
        }

        List<HistoryProcessInstanceVO> historyProcessInstanceVOS= new ArrayList<>();
        Page<HistoryProcessInstanceVO> page=new Page<>();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
            HistoryProcessInstanceVO historyProcessInstanceVO=new HistoryProcessInstanceVO();
            historyProcessInstanceVO.setProcessInstanceId(historicProcessInstance.getId());
            historyProcessInstanceVO.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            historyProcessInstanceVO.setStartUser(JSONObject.parseObject(MapUtil.getStr(processVariables,START_USER_INFO),new TypeReference<UserInfo>(){}));
            historyProcessInstanceVO.setUsers(collect.get(Long.valueOf(historyProcessInstanceVO.getStartUser().getId())));
            historyProcessInstanceVO.setStartTime(historicProcessInstance.getStartTime());
            historyProcessInstanceVO.setEndTime(historicProcessInstance.getEndTime());
            Boolean flag= historicProcessInstance.getEndTime() != null;
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
        List<ActivityInstance> list = runtimeService.createActivityInstanceQuery().processInstanceId(processInstanceId).activityType("userTask").unfinished().orderByActivityInstanceStartTime().desc().list();
        if(CollUtil.isEmpty(list)){
            return "";
        }
        else{
            String activityId = list.get(0).getActivityId();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            FlowElement flowElement = bpmnModel.getMainProcess().getFlowElement(activityId);
            return flowElement.getName();
        }
    }

    @Operation(summary ="查看我的待办")
    @PostMapping("process/toDoList")
    public Result<Page<TaskVO>> toDoList(@RequestBody TaskDTO taskDTO){
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(taskDTO.getCurrentUserInfo().getId())
                .includeProcessVariables()
                .orderByTaskCreateTime().desc()
                .listPage((taskDTO.getPageNo() - 1) * taskDTO.getPageSize(), taskDTO.getPageSize());
        long count = taskService.createTaskQuery().taskAssignee(taskDTO.getCurrentUserInfo().getId()).count();
        List<TaskVO> taskVOS= new ArrayList<>();
        Page<TaskVO> page =new Page<>();


        List<String> taskIds= new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> processVariables = task.getProcessVariables();
            String id = JSONObject.parseObject(MapUtil.getStr(processVariables, START_USER_INFO), new TypeReference<UserInfo>() {
            }).getId();
            taskIds.add(id);
        }


        Map<Long, Users> collect=new HashMap<>();
        if(CollUtil.isNotEmpty(taskIds)){
            LambdaQueryWrapper<Users> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Users::getUserId,taskIds);
            List<Users> list = userService.list(lambdaQueryWrapper);
            collect = list.stream().collect(Collectors.toMap(Users::getUserId, Function.identity()));
        }

        for (Task task : tasks) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            Map<String, Object> processVariables = task.getProcessVariables();
            TaskVO taskVO=new TaskVO();
            taskVO.setTaskId(task.getId());
            taskVO.setProcessInstanceId(task.getProcessInstanceId());
            taskVO.setProcessDefinitionName(bpmnModel.getMainProcess().getName());
            taskVO.setStartUser(JSONObject.parseObject(MapUtil.getStr(processVariables,START_USER_INFO),new TypeReference<UserInfo>(){}));
            taskVO.setUsers(collect.get(Long.valueOf(taskVO.getStartUser().getId())));
            taskVO.setStartTime(processInstance.getStartTime());
            taskVO.setCurrentActivityName(getCurrentName(processInstance.getId(),false,processInstance.getProcessDefinitionId()));

            taskVO.setBusinessStatus(MapUtil.getStr(processVariables,PROCESS_STATUS));
            taskVO.setTaskCreatedTime(task.getCreateTime());
            DelegationState delegationState = task.getDelegationState();
            if(delegationState!=null){
                taskVO.setDelegationState(delegationState);
            }
            taskVOS.add(taskVO);

        }
        page.setRecords(taskVOS);
        page.setCurrent(taskDTO.getPageNo());
        page.setSize(taskDTO.getPageSize());
        page.setTotal(count);
        return Result.OK(page);
    }

    @Operation(summary ="查看我的已办")
    @PostMapping("process/doneList")
    public Result<Page<TaskVO>> doneList(@RequestBody TaskDTO taskDTO){
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(taskDTO.getCurrentUserInfo().getId())
                .finished()
                .includeProcessVariables()
                .orderByTaskCreateTime().desc()
                .listPage((taskDTO.getPageNo() - 1) * taskDTO.getPageSize(), taskDTO.getPageSize());
        long count = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(taskDTO.getCurrentUserInfo().getId()).finished().count();
        List<TaskVO> taskVOS= new ArrayList<>();
        Page<TaskVO> page =new Page<>();

        List<String> taskIds= new ArrayList<>();
        for (HistoricTaskInstance task : tasks) {
            Map<String, Object> processVariables = task.getProcessVariables();
            String id = JSONObject.parseObject(MapUtil.getStr(processVariables, START_USER_INFO), new TypeReference<UserInfo>() {
            }).getId();
            taskIds.add(id);
        }


        Map<Long, Users> collect=new HashMap<>();
        if(CollUtil.isNotEmpty(taskIds)){
            LambdaQueryWrapper<Users> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Users::getUserId,taskIds);
            List<Users> list = userService.list(lambdaQueryWrapper);
            collect = list.stream().collect(Collectors.toMap(Users::getUserId, Function.identity()));
        }


        for (HistoricTaskInstance task : tasks) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            Boolean flag=historicProcessInstance.getEndTime()==null?false:true;
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            Map<String, Object> processVariables = task.getProcessVariables();
            TaskVO taskVO=new TaskVO();
            taskVO.setTaskId(task.getId());
            taskVO.setTaskName(task.getName());
            taskVO.setProcessInstanceId(task.getProcessInstanceId());
            taskVO.setProcessDefinitionName(bpmnModel.getMainProcess().getName());
            taskVO.setStartUser(JSONObject.parseObject(MapUtil.getStr(processVariables,START_USER_INFO),new TypeReference<UserInfo>(){}));
            taskVO.setUsers(collect.get(Long.valueOf(taskVO.getStartUser().getId())));
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

    @Operation(summary ="同意按钮")
    @PostMapping("/agree")
    public Result agree(@RequestBody HandleDataDTO handleDataDTO,MultipartFile file){
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

    @Operation(summary ="委派按钮")
    @PostMapping("/delegateTask")
    public Result delegateTask(@RequestBody HandleDataDTO handleDataDTO){
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

        UserInfo delegateUserInfo = handleDataDTO.getDelegateUserInfo();
        taskService.delegateTask(task.getId(),delegateUserInfo.getId());
        return Result.OK();
    }

    @Operation(summary ="委派人完成的按钮")
    @PostMapping("/resolveTask")
    public Result resolveTask(@RequestBody HandleDataDTO handleDataDTO){
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

        taskService.resolveTask(taskId);
        return Result.OK();
    }


    @Operation(summary ="拒绝按钮")
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

    @Operation(summary ="撤销按钮")
    @PostMapping("/revoke")
    public Result revoke(@RequestBody HandleDataDTO handleDataDTO){
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
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


    @Operation(summary ="转办按钮")
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
        map.put(PROCESS_STATUS,BUSINESS_STATUS_1);
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
    @Operation(summary ="查询可退回的节点(这个是给 下面 rollback接口作为入参用的 )")
    @PostMapping("/rollbackNodes")
    public Result rollbackNodes(@RequestBody HandleDataDTO handleDataDTO){
        List<ActivityInstance> list = runtimeService.createActivityInstanceQuery().unfinished().processInstanceId(handleDataDTO.getProcessInstanceId()).list();
        Map<String,String> nodes=new HashMap<>();
        for (ActivityInstance activityInstance : list) {
            nodes.put(activityInstance.getActivityId(),activityInstance.getActivityName());
        }
        return Result.OK(nodes);
    }

    @Operation(summary ="退回按钮")
    @PostMapping("/rollback")
    public Result rollback(@RequestBody HandleDataDTO handleDataDTO){
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        String processInstanceId = handleDataDTO.getProcessInstanceId();
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        Task task = null;
        List<String> taskIds = new ArrayList<>();

        for (Task task1 : list) {
            if(task1.getId().equals(taskId)){
                task=task1;
            }
            taskIds.add(task1.getTaskDefinitionKey());
        }



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


        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveActivityIdsToSingleActivityId(taskIds,handleDataDTO.getRollbackId())
                .changeState();
        return Result.OK();
    }


    @Operation(summary ="加签按钮")
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
        map.put(PROCESS_STATUS,BUSINESS_STATUS_1);
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
        ExecutionEntity execution = (ExecutionEntity) runtimeService.addMultiInstanceExecution(task.getTaskDefinitionKey(), task.getProcessInstanceId(), variableMap);
        return Result.OK();
    }


    @Operation(summary ="查到签上的人")
    @PostMapping("/queryMultiUsersInfo")
    public Result<List<MultiVO>> queryMultiUsersInfo(@RequestBody Map<String,Object> map){
        String taskId = MapUtil.getStr(map, "taskId");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        List<Task> list = taskService.createTaskQuery()
            .processInstanceId(task.getProcessInstanceId())
            .taskDefinitionKey(task.getTaskDefinitionKey()).list();
        Iterator<Task> iterator = list.iterator();
        List<MultiVO> multiVOList= new ArrayList<>();
        while (iterator.hasNext()){
            Task next = iterator.next();
            if(!taskId.equals(next.getId())){
                MultiVO multiVO=new MultiVO();
                multiVO.setTaskId(next.getId());
                multiVO.setProcessInstanceId(next.getProcessInstanceId());
                multiVO.setExecutionId(next.getExecutionId());
                multiVO.setUserId(next.getAssignee());
                multiVOList.add(multiVO);
            }

        }
        return Result.OK(multiVOList);
    }

    @Operation(summary ="减签按钮")
    @PostMapping("/deleteMulti")
    public Result deleteMulti(@RequestBody List<String> executionIds){
        for (String executionId : executionIds) {
            runtimeService.deleteMultiInstanceExecution(executionId,true);
        }
        return Result.OK();
    }


    @Operation(summary ="评论按钮")
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
        map.put(PROCESS_STATUS,BUSINESS_STATUS_1);
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

    @Operation(summary ="通过流程实例id查看详情")
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
//                        iterator.remove();
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
        else{
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
        else {
            handleDataVO.setSignFlag(false);
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



    @Operation(summary ="上传文件")
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
    @Operation(summary ="下载文件")
    @PostMapping("downLoadFile")
    public void downLoadFile(@RequestParam("name") String name){
        MinioUploadUtil minioUploadUtil = SpringContextHolder.getBean(MinioUploadUtil.class);
        minioUploadUtil.downFile(name,"java",name);
    }

}
