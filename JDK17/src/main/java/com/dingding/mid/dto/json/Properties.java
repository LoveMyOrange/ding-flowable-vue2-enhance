package com.dingding.mid.dto.json;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:30

\"assignedType\":\"ASSIGN_USER\",\"mode\":\"AND\",\"sign\":true,\
"nobody\":{\"handler\":\"TO_PASS\",\"assignedUser\":[]},\
"timeLimit\":{\"timeout\":{\"unit\":\"H\",\"value\":\"1\"},\
"handler\":{\"type\":\"REFUSE\",\"notify\":{\"once\":true,\"hour\":1}}},\
"assignedUser\":[{\"id\":381496,\"name\":\"旅人\",\"type\":\"user\",\"sex\":false,\"selected\":false}],\
"formPerms\":[{\"id\":\"field6131501574832\",\"title\":\"单行文本输入\",\"required\":true,\"perm\":\"R\"}],\
"selfSelect\":{\"multiple\":false},\
"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\
"leader\":{\"level\":1},\"role\":[],\
"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"formUser\":\"\"},\"type\":\"APPROVAL\",\"name\":\"审批人\",\"children\":{}}}"
 
 */
@Data
public class Properties {
  private String assignedType;
  private List<UserInfo> assignedUser;
  //发起人自旋  multiple true false
  private Map<String,Object> selfSelect;
  //连续主管 endCondition TOP   LEAVE    endLevel  level
  private Map<String,Object> leaderTop=new HashMap<>();
  //指定主管审批
  private Map<String,Object> leader=new HashMap<>();
  //系统角色
  private List<Map<String,Object>> role;
  //表单人员
  private String formUser;


  //审批人为空的规则  hander 和 assignedUser
  private Map<String,Object> nobody;
  private String mode;
  
  private Boolean sign;
  //审批超时
  private JSONObject timeLimit;

  private Map<String,Object> refuse;
  
  private List<FormOperates> formPerms;
  
  
  //------------------------------------->
  private String groupsType;
  private String expression;
  private List<GroupsInfo> groups;
  
  //
  private Boolean shouldAdd;
  //
  private String type;
  private Long time;
  private String unit;
  private String dateTime;
  
  //
  private HttpInfo http;
  private EmailInfo email;
}
