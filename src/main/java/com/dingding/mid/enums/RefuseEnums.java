package com.dingding.mid.enums;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 18:15
 */
public enum RefuseEnums {
  TO_END("TO_END"),
  TO_BEFORE("TO_BEFORE"),
  TO_NODE("TO_NODE");

  private String typeName;

  RefuseEnums(String typeName) {
    this.typeName = typeName;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }
}
