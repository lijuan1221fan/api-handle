package com.visionvera.api.handler.bean;


/**
 * 会管请求体
 *
 * @author EricShen
 */
public class MeetingEntity {

  /**
   * ip
   */
  private String ip;
  /**
   * 端口
   */
  private Integer port;
  /**
   * 用户账号
   */
  private String loginName;
  /**
   * 用户密码
   */
  private String loginPwd;
  /**
   * 用户id
   */
  private String uuid;
  /**
   * token
   */
  private String accessToken;
  /**
   * 主席id  或终端id
   */
  private Integer masterId;
  /**
   * 主席ip
   */
  private String masterIp;
  /**
   * 参会id
   */
  private Integer slaveId;
  /**
   * 终端类型
   */
  private Integer deviceType;
  /**
   * 会议id
   */
  private String scheduleId;
  /**
   * 动态id
   */
  private Integer dynamicId;
  /**
   * 终端角色
   */
  private Integer deviceRole;
  /**
   * 分组id
   */
  private String groupId;

  public MeetingEntity() {

  }

  /**
   * 开会 封装
   *
   * @param ip
   * @param port
   * @param loginName
   * @param loginPwd
   * @param uuid
   * @param accessToken
   * @param masterId
   * @param masterIp
   * @param slaveId
   * @param deviceType
   */
  public MeetingEntity(String ip, int port, String loginName, String loginPwd, String uuid,
      String accessToken, int masterId, String masterIp, int slaveId, int deviceType) {
    this.ip = ip;
    this.port = port;
    this.loginName = loginName;
    this.loginPwd = loginPwd;
    this.uuid = uuid;
    this.accessToken = accessToken;
    this.masterId = masterId;
    this.masterIp = masterIp;
    this.slaveId = slaveId;
    this.deviceType = deviceType;
  }

  /**
   * 开启辅流 / 动态出会 封装 ip 账号 port 端口 uuid 用户id accessToken tocke scheduleId 会议id loginName 登陆账号
   * loginPwd 登陆密码 masterId  终端id
   */
  public MeetingEntity(String ip, Integer port, String loginName, String loginPwd, String uuid,
      String accessToken, String scheduleId, int masterId) {
    this.ip = ip;
    this.port = port;
    this.loginName = loginName;
    this.loginPwd = loginPwd;
    this.uuid = uuid;
    this.accessToken = accessToken;
    this.scheduleId = scheduleId;
    this.masterId = masterId;
  }

  /**
   * 停会 /停止辅流/停止录制/会议查询  封装 ip 账号 port 端口 uuid 用户id accessToken tocke scheduleId 会议id loginName 登陆账号
   * loginPwd 登陆密码
   */
  public MeetingEntity(String ip, Integer port, String loginName, String loginPwd, String uuid,
      String accessToken, String scheduleId) {
    this.ip = ip;
    this.port = port;
    this.loginName = loginName;
    this.loginPwd = loginPwd;
    this.uuid = uuid;
    this.accessToken = accessToken;
    this.scheduleId = scheduleId;
  }

  /**
   * 动态入会 & 动态切换音频 ip 账号 port 端口 uuid 用户id accessToken tocke scheduleId 会议id loginName 登陆账号 loginPwd
   * 登陆密码 masterId 主席id slaveId 参会方id dynamicId 动态入会id deviceRole 终端角色 deviceType 终端类型
   */
  public MeetingEntity(String ip, Integer port, String loginName, String loginPwd, String uuid,
      String accessToken, Integer masterId, Integer slaveId, Integer deviceType, String scheduleId,
      Integer dynamicId, Integer deviceRole) {
    this.ip = ip;
    this.port = port;
    this.loginName = loginName;
    this.loginPwd = loginPwd;
    this.uuid = uuid;
    this.accessToken = accessToken;
    this.masterId = masterId;
    this.slaveId = slaveId;
    this.deviceType = deviceType;
    this.scheduleId = scheduleId;
    this.dynamicId = dynamicId;
    this.deviceRole = deviceRole;
  }

  /**
   * 开始录制 ip 账号 port 端口 uuid 用户id accessToken tocke scheduleId 会议id loginName 登陆账号 loginPwd 登陆密码
   * masterId 主席id slaveId 参会id
   */
  public MeetingEntity(String ip, Integer port, String loginName, String loginPwd, String uuid,
      String accessToken, Integer masterId, Integer slaveId, String scheduleId) {
    this.ip = ip;
    this.port = port;
    this.loginName = loginName;
    this.loginPwd = loginPwd;
    this.uuid = uuid;
    this.accessToken = accessToken;
    this.masterId = masterId;
    this.slaveId = slaveId;
    this.scheduleId = scheduleId;
  }

  /**
   * 同步终端 ip 账号 port 端口 accessToken tocke groupId 分组id loginName 登陆账号 loginPwd 登陆密码
   */
  public MeetingEntity(String ip, Integer port, String loginName, String loginPwd,
      String accessToken, String groupId) {
    this.ip = ip;
    this.port = port;
    this.loginName = loginName;
    this.loginPwd = loginPwd;
    this.accessToken = accessToken;
    this.groupId = groupId;
  }


  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public Integer getDeviceRole() {
    return deviceRole;
  }

  public void setDeviceRole(Integer deviceRole) {
    this.deviceRole = deviceRole;
  }

  public Integer getDynamicId() {
    return dynamicId;
  }

  public void setDynamicId(Integer dynamicId) {
    this.dynamicId = dynamicId;
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

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getLoginPwd() {
    return loginPwd;
  }

  public void setLoginPwd(String loginPwd) {
    this.loginPwd = loginPwd;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Integer getMasterId() {
    return masterId;
  }

  public void setMasterId(Integer masterId) {
    this.masterId = masterId;
  }

  public String getMasterIp() {
    return masterIp;
  }

  public void setMasterIp(String masterIp) {
    this.masterIp = masterIp;
  }

  public Integer getSlaveId() {
    return slaveId;
  }

  public void setSlaveId(Integer slaveId) {
    this.slaveId = slaveId;
  }

  public Integer getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(Integer deviceType) {
    this.deviceType = deviceType;
  }

  public String getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(String scheduleId) {
    this.scheduleId = scheduleId;
  }

}
