package cn.iocoder.yudao.module.bpm.service.definition;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.util.object.PageUtils;
import cn.iocoder.yudao.framework.common.util.validation.ValidationUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model.*;
import cn.iocoder.yudao.module.bpm.convert.definition.BpmModelConvert;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmFormDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmModelPlusDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmUserGroupDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.ProcessDefinitionDO;
import cn.iocoder.yudao.module.bpm.dal.mysql.definition.BpmModelPlusMapper;
import cn.iocoder.yudao.module.bpm.enums.definition.BpmModelFormTypeEnum;
import cn.iocoder.yudao.module.bpm.service.definition.dto.BpmModelMetaInfoRespDTO;
import cn.iocoder.yudao.module.bpm.service.definition.dto.BpmProcessDefinitionCreateReqDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.persistence.entity.SuspensionState;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.*;

/**
 * Flowable流程模型实现
 * 主要进行 Flowable {@link Model} 的维护
 *
 * @author yunlongn
 * @author 芋道源码
 * @author jason
 */
@Service
@Validated
@Slf4j
public class BpmModelServiceImpl implements BpmModelService {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private BpmFormService bpmFormService;
    @Resource
    private BpmTaskAssignRuleService taskAssignRuleService;
    @Resource
    private BpmModelPlusMapper bpmModelPlusMapper;

    @Override
    public PageResult<BpmModelPageItemRespVO> getModelPage(BpmModelPageReqVO pageVO) {
        LambdaQueryWrapper<BpmModelPlusDO> lambdaQueryWrapper= new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(pageVO.getKey())) {
            lambdaQueryWrapper.eq(BpmModelPlusDO::getKey,pageVO.getKey());
        }
        if (StrUtil.isNotBlank(pageVO.getName())) {
            lambdaQueryWrapper.like(BpmModelPlusDO::getName,"%"+pageVO.getName()+"%");
        }
        if (StrUtil.isNotBlank(pageVO.getCategory())) {
            lambdaQueryWrapper.eq(BpmModelPlusDO::getCategory,pageVO.getCategory());
        }

        lambdaQueryWrapper.orderByDesc(BpmModelPlusDO::getCreateTime);

        PageResult<BpmModelPlusDO> bpmModelPlusDOPageResult = bpmModelPlusMapper.selectPage(pageVO, lambdaQueryWrapper);

        List<BpmModelPlusDO> models = bpmModelPlusDOPageResult.getList();
        // 获得 Form Map
        Set<Long> formIds = CollectionUtils.convertSet(models, model -> {
            BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(model.getFormInfo(), BpmModelMetaInfoRespDTO.class);
            return metaInfo != null ? metaInfo.getFormId() : null;
        });
        Map<Long, BpmFormDO> formMap = bpmFormService.getFormMap(formIds);

        // 获得 Deployment Map
        Set<String> deploymentIds = new HashSet<>();
        models.forEach(model -> CollectionUtils.addIfNotNull(deploymentIds, model.getDeploymentId()));
        Map<String, Deployment> deploymentMap = processDefinitionService.getDeploymentMap(deploymentIds);
        // 获得 ProcessDefinition Map
        List<ProcessDefinitionDO> processDefinitions = processDefinitionService.getProcessDefinitionListByDeploymentIds(deploymentIds);
        Map<String, ProcessDefinitionDO> processDefinitionMap = convertMap(processDefinitions, ProcessDefinitionDO::getDeploymentId);
        Long modelCount = bpmModelPlusDOPageResult.getTotal();


        List<BpmModelPageItemRespVO> bpmModelPageItemRespVOS = CollectionUtils.convertList(models, model -> {
            BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(model.getFormInfo(), BpmModelMetaInfoRespDTO.class);
            BpmFormDO form = metaInfo != null ? formMap.get(metaInfo.getFormId()) : null;
            Deployment deployment = model.getDeploymentId() != null ? deploymentMap.get(model.getDeploymentId()) : null;
            ProcessDefinitionDO processDefinition = model.getDeploymentId() != null ? processDefinitionMap.get(model.getDeploymentId()) : null;
            BpmModelPageItemRespVO modelRespVO = new BpmModelPageItemRespVO();
            modelRespVO.setId(model.getId() + "");
            modelRespVO.setCreateTime(model.getCreateTime());
            modelRespVO.setName(model.getName());
            modelRespVO.setKey(model.getKey());
            modelRespVO.setCategory(model.getCategory());
            // metaInfo
            if (metaInfo != null) {
                modelRespVO.setDescription(metaInfo.getDescription());
                modelRespVO.setFormType(metaInfo.getFormType());
                modelRespVO.setFormId(metaInfo.getFormId());
                modelRespVO.setFormCustomCreatePath(metaInfo.getFormCustomCreatePath());
                modelRespVO.setFormCustomViewPath(metaInfo.getFormCustomViewPath());
            }
            // Form
            if (form != null) {
                modelRespVO.setFormId(form.getId());
                modelRespVO.setFormName(form.getName());
            }

            if (processDefinition != null) {
                BpmModelPageItemRespVO.ProcessDefinition subProcessDef = new BpmModelPageItemRespVO.ProcessDefinition();
                subProcessDef.setId(processDefinition.getId());
                subProcessDef.setVersion(processDefinition.getVersion());
                // ProcessDefinition
                modelRespVO.setProcessDefinition(subProcessDef);
                if (modelRespVO.getProcessDefinition() != null) {
                    modelRespVO.getProcessDefinition().setSuspensionState(processDefinition.getSuspensionState());
                    modelRespVO.getProcessDefinition().setDeploymentTime(DateUtils.of(deployment.getDeploymentTime()));
                }
            }
            return modelRespVO;
        });
        return new PageResult<>(bpmModelPageItemRespVOS,modelCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createModel(@Valid BpmModelCreateReqVO createReqVO, String bpmnXml) {
        checkKeyNCName(createReqVO.getKey());
        // 校验流程标识已经存在
        BpmModelPlusDO keyModel = getModelByKey(createReqVO.getKey());
        if (keyModel != null) {
            throw exception(MODEL_KEY_EXISTS, createReqVO.getKey());
        }


        BpmModelPlusDO bpmModelPlusDO = new BpmModelPlusDO();
        String key = createReqVO.getKey();
        String description = createReqVO.getDescription();
        String name = createReqVO.getName();

        bpmModelPlusDO.setKey(key);
        bpmModelPlusDO.setName(name);
        bpmModelPlusDO.setDescription(description);
        BpmModelMetaInfoRespDTO metaInfo = new BpmModelMetaInfoRespDTO();
        metaInfo.setDescription(description);
        bpmModelPlusDO.setFormInfo(JsonUtils.toJsonString(metaInfo));
        bpmModelPlusDO.setBpmnXml(bpmnXml);
        bpmModelPlusDO.setCreateTime(LocalDateTime.now());
        bpmModelPlusMapper.insert(bpmModelPlusDO);
        // 保存 BPMN XML
        return bpmModelPlusDO.getId()+"";
    }

    private BpmModelPlusDO getModelByKey(String key) {
        LambdaQueryWrapper<BpmModelPlusDO> lambdaQueryWrapper= new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BpmModelPlusDO::getKey,key);
        BpmModelPlusDO bpmModelPlusDO = bpmModelPlusMapper.selectOne(lambdaQueryWrapper);
        return bpmModelPlusDO;
    }

    @Override
    public BpmModelRespVO getModel(String id) {
        LambdaQueryWrapper<BpmModelPlusDO> lambdaQueryWrapper= new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BpmModelPlusDO::getId,id);
        BpmModelPlusDO bpmModelPlusDO = bpmModelPlusMapper.selectOne(lambdaQueryWrapper);
        if (bpmModelPlusDO == null) {
            return null;
        }

        BpmModelRespVO modelRespVO = new BpmModelRespVO();
        modelRespVO.setId(bpmModelPlusDO.getId()+"");
        modelRespVO.setCreateTime(bpmModelPlusDO.getCreateTime());


        modelRespVO.setName(bpmModelPlusDO.getName());
        modelRespVO.setKey(bpmModelPlusDO.getKey());
        modelRespVO.setCategory(bpmModelPlusDO.getCategory());
        // metaInfo
        BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(bpmModelPlusDO.getFormInfo(), BpmModelMetaInfoRespDTO.class);
        if(metaInfo!=null){
            modelRespVO.setDescription(metaInfo.getDescription());
            modelRespVO.setFormType(metaInfo.getFormType());
            modelRespVO.setFormId(metaInfo.getFormId());
            modelRespVO.setFormCustomCreatePath(metaInfo.getFormCustomCreatePath());
            modelRespVO.setFormCustomViewPath(metaInfo.getFormCustomViewPath());
        }
        modelRespVO.setBpmnXml(bpmModelPlusDO.getBpmnXml());
        return modelRespVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 因为进行多个操作，所以开启事务
    public void updateModel(@Valid BpmModelUpdateReqVO updateReqVO) {
        LambdaQueryWrapper<BpmModelPlusDO> lambdaQueryWrapper= new LambdaQueryWrapper<>();
        LambdaQueryWrapper<BpmModelPlusDO> bpmModelPlusDOLambdaQueryWrapper = lambdaQueryWrapper.eq(BpmModelPlusDO::getId, updateReqVO.getId());
        // 校验流程模型存在
        BpmModelPlusDO bpmModelPlusDO = bpmModelPlusMapper.selectOne(bpmModelPlusDOLambdaQueryWrapper);
        if (bpmModelPlusDO == null) {
            throw exception(MODEL_NOT_EXISTS);
        }
        // 修改流程定义
        bpmModelPlusDO.setName(updateReqVO.getName());
        bpmModelPlusDO.setCategory(updateReqVO.getCategory());

        BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(bpmModelPlusDO.getFormInfo(), BpmModelMetaInfoRespDTO.class);
        if (metaInfo == null) {
            metaInfo = new BpmModelMetaInfoRespDTO();
        }
        // 只有非空，才进行设置，避免更新时的覆盖
        if (StrUtil.isNotEmpty(updateReqVO.getDescription())) {
            metaInfo.setDescription(updateReqVO.getDescription());
        }
        Integer formType = updateReqVO.getFormType();
        if (Objects.nonNull(formType)) {
            metaInfo.setFormType(formType);
            metaInfo.setFormId(updateReqVO.getFormId());
            metaInfo.setFormCustomCreatePath(updateReqVO.getFormCustomCreatePath());
            metaInfo.setFormCustomViewPath(updateReqVO.getFormCustomViewPath());
        }
        bpmModelPlusDO.setFormInfo(JsonUtils.toJsonString(metaInfo));

        String bpmnXml = updateReqVO.getBpmnXml();
        bpmModelPlusDO.setBpmnXml(bpmnXml);
        bpmModelPlusMapper.updateById(bpmModelPlusDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 因为进行多个操作，所以开启事务
    public void deployModel(String id) {
        // 1.1 校验流程模型存在
        LambdaQueryWrapper<BpmModelPlusDO> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BpmModelPlusDO::getId,id);
        BpmModelPlusDO bpmModelPlusDO = bpmModelPlusMapper.selectOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(bpmModelPlusDO)) {
            throw exception(MODEL_NOT_EXISTS);
        }
        // 1.2 校验流程图
        // TODO 芋艿：校验流程图的有效性；例如说，是否有开始的元素，是否有结束的元素；
        String bpmnXml = bpmModelPlusDO.getBpmnXml();
        if (StringUtils.isBlank(bpmnXml)) {
            throw exception(MODEL_NOT_EXISTS);
        }


        InputStream inputStream=null;
        BpmnModelInstance bpmnModelInstance=null;
        try {
            inputStream = new ByteArrayInputStream(
                    new String(bpmnXml).getBytes());
            bpmnModelInstance = Bpmn.readModelFromStream(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if(StringUtils.isNotBlank(bpmnXml)){
            bpmnXml=bpmnXml.replace("targetNamespace=\"http://bpmn.io/schema/bpmn\"","targetNamespace=\""+bpmModelPlusDO.getCategory()+"\"");
        }

        // 1.3 校验表单已配
        BpmFormDO form = checkFormConfig(bpmModelPlusDO.getFormInfo());
        // 1.4 校验任务分配规则已配置
        taskAssignRuleService.checkTaskAssignRuleAllConfig(id);

        // 1.5 校验模型是否发生修改。如果未修改，则不允许创建
        BpmProcessDefinitionCreateReqDTO definitionCreateReqDTO = BpmModelConvert.INSTANCE.convert2(bpmModelPlusDO, form).setBpmnBytes(StrUtil.utf8Bytes(bpmnXml));
//        if (processDefinitionService.isProcessDefinitionEquals(definitionCreateReqDTO)) { // 流程定义的信息相等
//            ProcessDefinition oldProcessDefinition = processDefinitionService.getProcessDefinitionByDeploymentId(bpmModelPlusDO.getDeploymentId());
//            if (oldProcessDefinition != null && taskAssignRuleService.isTaskAssignRulesEquals(bpmModelPlusDO.getId()+"", oldProcessDefinition.getId())) {
//                throw exception(MODEL_DEPLOY_FAIL_TASK_INFO_EQUALS);
//            }
//        }

        // 2.1 创建流程定义
        String definitionId = processDefinitionService.createProcessDefinition(definitionCreateReqDTO);

//        // 2.2 将老的流程定义进行挂起。也就是说，只有最新部署的流程定义，才可以发起任务。
//        updateProcessDefinitionSuspended(model.getDeploymentId());

        // 2.3 更新 model 的 deploymentId，进行关联
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(definitionId);
        bpmModelPlusDO.setDeploymentId(definition.getDeploymentId());
        bpmModelPlusMapper.updateById(bpmModelPlusDO);

        // 2.4 复制任务分配规则
        taskAssignRuleService.copyTaskAssignRules(id, definition.getId());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModel(String id) {

        LambdaQueryWrapper<BpmModelPlusDO> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BpmModelPlusDO::getId,id);
        BpmModelPlusDO bpmModelPlusDO = bpmModelPlusMapper.selectOne(lambdaQueryWrapper);

        if (bpmModelPlusDO == null) {
            throw exception(MODEL_NOT_EXISTS);
        }
        // 执行删除
        bpmModelPlusMapper.deleteById(bpmModelPlusDO);
        // 禁用流程实例
        updateProcessDefinitionSuspended(bpmModelPlusDO.getDeploymentId());
    }

    @Override
    public void updateModelState(String id, Integer state) {
        LambdaQueryWrapper<BpmModelPlusDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BpmModelPlusDO::getId,id);
        BpmModelPlusDO bpmModelPlusDO = bpmModelPlusMapper.selectOne(lambdaQueryWrapper);
        // 校验流程模型存在
        if (bpmModelPlusDO == null) {
            throw exception(MODEL_NOT_EXISTS);
        }
        // 校验流程定义存在
        ProcessDefinition definition = processDefinitionService.getProcessDefinitionByDeploymentId(bpmModelPlusDO.getDeploymentId());
        if (definition == null) {
            throw exception(PROCESS_DEFINITION_NOT_EXISTS);
        }

        // 更新状态
        processDefinitionService.updateProcessDefinitionState(definition.getId(), state);
    }

    @Override
    public BpmnModelInstance getBpmnModel(String id) {
        LambdaQueryWrapper<BpmModelPlusDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BpmModelPlusDO::getId,id);
        BpmModelPlusDO bpmModelPlusDO = bpmModelPlusMapper.selectOne(lambdaQueryWrapper);
        String bpmnXml = bpmModelPlusDO.getBpmnXml();
        if(StringUtils.isBlank(bpmnXml)){
            return null;
        }
        InputStream inputStream=null;
        BpmnModelInstance bpmnModelInstance=null;
        try {
             inputStream = new ByteArrayInputStream(
                    new String(bpmnXml).getBytes());
             bpmnModelInstance = Bpmn.readModelFromStream(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return bpmnModelInstance;
    }

    private void checkKeyNCName(String key) {
        if (!ValidationUtils.isXmlNCName(key)) {
            throw exception(MODEL_KEY_VALID);
        }
    }

    /**
     * 校验流程表单已配置
     *
     * @param metaInfoStr 流程模型 metaInfo 字段
     * @return 流程表单
     */
    private BpmFormDO checkFormConfig(String  metaInfoStr) {
        BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(metaInfoStr, BpmModelMetaInfoRespDTO.class);
        if (metaInfo == null || metaInfo.getFormType() == null) {
            throw exception(MODEL_DEPLOY_FAIL_FORM_NOT_CONFIG);
        }
        // 校验表单存在
        if (Objects.equals(metaInfo.getFormType(), BpmModelFormTypeEnum.NORMAL.getType())) {
            BpmFormDO form = bpmFormService.getForm(metaInfo.getFormId());
            if (form == null) {
                throw exception(FORM_NOT_EXISTS);
            }
            return form;
        }
        return null;
    }


    /**
     * 挂起 deploymentId 对应的流程定义。 这里一个deploymentId 只关联一个流程定义
     * @param deploymentId 流程发布Id.
     */
    private void updateProcessDefinitionSuspended(String deploymentId) {
        if (StrUtil.isEmpty(deploymentId)) {
            return;
        }
        ProcessDefinition oldDefinition = processDefinitionService.getProcessDefinitionByDeploymentId(deploymentId);
        if (oldDefinition == null) {
            return;
        }
        processDefinitionService.updateProcessDefinitionState(oldDefinition.getId(), SuspensionState.SUSPENDED.getStateCode());
    }


}
