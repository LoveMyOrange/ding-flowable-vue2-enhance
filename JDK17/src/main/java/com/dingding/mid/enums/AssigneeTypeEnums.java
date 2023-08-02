package com.dingding.mid.enums;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 18:15
 */
public enum  AssigneeTypeEnums {
  ASSIGN_USER("ASSIGN_USER"),
  SELF_SELECT("SELF_SELECT"),
  LEADER_TOP("LEADER_TOP"),
  LEADER("LEADER"),
  ROLE("ROLE"),
  SELF("SELF"),
  FORM_USER("FORM_USER");

  private String typeName;

  AssigneeTypeEnums(String typeName) {
    this.typeName = typeName;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }
}
