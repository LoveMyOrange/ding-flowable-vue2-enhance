package com.dingding.mid.dto.json;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 20:04
 */
@Data
public class HttpInfo {
  private String method;
  private String url;
  private List<Map<String,Object>> headers;
  private String contentType;
  private List<Map<String,Object>> params;
  private Integer retry;
  private Boolean handlerByScript;
  private String success;
  private String fail;
}
