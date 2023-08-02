package com.dingding.mid.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/22 15:33
 */
@Schema(description = "减签前一步操作")
@Data
public class MultiVO {
  private String taskId;
  private String processInstanceId;
  private String executionId;
  private String userId;
  private String userName;
}
