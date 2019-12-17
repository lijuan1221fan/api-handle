package com.visionvera.api.handler.bean.res;

import com.alibaba.fastjson.annotation.JSONField;
import com.visionvera.api.handler.constant.StorageApi;
import com.visionvera.api.handler.utils.ParamBaseEntity;
import java.io.File;

/**
 * 存储网关资源操作请求体
 *
 * @author EricShen
 * @date 2018-12-24
 */
public class ResRequest extends ParamBaseEntity {

  /**
   * ip
   */
  private String ip;
  /**
   * 端口
   */
  private Integer port;
  /**
   * 免登陆标识，由内容管理平台提供
   */
  private String sessionID;

  /**
   * 0-16位；1-64位；2-流媒体
   *
   * @see StorageApi.PlatformType
   */
  private Integer platform;


  /**
   * 素材类型0文字 1图片；2视频； 3ppt;4word;5excel;6pdf
   *
   * @see StorageApi.ResType
   */
  private Integer resType;
  /**
   * 具体资源内容
   */
  @JSONField(serialize = false)
  private File resContent;

  /**
   * 上传素材 是否新增(1-新增，2—更新)
   *
   * @see StorageApi.UploadType
   */
  private Integer type;
  /**
   * 需要更新的资源ID
   */
  private Integer resId;
  /**
   * 上传的对象
   */
  private String sendParam;
  /**
   * 素材ID，多个素材ID用逗号分隔
   */
  private String resIds;
  /**
   * 是否删除 true 查询，false删除
   */
  private boolean delete;

  /**
   * 上传文件构造方法
   *
   * @param ip
   * @param port
   * @param platform
   * @param resType
   * @param type
   * @param resId
   * @param resContent
   */
  public ResRequest(String ip, Integer port, Integer platform, Integer resType, Integer type,
      Integer resId, File resContent) {
    this.ip = ip;
    this.port = port;
    this.platform = platform;
    this.resType = resType;
    this.type = type;
    this.resId = resId;
    this.resContent = resContent;
  }

  /**
   * 获取/删除文件构造方法
   *
   * @param ip
   * @param port
   * @param platform
   * @param resIds
   * @param isDelete
   */
  public ResRequest(String ip, Integer port, Integer platform, String resIds, boolean isDelete) {
    this.ip = ip;
    this.port = port;
    this.platform = platform;
    this.resIds = resIds;
    this.delete = isDelete;
  }

  public ResRequest() {
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getSessionID() {
    return sessionID;
  }

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  public Integer getPlatform() {
    return platform;
  }

  public void setPlatform(Integer platform) {
    this.platform = platform;
  }

  public Integer getResType() {
    return resType;
  }

  public void setResType(Integer resType) {
    this.resType = resType;
  }

  public File getResContent() {
    return resContent;
  }

  public void setResContent(File resContent) {
    this.resContent = resContent;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getResId() {
    return resId;
  }

  public void setResId(Integer resId) {
    this.resId = resId;
  }

  public String getSendParam() {
    return sendParam;
  }

  private void setSendParam(String sendParam) {
    this.sendParam = sendParam;
  }

  public String getResIds() {
    return resIds;
  }

  public void setResIds(String resIds) {
    this.resIds = resIds;
  }

  public boolean isDelete() {
    return delete;
  }

  public void setDelete(boolean delete) {
    this.delete = delete;
  }
}
