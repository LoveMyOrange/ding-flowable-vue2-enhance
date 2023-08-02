package com.dingding.mid.dto;

import com.dingding.mid.dto.json.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author LoveMyOrange
 * @create 2022-10-14 23:47
 */
@Data
@Schema(description = "待办 需要返回给前端的VO")
public class TaskDTO extends PageDTO {
    private UserInfo currentUserInfo;
}
