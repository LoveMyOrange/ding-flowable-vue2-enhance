package com.dingding.mid.dto;

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
public class TaskDTO extends PageDTO {
    private UserInfo currentUserInfo;
}
