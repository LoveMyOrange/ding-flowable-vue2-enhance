package com.dingding.mid.vo;

import com.dingding.mid.dto.json.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author LoveMyOrange
 * @create 2022-10-14 23:47
 */
@Data
@ApiModel("待办 需要返回给前端的VO")
public class TaskVO {
    @ApiModelProperty("任务id")
    private String taskId;
    @ApiModelProperty("任务id")
    private String processInstanceId;
    @ApiModelProperty("审批类型")
    private String processDefinitionName;
    @ApiModelProperty("发起人")
    private UserInfo startUser;
    @ApiModelProperty("提交时间")
    private Date startTime;
    @ApiModelProperty("任务到达时间")
    private Date taskCreatedTime;
    @ApiModelProperty("当前节点")
    private String currentActivityName;
    @ApiModelProperty("审批状态")
    private String businessStatus;
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("耗时")
    private String duration;
}
