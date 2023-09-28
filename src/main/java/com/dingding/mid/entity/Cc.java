package com.dingding.mid.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dingding.mid.dto.json.UserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (Users)实体类
 *
 * @author makejava
 * @since 2020-09-17 14:18:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "cc")
public class Cc implements Serializable {
    /**
     * 用户id
     */
    @TableId
    private String id;
    private Long userId;
    private String processInstanceId;


    @TableField(exist = false)
    @ApiModelProperty("审批类型")
    private String processDefinitionName;
    @TableField(exist = false)
    @ApiModelProperty("发起人")
    private UserInfo startUser;
    @TableField(exist = false)
    @ApiModelProperty("发起人(带icon)")
    private Users users;
    @TableField(exist = false)
    @ApiModelProperty("提交时间")
    private Date startTime;
    @TableField(exist = false)
    @ApiModelProperty("结束时间")
    private Date endTime;
    @TableField(exist = false)
    @ApiModelProperty("当前节点")
    private String currentActivityName;
    @TableField(exist = false)
    @ApiModelProperty("审批状态")
    private String businessStatus;
    @TableField(exist = false)
    @ApiModelProperty("耗时")
    private String duration;

}
