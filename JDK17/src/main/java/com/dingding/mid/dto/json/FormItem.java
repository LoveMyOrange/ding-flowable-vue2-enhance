package com.dingding.mid.dto.json;

import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 15:55
 */
@Data
public class FormItem {
  private String title;
  private String name;
  private String icon;
  private String value;
  private String valueType;
  private String id;
  private FormItemProps props;
}
