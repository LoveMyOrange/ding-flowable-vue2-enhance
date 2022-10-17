package com.dingding.mid.dto;

import com.alibaba.fastjson.JSONObject;
import com.dingding.mid.dto.json.UserInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author LoveMyOrange
 * @create 2022-10-14 23:27
 */
@Data
public class StartProcessInstanceDTO {
    private String processDefinitionId;
    private JSONObject formData;
    private Map<String, List<UserInfo>> processUsers;
    private UserInfo startUserInfo;
}
