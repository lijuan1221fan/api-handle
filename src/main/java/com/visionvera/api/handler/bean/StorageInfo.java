package com.visionvera.api.handler.bean;


/**
 * 存储网关返回体
 *
 * @author EricShen
 */
public class StorageInfo {

  private boolean result;
  private String msg;
  private Object data;

  public boolean getResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public StorageInfo(boolean result, String msg) {
    this.result = result;
    this.msg = msg;
  }

  @Override
  public String toString() {
    return "StorageInfo{" + "result=" + result + ", msg='" + msg + '\'' + ", data=" + data + '}';
  }
}
