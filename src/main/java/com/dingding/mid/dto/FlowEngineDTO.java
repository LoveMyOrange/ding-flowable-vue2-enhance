package com.dingding.mid.dto;

import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 15:51
 */
@Data
public class FlowEngineDTO {
  private String formId;
  private String formItems;
  private String formName;
  //表单分类id
  private Integer groupId;
  private String logo;
  private String process;
  private String remark;
  private String settings;
}
