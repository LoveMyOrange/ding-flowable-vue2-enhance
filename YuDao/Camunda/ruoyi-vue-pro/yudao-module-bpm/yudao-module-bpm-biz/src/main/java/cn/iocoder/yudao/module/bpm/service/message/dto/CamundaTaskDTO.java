package cn.iocoder.yudao.module.bpm.service.message.dto;

import lombok.Data;

import java.util.Date;

/**

 BpmTaskExtDO taskExtDO = new BpmTaskExtDO()
 .setTaskId(task.getId())
 .setAssigneeUserId(NumberUtils.parseLong(task.getAssignee())).setName(task.getName())
 .setProcessDefinitionId(task.getProcessDefinitionId()).setProcessInstanceId(task.getProcessInstanceId());
 taskExtDO.setCreateTime(LocalDateTimeUtil.of(task.getCreateTime()));

 */
@Data
public class CamundaTaskDTO {
    private String taskId;
    private Long assigneeUserId;
    private String name;
    private String processDefinitionId;
    private String processInstanceId;
    private Date createTime;
    private Long processStartUserId;
    private String processDefinitionName;
}
