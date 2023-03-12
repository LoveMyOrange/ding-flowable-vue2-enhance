package cn.iocoder.yudao.module.bpm.service.message.dto;

import lombok.Data;

@Data
public class CamundaProcessInstanceDTO {
    private String processInstanceId;
    private String processDefinitionName;
    private String processDefinitionId;
    private Long processStartUserId;
}
