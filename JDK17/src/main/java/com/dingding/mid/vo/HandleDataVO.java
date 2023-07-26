package com.dingding.mid.vo;

import com.alibaba.fastjson.JSONObject;
import com.dingding.mid.dto.json.ChildNode;
import com.dingding.mid.dto.json.UserInfo;
import com.dingding.mid.entity.ProcessTemplates;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 16:27
 */
@Data
@Schema(description = "详情VO")
public class HandleDataVO {
    @Schema(name="任务id")
    private String taskId;
    @Schema(name="流程实例id")
    private String processInstanceId;
    @Schema(name="表单数据")
    private JSONObject formData;
    @Schema(name="前端是否打开 签名板")
    private Boolean signFlag;
    @Schema(name="流程模板")
    private ProcessTemplates processTemplates;
    @Schema(name="当前节点json数据 如果有taskId的话才返回")
    private ChildNode currentNode;
    @Schema(name="任务详情")
    private Map<String,List<TaskDetailVO>> detailVOList;
    @Schema(name="已经结束的节点")
    List<String> endList;
    @Schema(name="正在运行的节点")
    List<String> runningList;
    @Schema(name="还没运行的节点")
    List<String> noTakeList;
}
