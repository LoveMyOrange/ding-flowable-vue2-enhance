package com.dingding.mid.vo;

import com.dingding.mid.entity.ProcessTemplates;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 16:09
 */
@Data
@Schema(description = "查看详情 需要返回给前端的VO")
public class ProcessInstanceDetailVO {

    private ProcessTemplates processTemplates;
    private String formData;
    private String processInstanceId;
    private List<String> completeIdList;
    private List<String> finishIdList;
    private List<String> noneList;
    private Boolean signFlag;
}
