package com.dingding.mid.dto;

import com.dingding.mid.dto.json.UserInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author LoveMyOrange
 * @create 2022-10-14 23:47
 */
@Data
@ApiModel("我发起流程 需要返回给前端的DTO")
public class ApplyDTO extends PageDTO {
    private UserInfo currentUserInfo;
}
