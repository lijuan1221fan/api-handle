package com.visionvera.api.handler.utils;

import java.util.UUID;


/**
 * 字符串工具类
 *
 * @author EricShen
 */
public class StringUtil {

  public static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  public static boolean isEmptyByArr(String... strArr) {
    for (String str : strArr) {
      if (str == null || str.length() == 0) {
        return true;
      }
    }
    return false;
  }

  public static String getUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString().replaceAll("\\-", "");
  }

}
