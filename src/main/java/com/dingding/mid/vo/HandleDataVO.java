package com.dingding.mid.vo;

import com.alibaba.fastjson.JSONObject;
import com.dingding.mid.dto.json.ChildNode;
import com.dingding.mid.dto.json.UserInfo;
import com.dingding.mid.entity.ProcessTemplates;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 16:27
 */
@Data
@ApiModel("详情VO")
public class HandleDataVO {
    @ApiModelProperty("任务id")
    private String taskId;
    @ApiModelProperty("流程实例id")
    private String processInstanceId;
    @ApiModelProperty("表单数据")
    private JSONObject formData;
    @ApiModelProperty("前端是否打开 签名板")
    private Boolean signFlag;
    @ApiModelProperty("流程模板")
    private ProcessTemplates processTemplates;
    @ApiModelProperty("当前节点json数据 如果有taskId的话才返回")
    private ChildNode currentNode;
    @ApiModelProperty("任务详情")
    private Map<String,List<TaskDetailVO>> detailVOList;
    @ApiModelProperty("已经结束的节点")
    List<String> endList;
    @ApiModelProperty("正在运行的节点")
    List<String> runningList;
    @ApiModelProperty("还没运行的节点")
    List<String> noTakeList;
}
