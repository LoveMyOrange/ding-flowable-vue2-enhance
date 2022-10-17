package com.dingding.mid.dto.json;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:15
 */
@Data
public class NotifyTypeInfo {
  private String title;
  private JSONObject types;
}
