package com.dingding.mid.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dingding.mid.common.R;
import com.dingding.mid.common.WorkFlowConstants;
import com.dingding.mid.dto.FlowEngineDTO;
import com.dingding.mid.dto.json.ChildNode;
import com.dingding.mid.dto.json.FormItem;
import com.dingding.mid.dto.json.LogoInfo;
import com.dingding.mid.dto.json.SettingsInfo;
import com.dingding.mid.entity.FormGroups;
import com.dingding.mid.entity.ProcessTemplates;
import com.dingding.mid.entity.TemplateGroup;
import com.dingding.mid.entity.TemplateGroupBo;
import com.dingding.mid.exception.WorkFlowException;
import com.dingding.mid.mapper.TemplateGroupMapper;
import com.dingding.mid.service.FormGroupService;
import com.dingding.mid.service.ProcessTemplateService;
import com.dingding.mid.service.SettingService;
import com.dingding.mid.service.TemplateGroupService;
import com.dingding.mid.utils.IdWorker;
import com.dingding.mid.utils.SpringContextHolder;
import com.dingding.mid.vo.TemplateGroupVo;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import jakarta.annotation.Resource;

import static com.dingding.mid.utils.BpmnModelUtils.*;

/**
 * @author : willian fu
 * @date : 2020/9/21
 */
@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private ProcessTemplateService processTemplateService;

    @Autowired
    private TemplateGroupService templateGroupService;

    @Autowired
    private FormGroupService formGroupService;
    @Resource
    private IdWorker idWorker;
    @Resource
    private RepositoryService repositoryService;

    /**
     * 查询表单组
     *
     * @return 表单组数据
     */
    @Override
    public Object getFormGroups(String token, String name) {
      TemplateGroupMapper templateGroupMapper = SpringContextHolder.getBean(TemplateGroupMapper.class);
      List<TemplateGroupBo> allformAndGroups = templateGroupMapper.getAllFormAndGroups();
        Map<Integer, List<TemplateGroupBo>> coverMap = new LinkedHashMap<>();
        allformAndGroups.forEach(fg -> {
            List<TemplateGroupBo> bos = coverMap.get(fg.getGroupId());
            if (CollectionUtil.isEmpty(bos)) {
                List<TemplateGroupBo> list = new ArrayList<>();
                list.add(fg);
                coverMap.put(fg.getGroupId(), list);
            } else {
                bos.add(fg);
            }
        });
        List<TemplateGroupVo> results = new ArrayList<>();
        coverMap.forEach((key, val) -> {
            List<TemplateGroupVo.Template> templates = new ArrayList<>();
            val.forEach(v -> {
                if (ObjectUtil.isNotNull(v.getTemplateId())) {
                    templates.add(TemplateGroupVo.Template.builder()
                            .formId(v.getTemplateId())
                            .tgId(v.getId())
                            .remark(v.getRemark())
                            .formName(v.getTemplateName())
                            .icon(v.getIcon())
                            .isStop(v.getIsStop())
                            .updated(DateFormatUtils.format(v.getUpdated(),"yyyy年MM月dd日 HH时:mm分:ss秒"))
                            .background(v.getBackground())
                            .templateId(v.getTemplateId())
                            .logo(JSONObject.parseObject(v.getBackground(),new TypeReference<JSONObject>(){}))
                            .build());
                }
            });
            results.add(TemplateGroupVo.builder().id(key).name(val.get(0).getGroupName()).items(templates).build());
        });
        return R.ok(results);
    }

    /**
     * 表单及分组排序
     *
     * @param groups 分组数据
     * @return 排序结果
     */
    @Override
    @Transactional
    public Object formGroupsSort(List<TemplateGroupVo> groups) {
        int i = 0, j = 0;
        try {
            for (TemplateGroupVo group : groups) {
                formGroupService.updateById(FormGroups.builder()
                        .groupId(group.getId())
                        .sortNum(group.getId().equals(0) ? 999999 : i + 2)
                        .build());
                for (TemplateGroupVo.Template item : group.getItems()) {
                    templateGroupService.updateById(
                            TemplateGroup.builder()
                                    .id(item.getTgId())
                                    .groupId(group.getId())
                                    .templateId(item.getFormId())
                                    .sortNum(j + 1).build());
                    j++;
                }
                i++;
                j = 0;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.badRequest("排序异常 " + e.getMessage());
        }
        return R.ok("排序成功");
    }

    /**
     * 查询表单模板数据
     *
     * @param templateId 模板id
     * @return 模板详情数据
     */
    @Override
    public Object getFormTemplateById(String templateId) {
        ProcessTemplates processTemplates = processTemplateService.getById(templateId);
        processTemplates.setLogo(processTemplates.getIcon());
        processTemplates.setFormId(processTemplates.getTemplateId());
        processTemplates.setFormName(processTemplates.getTemplateName());
        return R.ok(processTemplates);
    }

    /**
     * 修改分组
     *
     * @param id   分组ID
     * @param name 分组名
     * @return 修改结果
     */
    @Override
    public Object updateFormGroupName(Integer id, String name) {
        if (id < 2){
            return R.badRequest("基础分组不允许修改");
        }
        formGroupService.updateById(FormGroups.builder()
                .groupId(id).groupName(name.trim()).build());
        return R.ok("修改成功");
    }

    /**
     * 新增表单分组
     *
     * @param name 分组名
     * @return 添加结果
     */
    @Override
    public Object createFormGroup(String name) {
      LambdaQueryWrapper<FormGroups> lambdaQueryWrapper=new LambdaQueryWrapper<>();
      lambdaQueryWrapper.eq(FormGroups::getGroupName, name);
        if (formGroupService.count(lambdaQueryWrapper)>0){
            return R.badRequest("分组名称 [" + name + "] 已存在");
        }
        Date date =new Date();
      FormGroups formGroups = FormGroups.builder().groupName(name)
          .sortNum(1).created(date)
          .updated(date).build();
      formGroupService.save(
                formGroups);
        return R.ok("添加分组 " + name + " 成功");
    }

    /**
     * 删除分组
     *
     * @param id 分组ID
     * @return 删除结果
     */
    @Override
    @Transactional
    public Object deleteFormGroup(Integer id) {
        if (id < 2){
            return R.badRequest("基础分组不允许删除");
        }
        LambdaUpdateWrapper<TemplateGroup> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(TemplateGroup::getGroupId,1);
        lambdaUpdateWrapper.eq(TemplateGroup::getGroupId,id);
        templateGroupService.update(lambdaUpdateWrapper);
        formGroupService.removeById(id);
        return R.ok("删除分组成功");
    }

    /**
     * 编辑表单
     *
     * @param templateId 摸板ID
     * @param type       类型 stop using delete
     * @return 操作结果
     */
    @Override
    @Transactional
    public Object updateForm(String templateId, String type, Integer groupId) {
        boolean isStop = "stop".equals(type);



      ProcessTemplates build = ProcessTemplates.builder().templateId(templateId).isStop(isStop)
          .build();
      if ("using".equals(type) || isStop) {
            processTemplateService.updateById(
                ProcessTemplates.builder().templateId(templateId).isStop(isStop).build());
            LambdaUpdateWrapper<TemplateGroup> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(TemplateGroup::getTemplateId,templateId);
            lambdaUpdateWrapper.set(TemplateGroup::getGroupId,isStop ? 0 :1 );
            templateGroupService
                .update(lambdaUpdateWrapper);
        } else if ("delete".equals(type)) {
            processTemplateService.removeById(templateId);
            LambdaQueryWrapper<TemplateGroup> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(TemplateGroup::getTemplateId,templateId);
            templateGroupService.remove(lambdaQueryWrapper);
        } else if ("move".equals(type)) {
            if (ObjectUtil.isNull(groupId)) {
                return R.badRequest("分组ID必传");
            }

        LambdaUpdateWrapper<TemplateGroup> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(TemplateGroup::getTemplateId,templateId);
        lambdaUpdateWrapper.set(TemplateGroup::getGroupId,groupId);
        templateGroupService.update(lambdaUpdateWrapper);
        } else {
            return R.badRequest("不支持的操作");
        }
        return R.ok("操作成功");
    }

    /**
     * 编辑表单详情
     *
     * @param template 表单模板信息
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateFormDetail(ProcessTemplates template) {
        SettingsInfo settingsInfo = JSONObject.parseObject(template.getSettings(), new TypeReference<SettingsInfo>() {});
        ProcessTemplates processTemplates = processTemplateService.getById(template.getFormId());
        processTemplates.setTemplateName(template.getFormName());
        processTemplates.setGroupId(template.getGroupId());
        processTemplates.setFormItems(template.getFormItems());
        processTemplates.setSettings(template.getSettings());
        processTemplates.setProcess(template.getProcess());
        processTemplates.setIcon(template.getIcon());
        processTemplates.setBackground(template.getBackground());
        processTemplates.setNotify(settingsInfo.getNotify().toJSONString());
        String adminInfo = JSONObject.toJSONString(settingsInfo.getAdmin());
        processTemplates.setWhoCommit(adminInfo);

        processTemplates.setWhoEdit(adminInfo);
        processTemplates.setWhoExport(adminInfo);
        processTemplates.setRemark(template.getRemark());
        processTemplates.setUpdated(new Date());
        processTemplateService.updateById(processTemplates);
        ChildNode childNode = JSONObject.parseObject(template.getProcess(), new TypeReference<ChildNode>(){});
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("processJson",template.getProcess());
        jsonObject.put("formJson",template.getFormItems());
        BpmnModel bpmnModel = assemBpmnModel(jsonObject, childNode, template.getRemark(),
            template.getFormName(), template.getGroupId(), template.getFormId());
        repositoryService.createDeployment()
            .addBpmnModel(template.getFormName() + ".bpmn", bpmnModel)
            .name(template.getFormName())
            .category(template.getGroupId() + "")
            .deploy();
        return R.ok("发布更新后的表单成功");
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void jsonToBpmn(FlowEngineDTO flowEngineDTO) throws InvocationTargetException, IllegalAccessException {
        String processJson = flowEngineDTO.getProcess();
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>(){});
        String settingsJson = flowEngineDTO.getSettings();
        SettingsInfo settingsInfo = JSONObject.parseObject(settingsJson, new TypeReference<SettingsInfo>() {});
        String remark = flowEngineDTO.getRemark();
        String formItems = flowEngineDTO.getFormItems();
        String formName = flowEngineDTO.getFormName();
        String logo = flowEngineDTO.getLogo();
        Integer groupId = flowEngineDTO.getGroupId();
        String templateId = idWorker.nextId()+"";

        ProcessTemplates processTemplates = ProcessTemplates.builder().build();
        processTemplates.setTemplateId(templateId);
        processTemplates.setTemplateName(formName);
        processTemplates.setGroupId(groupId);
        processTemplates.setFormItems(formItems);
        processTemplates.setProcess(processJson);
        processTemplates.setIcon(logo);
        processTemplates.setBackground(logo);
        processTemplates.setNotify(settingsInfo.getNotify().toJSONString());
        String adminInfo = JSONObject.toJSONString(settingsInfo.getAdmin());
        processTemplates.setSettings(settingsJson);
        processTemplates.setWhoCommit(adminInfo);
        processTemplates.setWhoEdit(adminInfo);
        processTemplates.setWhoExport(adminInfo);
        processTemplates.setRemark(flowEngineDTO.getRemark());
        processTemplates.setIsStop(false);
        Date date= new Date();
        processTemplates.setCreated(date);
        processTemplates.setUpdated(date);
        processTemplateService.save(processTemplates);
        TemplateGroup templateGroup=new TemplateGroup();
        templateGroup.setTemplateId(processTemplates.getTemplateId());
        templateGroup.setGroupId(groupId);
        templateGroup.setSortNum(0);
        templateGroup.setCreated(date);
        templateGroupService.save(templateGroup);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("processJson", processJson);
        jsonObject.put("formJson", formItems);
        BpmnModel bpmnModel = assemBpmnModel(jsonObject, childNode, remark, formName, groupId,
            templateId);
        repositoryService.createDeployment()
            .addBpmnModel(formName + ".bpmn", bpmnModel)
            .name(formName)
            .category(groupId + "")
            .deploy();


    }

    private BpmnModel assemBpmnModel(JSONObject jsonObject, ChildNode childNode, String remark,
        String formName, Integer groupId, String templateId)
        {
        BpmnModel bpmnModel =new BpmnModel();
        List<SequenceFlow> sequenceFlows = Lists.newArrayList();
        Map<String,ChildNode> childNodeMap=new HashMap<>();
        bpmnModel.setTargetNamespace(groupId+"");
        ExtensionAttribute extensionAttribute=new ExtensionAttribute();
        extensionAttribute.setName("DingDing");
        extensionAttribute.setNamespace("http://flowable.org/bpmn");
        extensionAttribute.setValue(jsonObject.toJSONString());
        Process process =new Process();
        process.setId(WorkFlowConstants.PROCESS_PREFIX+templateId);
        process.setName(formName);
        process.setDocumentation(remark);
        process.addAttribute(extensionAttribute);
        bpmnModel.addProcess(process);

        StartEvent startEvent = createStartEvent();
        process.addFlowElement(startEvent);
            String lastNode = null;
            try {
                lastNode = create(startEvent.getId(), childNode,process,bpmnModel,sequenceFlows,childNodeMap);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new WorkFlowException("操作失败");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new WorkFlowException("操作失败");
            }
            EndEvent endEvent = createEndEvent();
        process.addFlowElement(endEvent);
        process.addFlowElement(connect(lastNode, endEvent.getId(),sequenceFlows,childNodeMap,process));
        List<FlowableListener> executionListeners =new ArrayList<>();
        FlowableListener flowableListener=new FlowableListener();
        flowableListener.setEvent(ExecutionListener.EVENTNAME_END);
        flowableListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        flowableListener.setImplementation("${processListener}");
        executionListeners.add(flowableListener);
        process.setExecutionListeners(executionListeners);
        new BpmnAutoLayout(bpmnModel).execute();
        return bpmnModel;
    }
}
