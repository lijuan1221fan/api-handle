package com.visionvera.api.handler.bean;

import com.visionvera.api.handler.utils.ParamBaseEntity;


/**
 * 存储网关请求体
 *
 * @author EricShen
 */
public class StorageEntity extends ParamBaseEntity {

  /**
   * ip
   */
  private String ip;
  /**
   * 端口
   */
  private Integer port;
  /**
   * 免登陆令牌，由录播系统的开发人员提供
   */
  private String sessionID;
  /**
   * 业务id(会议id ：scheduleId)
   */
  private String guid;
  /**
   * 终端号码
   */
  private String v2vid;
  /**
   * 身份证信息的类型，0：所有信息（默认），1：身份证头像图片，2：身份证信息。
   */
  private Integer scanType;
  /**
   * 图片格式，默认为bmp
   */
  private String photoFormat;
  /**
   * 图片信息的id，多个以逗号隔开
   */
  private String ids;
  /**
   * 高拍仪采集的资料的类型，由远程申办中“父类型*1000+子类型（十进制）”组成。
   */
  private Integer type;
  /**
   * 证件类型id 具体参考t_materials 材料表 materials_type 为3的数据
   */
  private Integer materialsId;


  /**
   * 获取图片列表和录像地址
   *
   * guid 业务id ( 会议id ：scheduleId) type 视频类型，1正在录制的视频，默认值；2录制完毕的视频 ( 获取方式：businessInfo.getStatu())
   */
  public StorageEntity(String ip, Integer port, String guid, Integer type, Integer materialsId) {
    this.ip = ip;
    this.port = port;
    this.guid = guid;
    this.type = type;
    this.materialsId = materialsId;
  }

  /**
   * 删除图片
   *
   * ids 图片信息的id，多个以逗号隔开 type 扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照  (支持 1、3、4、5)
   */
  public StorageEntity(String ip, Integer port, String ids, Integer type) {
    this.ip = ip;
    this.port = port;
    this.ids = ids;
    this.type = type;
  }

  /**
   * 获取身份证信息
   *
   * guid 业务id(会议id ：scheduleId) v2vid 终端号码
   */
  public StorageEntity(String ip, Integer port, String guid, String v2vid) {
    this.ip = ip;
    this.port = port;
    this.guid = guid;
    this.v2vid = v2vid;
  }

  /**
   * 获取图片
   *
   * guid 业务id ( 会议id ：scheduleId) v2vid 终端号码 type  扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照  (支持
   * 1、3、4、5) materialsId  证件类型id 具体参考t_materials 材料表 materials_type 为3的数据(只在传5时候必填)
   */
  public StorageEntity(String ip, Integer port, String guid, String v2vid, Integer type,
      Integer materialsId) {
    this.ip = ip;
    this.port = port;
    this.guid = guid;
    this.v2vid = v2vid;
    this.type = type;
    this.materialsId = materialsId;
  }

  public Integer getMaterialsId() {
    return materialsId;
  }

  public void setMaterialsId(Integer materialsId) {
    this.materialsId = materialsId;
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

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getV2vid() {
    return v2vid;
  }

  public void setV2vid(String v2vid) {
    this.v2vid = v2vid;
  }

  public Integer getScanType() {
    return scanType;
  }

  public void setScanType(Integer scanType) {
    this.scanType = scanType;
  }

  public String getPhotoFormat() {
    return photoFormat;
  }

  public void setPhotoFormat(String photoFormat) {
    this.photoFormat = photoFormat;
  }

  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }
}
