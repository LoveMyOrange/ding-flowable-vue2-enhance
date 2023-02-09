package com.dingding.mid.dto.json;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:20
 *
 * process: "
 * {\"id\":\"root\",\
 * "parentId\":null,\
 * "type\":\"ROOT\",\
 * "name\":\"发起人\",\
 * "desc\":\"任何人\",\
 * "props\":{\
 * "assignedUser\":


[{\"id\":1486186,\"name\":\"xx科技有限公司\",\"type\":\"dept\",\"sex\":null,\"selected\":false}],\
 "formPerms\":[{\"id\":\"field6131501574832\",\"title\":\"单行文本输入\",\"required\":true,\"perm\":\"E\"}]},\
  "children\":{\"id\":\"node_040730749764\",\"parentId\":\"root\",\"props\":{\"assignedType\":\"ASSIGN_USER\",\"mode\":\"AND\",\"sign\":true,\"nobody\":{\"handler\":\"TO_PASS\",\"assignedUser\":[]},\"timeLimit\":{\"timeout\":{\"unit\":\"H\",\"value\":\"1\"},\"handler\":{\"type\":\"REFUSE\",\"notify\":{\"once\":true,\"hour\":1}}},\"assignedUser\":[{\"id\":381496,\"name\":\"旅人\",\"type\":\"user\",\"sex\":false,\"selected\":false}],\"formPerms\":[{\"id\":\"field6131501574832\",\"title\":\"单行文本输入\",\"required\":true,\"perm\":\"R\"}],\"selfSelect\":{\"multiple\":false},\"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\"leader\":{\"level\":1},\"role\":[],\"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"formUser\":\"\"},\"type\":\"APPROVAL\",\"name\":\"审批人\",\"children\":{}}}"
 */
@Data
public class ChildNode {
  private String id;
  private String parentId;
  private String type;
  private String name;
  private String desc;
  private Properties props;
  private ChildNode children;
  private List<ChildNode> branchs;
  private String parallelStr;
  private JSONObject incoming=new JSONObject();
  private Boolean typeElse;
}

