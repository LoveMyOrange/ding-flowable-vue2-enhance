package com.dingding.mid.dto.json;

import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:10
 */
@Data
public class UserInfo {
  private String id;
  private String name;
  private String type;
  private String sex;
  private Boolean selected;
}
