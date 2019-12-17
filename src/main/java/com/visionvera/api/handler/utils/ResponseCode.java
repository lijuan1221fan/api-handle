package com.visionvera.api.handler.utils;


/**
 * 响应码
 *
 * @author EricShen
 */
public enum ResponseCode {

  /**
   * 成功
   */
  SUCCEED("2000", "成功"),
  /**
   * 错误，catch中抛出异常
   */
  ERROR("3000", "错误,内部异常"),
  /**
   * 超时
   */
  TIMEOUT_ERROR("4001", "超时异常"),
  /**
   * 网络异常
   */
  NETWORK_ERROR("4002", "网络异常"),
  /**
   * 参数错误
   */
  PARAM_ERROR("4000", "参数错误"),
  /**
   * http请求错误
   */
  REQUEST_ERROR("4004", "http请求错误"),
  /**
   * 请求成功，但返回结果错误
   */
  RESPONSE_ERROR("4005", "请求成功，但返回结果错误");

  private String code;
  private String value;

  private ResponseCode(String code, String value) {
    this.code = code;
    this.value = value;
  }

  public String getCode() {
    return code;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "ResponseCode{" + "code='" + code + '\'' + ", value='" + value + '\'' + '}';
  }}
