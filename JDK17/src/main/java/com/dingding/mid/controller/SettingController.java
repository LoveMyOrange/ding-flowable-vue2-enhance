package com.dingding.mid.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dingding.mid.common.R;
import com.dingding.mid.dto.FlowEngineDTO;
import com.dingding.mid.dto.json.ChildNode;
import com.dingding.mid.entity.ProcessTemplates;
import com.dingding.mid.service.SettingService;
import com.dingding.mid.vo.TemplateGroupVo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : willian fu
 * @date : 2020/9/17
 */
@RestController
@RequestMapping("admin")
@Tag(name = "Vue2表单的CRUD接口")
@ApiSort(2)
public class SettingController {

    @Autowired
    private SettingService settingService;

    /**
     * 1>
     * @param flowEngineDTO
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @ApiOperationSupport(order = 0)
    @Operation(summary ="自定义表单的保存接口(会在此Json转Bpmn)")
    @PostMapping("/form")
    public Object saveForm(@RequestBody FlowEngineDTO flowEngineDTO) throws InvocationTargetException, IllegalAccessException {
        settingService.jsonToBpmn(flowEngineDTO);
        return R.ok("保存成功");
    }

    /**
     * 查询所有表单分组
     * @return
     */
    @GetMapping("form/group")
    public Object getFormGroups(){
        return settingService.getFormGroups(null, null);
    }

    /**
     * 表单分组排序
     * @param groups 分组数据
     * @return 排序结果
     */
    @PutMapping("form/group/sort")
    public Object formGroupsSort(@RequestBody List<TemplateGroupVo> groups){
        return settingService.formGroupsSort(groups);
    }

    /**
     * 修改分组
     * @param id 分组ID
     * @param name 分组名
     * @return 修改结果
     */
    @PutMapping("form/group")
    public Object updateFormGroupName(@RequestParam Integer id,
                                       @RequestParam String name){
        return settingService.updateFormGroupName(id, name);
    }

    /**
     * 新增表单分组
     * @param name 分组名
     * @return 添加结果
     */
    @PostMapping("form/group")
    public Object createFormGroup(@RequestParam String name){
        return settingService.createFormGroup(name);
    }

    /**
     * 删除分组
     * @param id 分组ID
     * @return 删除结果
     */
    @DeleteMapping("form/group")
    public Object deleteFormGroup(@RequestParam Integer id){
        return settingService.deleteFormGroup(id);
    }

    /**
     * 查询表单模板数据
     * @param templateId 模板id
     * @return 模板详情数据
     */
    @GetMapping("form/detail/{formId}")
    public Object getFormTemplateById(@PathVariable("formId") String templateId){
        return settingService.getFormTemplateById(templateId);
    }

    /**
     * 编辑表单
     * @param templateId 摸板ID
     * @param type 类型 stop using delete
     * @return 操作结果
     */
    @PutMapping("form")
    public Object updateForm(@RequestParam String templateId,
                             @RequestParam String type,
                             @RequestParam(required = false) Integer groupId){
        return settingService.updateForm(templateId, type, groupId);
    }

    /**
     * 编辑表单详情
     * @param template 表单模板信息
     * @return 修改结果
     */
    @PutMapping("form/detail")
    public Object updateFormDetail(@RequestBody ProcessTemplates template){
        return settingService.updateFormDetail(template);
    }
}
