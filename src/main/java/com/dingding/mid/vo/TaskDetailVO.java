package com.dingding.mid.vo;

import lombok.Data;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author LoveMyOrange
 * @create 2022-10-16 9:38
 */
@Data
public class TaskDetailVO {
    private String taskId;
    private String activityId;
    private String name;
    private Date createTime;
    private Date endTime;
    private String signImage;
    private List<AttachmentVO> attachmentVOList;
    private List<OptionVO> optionVOList;
    private List<CommentVO> commentVOList;
}
