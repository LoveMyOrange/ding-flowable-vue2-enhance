package com.dingding.mid.job;

import com.alibaba.fastjson.JSONObject;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.job.api.Job;
import org.flowable.job.service.TimerJobService;
import org.flowable.job.service.impl.persistence.entity.TimerJobEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 21:51
 */
public class CustomJobCmd implements Command<Void>, Serializable {
    protected String processInstanceId;
    protected String taskId;
    protected String executionId;
    protected Date dueDate;
    protected String type;
    protected String comment;

    public CustomJobCmd(String processInstanceId, String comment, String taskId, String executionId, Date dueDate, String type) {
        this.processInstanceId = processInstanceId;
        this.comment = comment;
        this.taskId = taskId;
        this.executionId = executionId;
        this.dueDate = dueDate;
        this.type = type;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("comment", this.comment);
        jsonObject.put("task_id", this.taskId);

        TimerJobService timerJobService = CommandContextUtil.getTimerJobService(commandContext);
        TimerJobEntity job = timerJobService.createTimerJob();
        job.setJobType(Job.JOB_TYPE_TIMER);
        job.setExclusive(true);
        // 作业处理器类型
        job.setJobHandlerType(CustomJobHandler.TYPE);
        // 处理时间
        job.setDuedate(this.dueDate);
        job.setExecutionId(null);
        job.setProcessInstanceId(this.processInstanceId);
        job.setJobHandlerConfiguration(jsonObject.toJSONString());
        timerJobService.scheduleTimerJob(job);
        return null;
    }
}
