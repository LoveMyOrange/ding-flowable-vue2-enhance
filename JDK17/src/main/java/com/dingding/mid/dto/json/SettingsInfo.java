package com.dingding.mid.dto.json;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:06
 */
@Data
public class SettingsInfo {
  private List<String> commiter;
  private List<UserInfo> admin;
  private Boolean sign;
  private JSONObject notify;
}
