package com.dingding.mid.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/22 15:33
 */
@ApiModel("减签前一步操作")
@Data
public class MultiVO {
  private String taskId;
  private String processInstanceId;
  private String executionId;
  private String userId;
  private String userName;
}
