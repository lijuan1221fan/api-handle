package com.visionvera.api.handler.utils;

/**
 * 存储网关资源操作基础返回体
 *
 * @author EricShen
 * @date 2018-12-25
 */
public class ResReturnBaseEntity {

  private Integer errcode;
  private String errmsg;

  public Integer getErrcode() {
    return errcode;
  }

  public void setErrcode(Integer errcode) {
    this.errcode = errcode;
  }

  public String getErrmsg() {
    return errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }
}
