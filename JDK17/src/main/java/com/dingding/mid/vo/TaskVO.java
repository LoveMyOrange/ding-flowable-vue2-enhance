package com.dingding.mid.vo;

import com.dingding.mid.dto.json.UserInfo;
import com.dingding.mid.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.flowable.task.api.DelegationState;

import java.util.Date;

/**
 * @author LoveMyOrange
 * @create 2022-10-14 23:47
 */
@Data
@Schema(description = "待办 需要返回给前端的VO")
public class TaskVO {
    @Schema(name="任务id")
    private String taskId;
    @Schema(name="任务名称")
    private String taskName;
    @Schema(name="任务id")
    private String processInstanceId;
    @Schema(name="审批类型")
    private String processDefinitionName;
    @Schema(name="发起人")
    private UserInfo startUser;
    @Schema(name="提交时间")
    private Date startTime;
    @Schema(name="任务到达时间")
    private Date taskCreatedTime;
    @Schema(name="当前节点")
    private String currentActivityName;
    @Schema(name="审批状态")
    private String businessStatus;
    @Schema(name="结束时间")
    private Date endTime;
    @Schema(name="耗时")
    private String duration;
    @Schema(name="委派状态")
    private DelegationState delegationState;
    @Schema(name="发起人(带icon)")
    private Users users;
    @Schema(name="节点id")
    private String nodeId;
}
