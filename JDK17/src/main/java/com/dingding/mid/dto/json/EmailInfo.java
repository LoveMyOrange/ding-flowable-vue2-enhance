package com.dingding.mid.dto.json;

import java.util.List;
import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 20:08
 */
@Data
public class EmailInfo {
  private String subject;
  private List<String> to;
  private String content;
}
