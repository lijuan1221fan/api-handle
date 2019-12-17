package com.visionvera.api.handler.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * 返回结果
 *
 * @author EricShen
 */
public class ResultEntity<T> {

  /**
   * 结果
   */
  private boolean result;

  /**
   * 返回码
   */
  private ResponseCode responseCode;

  /**
   * 数据
   */
  private T data;

  private ResultEntity(boolean result, ResponseCode responseCode, T data) {
    this.result = result;
    this.responseCode = responseCode;
    this.data = data;
  }

  public boolean getResult() {
    return result;
  }

  public ResponseCode getResponseCode() {
    return responseCode;
  }

  public T getData() {
    return data;
  }

  @Override
  public String toString() {
    return "ResultEntity{" + "result=" + result + ", responseCode=" + responseCode + ", data="
        + data + '}';
  }

  /**
   * 成功返回无数据
   *
   * @return
   */
  public static <T> ResultEntity<T> success() {
    return ResultEntity.success(null);
  }

  /**
   * 成功返回带数据
   *
   * @param data 数据
   * @return
   */
  public static <T> ResultEntity<T> success(T data) {
    return ResultEntity.getResult(true, ResponseCode.SUCCEED, data);
  }

  /**
   * 失败返回无数据
   *
   * @return
   */
  public static <T> ResultEntity<T> error() {
    return ResultEntity.error(ResponseCode.ERROR);
  }

  /**
   * 失败返回，自定义返回码
   *
   * @param code 返回码
   * @return
   */
  public static <T> ResultEntity<T> error(ResponseCode code) {
    return ResultEntity.error(code, null);
  }

  /**
   * 失败返回带数据
   *
   * @param code 返回码
   * @param data 数据
   * @return
   */
  public static <T> ResultEntity<T> error(ResponseCode code, T data) {
    return ResultEntity.getResult(false, code, data);
  }

  /**
   * 自定义返回
   *
   * @param result 结果
   * @param code 返回码
   * @param data 数据
   * @return
   */
  public static <T> ResultEntity<T> getResult(boolean result, ResponseCode code, T data) {
    return new ResultEntity<T>(result, code, data);
  }

  /**
   * 测试
   */
  public static void main(String[] args) {
    Map<String, String> data = new HashMap<String, String>(1);
    data.put("2", "3");

    ResultEntity<Map<String, String>> resltEntity = ResultEntity.success();
    ResultEntity<Map<String, String>> resltEntity1 = ResultEntity.success(data);
    ResultEntity<Map<String, String>> resltEntity2 = ResultEntity
        .error(ResponseCode.TIMEOUT_ERROR, data);
    ResultEntity<Map<String, String>> resltEntity3 = ResultEntity.error(ResponseCode.NETWORK_ERROR);
    ResultEntity<Map<String, String>> resltEntity4 = ResultEntity.error();

    boolean result = resltEntity.getResult();
    ResponseCode responseCode = resltEntity.getResponseCode();
    Map<String, String> data1 = resltEntity.getData();
    System.out.println(result);
    System.out.println(responseCode);
    System.out.println(responseCode.getCode());
    System.out.println(data1);
    System.out.println(resltEntity);
    System.out.println(resltEntity1);
    System.out.println(resltEntity2);
    System.out.println(resltEntity3);
    System.out.println(resltEntity4);

  }
}
