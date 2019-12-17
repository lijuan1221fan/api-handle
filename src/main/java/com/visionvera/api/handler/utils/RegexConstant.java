package com.visionvera.api.handler.utils;


/**
 * 正则表达式
 *
 * @author EricShen
 */
public interface RegexConstant {

  /**
   * url剪切正则表达式
   */
  String URL_CUT_REGEX = "http://((\\d+\\.){3}(\\d+)):(\\d{2,5})/.*";
  /**
   * ip：端口  替换正则表达式
   */
  String IP_PORT_REPLACE_REGEX = "((\\d+\\.){3}(\\d+):(\\d{2,5}))";
  /**
   * 协议：//ip：端口替换 正则表达式
   */
  String PROTOCOL_IP_PORT_REPLACE_REGEX = "((\\d+\\.){3}(\\d+):(\\d{2,5}))";
  /**
   * 剪切ip
   */
  String CUT_IP = "$1";
  /**
   * 剪切端口
   */
  String CUT_PORT = "$4";

}
