package com.dingding.mid.controller.antd;

import com.alibaba.fastjson.JSONObject;
import com.dingding.mid.common.R;
import com.dingding.mid.utils.IdWorker;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * @author : lzgabel
 * @version : 1.0
 */
@RestController
@RequestMapping("/antd")
@Api("开整")
public class AntJsonToBpmnController {

    @Resource
    private IdWorker idWorker;

    @PostMapping("/jsonToBpmn")
    public Object saveForm(@RequestBody JSONObject jsonObject) throws InvocationTargetException, IllegalAccessException {


        return R.ok("保存成功");
    }
}



