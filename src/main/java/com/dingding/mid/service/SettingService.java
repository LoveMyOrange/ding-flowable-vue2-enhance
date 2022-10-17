package com.dingding.mid.service;

import com.dingding.mid.dto.FlowEngineDTO;
import com.dingding.mid.dto.json.ChildNode;
import com.dingding.mid.entity.ProcessTemplates;
import com.dingding.mid.vo.TemplateGroupVo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author : willian fu
 * @date : 2020/9/21
 */
public interface SettingService {

    /**
     * 查询表单组
     * @return 表单组数据
     */
    Object getFormGroups(String token, String name);

    /**
     * 表单及分组排序
     * @param groups 分组数据
     * @return 排序结果
     */
    Object formGroupsSort(List<TemplateGroupVo> groups);

    /**
     * 查询表单模板数据
     * @param templateId 模板id
     * @return 模板详情数据
     */
    Object getFormTemplateById(String templateId);

    /**
     * 修改分组
     * @param id 分组ID
     * @param name 分组名
     * @return 修改结果
     */
    Object updateFormGroupName(Integer id, String name);

    /**
     * 新增表单分组
     * @param name 分组名
     * @return 添加结果
     */
    Object createFormGroup(String name);

    /**
     * 删除分组
     * @param id 分组ID
     * @return 删除结果
     */
    Object deleteFormGroup(Integer id);

    /**
     * 编辑表单
     * @param templateId 摸板ID
     * @param type 类型 stop using delete
     * @return 操作结果
     */
    Object updateForm(String templateId, String type, Integer groupId);

    /**
     * 编辑表单详情
     * @param template 表单模板信息
     * @return 修改结果
     */
    Object updateFormDetail(ProcessTemplates template);


    void jsonToBpmn(FlowEngineDTO flowEngineDTO) throws InvocationTargetException, IllegalAccessException;
}
