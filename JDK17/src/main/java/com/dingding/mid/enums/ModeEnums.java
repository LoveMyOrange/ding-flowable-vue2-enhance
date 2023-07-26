package com.dingding.mid.enums;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 18:15
 */
public enum ModeEnums {
  AND("AND"),
  OR("OR"),
  NEXT("NEXT");

  private String typeName;

  ModeEnums(String typeName) {
    this.typeName = typeName;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }
}
