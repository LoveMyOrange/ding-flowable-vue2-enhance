package com.dingding.mid.controller.antd;

import com.alibaba.fastjson.JSONObject;
import com.dingding.mid.common.R;
import com.dingding.mid.utils.BpmnModelUtils;
import com.dingding.mid.utils.IdWorker;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static com.dingding.mid.utils.BpmnModelUtils.createExclusiveGateWayEnd;

/**
 * @author : lzgabel
 * @version : 1.0
 */
@RestController
@RequestMapping("/antd")
@Api("开整")
public class JsonToBpmnController {

    @Resource
    private IdWorker idWorker;

    @PostMapping("/jsonToBpmn")
    public Object saveForm(@RequestBody JSONObject jsonObject) throws InvocationTargetException, IllegalAccessException {


        return R.ok("保存成功");
    }
}



