package com.dingding.mid.dto;

import com.alibaba.fastjson.JSONObject;
import com.dingding.mid.dto.json.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 16:27
 */
@Data
@Schema(description = "各个按钮 处理数据需要传递的参数")
public class HandleDataDTO {
    @Schema(name="任务id")
    private String taskId;
    @Schema(name="流程实例id")
    private String processInstanceId;
    @Schema(name="表单数据")
    private JSONObject formData;
    @Schema(name="附件")
    private List attachments;
    @Schema(name="意见")
    private String comments;
    @Schema(name="签名信息")
    private String signInfo;
    @Schema(name="转办用户信息")
    private UserInfo transferUserInfo;
    @Schema(name="加签用户信息")
    private UserInfo multiAddUserInfo;
    @Schema(name="退回节点id")
    private String rollbackId;
    @Schema(name="当前用户信息")
    private UserInfo currentUserInfo;

    @Schema(name="委派的人")
    private UserInfo delegateUserInfo;
}
