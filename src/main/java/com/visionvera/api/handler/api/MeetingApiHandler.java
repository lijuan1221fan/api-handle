package com.visionvera.api.handler.api;

import com.alibaba.fastjson.JSON;
import com.visionvera.api.handler.bean.MeetingEntity;
import com.visionvera.api.handler.bean.MeetingInfo;
import com.visionvera.api.handler.constant.MeetingApi;
import com.visionvera.api.handler.utils.HttpUtils;
import com.visionvera.api.handler.utils.IntegerUtil;
import com.visionvera.api.handler.utils.RefUtil;
import com.visionvera.api.handler.utils.ResponseCode;
import com.visionvera.api.handler.utils.ResultEntity;
import com.visionvera.api.handler.utils.StringUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * 会管接口处理类
 *
 * @author EricShen
 */
public class MeetingApiHandler {

  /**
   * Get 请求
   */
  private static final int IS_GET = 1;

  /**
   * Post 请求
   */
  private static final int IS_POST = 2;

  /**
   * 请求通用返回(注册接口使用)
   *
   * @param url url
   * @param httpRequestParam 网络请求参数
   */
  private static ResultEntity requestCommonReturn(String url, Object httpRequestParam)
      throws Exception {
    return MeetingApiHandler.requestCommonReturn(url, httpRequestParam, null, null);
  }

  /**
   * 请求通用返回(注册接口使用)
   *
   * @param url url
   * @param httpRequestParam 网络请求参数
   */
  private static ResultEntity requestCommonReturn(String url, Object httpRequestParam,
      MeetingEntity methodParam, String methodName) throws Exception {
    return MeetingApiHandler
        .requestCommonReturn(url, httpRequestParam, methodParam, methodName, IS_POST);
  }

  /**
   * 请求通用返回（非注册接口）
   *
   * @param url url
   * @param httpRequestParam 网络请求参数
   * @param methodParam 方法参数
   * @param methodName 方法名称
   * @param method 1:get 2:post
   */
  private static ResultEntity requestCommonReturn(String url, Object httpRequestParam,
      MeetingEntity methodParam, String methodName, int method) throws Exception {
    String resultString = "";
    if (method == IS_POST) {
      resultString = HttpUtils.sendPostByJson(url, httpRequestParam);
    } else {
      resultString = HttpUtils.sendGet(url);
    }
    boolean result = HttpUtils.checkoutResult(resultString);
    if (result) {
      MeetingInfo meetingInfo = JSON.parseObject(resultString, MeetingInfo.class);
      if (meetingInfo.getErrcode() == 0) {
        if (methodParam != null) {
          meetingInfo.setAccess_token(methodParam.getAccessToken());
        }
        //成功
        return ResultEntity.success(meetingInfo);
      } else if (meetingInfo.getErrcode() == 2 && methodParam != null && StringUtil
          .isNotEmpty(methodName)) {
        //从新注册
        ResultEntity<MeetingInfo> resultEntity = MeetingApiHandler
            .doRegister(methodParam.getIp(), methodParam.getPort(), methodParam.getLoginName(),
                methodParam.getLoginPwd());
        if (resultEntity.getResult()) {
          MeetingInfo registerInfo = resultEntity.getData();
          String accessToken = registerInfo.getAccess_token();
          methodParam.setAccessToken(accessToken);
          System.err.println("重新注册成功,将执行方法：" + methodName);
          return (ResultEntity) RefUtil
              .executeStatic(MeetingApiHandler.class, methodName, methodParam);
        } else {
          return resultEntity;
        }
      } else {
        //响应错误
        return ResultEntity.error(ResponseCode.RESPONSE_ERROR, resultString);
      }
    } else {
      //请求错误
      return ResultEntity.error(ResponseCode.REQUEST_ERROR, resultString);
    }
  }

  /**
   * 注册
   *
   * @param ip ip
   * @param port 端口
   * @param loginName 用户账号
   * @param loginPwd 密码
   * @return ResultEntity
   */
  public static ResultEntity doRegister(String ip, int port, String loginName, String loginPwd) {
    try {
      if (StringUtil.isEmpty(ip) || StringUtil.isEmpty(loginName) || StringUtil.isEmpty(loginPwd)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String registerUrl = MeetingApi.getRegisterUrl(ip, port);
      Map<String, Object> paramMap = new HashMap<String, Object>(2);
      paramMap.put("loginName", loginName);
      paramMap.put("loginPwd", loginPwd);
      return MeetingApiHandler.requestCommonReturn(registerUrl, paramMap);
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 开会 ip ip port 端口 uuid 用户id accessToken token masterId 主席id masterIp 主席ip slaveId 参会id
   * deviceType 终端类型 loginName 用户账号 loginPwd 用户密码
   */
  public static ResultEntity startMeeting(MeetingEntity meetingEntity) {
    try {
      String ip = meetingEntity.getIp();
      String uuid = meetingEntity.getUuid();
      String accessToken = meetingEntity.getAccessToken();
      String masterIp = meetingEntity.getMasterIp();
      Integer masterId = meetingEntity.getMasterId();
      Integer port = meetingEntity.getPort();
      Integer deviceType = meetingEntity.getDeviceType();
      Integer slaveId = meetingEntity.getSlaveId();
      String loginName = meetingEntity.getLoginName();
      String loginPwd = meetingEntity.getLoginPwd();

      if (StringUtil.isEmptyByArr(ip, uuid, accessToken, masterIp, loginName, loginPwd)
          || IntegerUtil.isEmptyByArr(masterId, port, deviceType, slaveId)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String url = MeetingApi.getUrl(ip, port, MeetingApi.START_MEETING, uuid, accessToken);
      Map<String, Object> paramMap = new HashMap<String, Object>(9);
      //会议名称
      paramMap.put("name", StringUtil.getUUID());
      //参会终端ID（多个值用,分割字符串） 格式：1(号码),2(类型),3(角色);2(号码),2(类型),2(角色);  类型 2是极光 1是启明 角色统统为0
      String devices = masterId + "," + deviceType + ",0;" + slaveId + "," + deviceType + ",0";
      paramMap.put("devices", devices);
      //主席ip
      paramMap.put("ip", masterIp);
      //会议模式（0：大会模式。4：会商模式。默认为0）
      paramMap.put("meetMode", 4);
      //标识，默认0。0 不录制 1 录制。为1时，video与audio不能为空
      paramMap.put("startVCR", 1);
      //视频录制列表（参数格式：”1(号码),2(通道);2(号码),2(通道);…”）
      String video = masterId + ",0;" + slaveId + ",0";
      paramMap.put("video", video);
      //音频录制列表 （参数格式：”1(号码),2(通道);2(号码),2(通道);…”）
      String audio = masterId + ",0;" + slaveId + ",0";
      paramMap.put("audio", audio);
      //分屏模式 1：一分屏 21：大小屏 22：左右分屏 31：三分屏41：四分屏等分42：四分屏上一下三43：四分屏左一右三（默认为1 一分屏）
      paramMap.put("disPlayMod", 22);
      //会议设备类型（区分申办业务）1极光 2启明 默认为1 极光
      paramMap.put("versionMode", 1);
      return MeetingApiHandler
          .requestCommonReturn(url, paramMap, meetingEntity, RefUtil.getMethodName());
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 停会 ip 账号 port 端口 uuid 用户id accessToken tocke scheduleId 会议id loginName 登陆账号 loginPwd 登陆密码
   */
  public static ResultEntity stopMeeting(MeetingEntity meetingEntity) {
    try {
      String ip = meetingEntity.getIp();
      String uuid = meetingEntity.getUuid();
      String accessToken = meetingEntity.getAccessToken();
      Integer port = meetingEntity.getPort();
      String loginName = meetingEntity.getLoginName();
      String loginPwd = meetingEntity.getLoginPwd();
      String scheduleId = meetingEntity.getScheduleId();
      if (StringUtil.isEmptyByArr(ip, uuid, accessToken, scheduleId, loginName, loginPwd)
          || IntegerUtil.isEmptyByArr(port)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String url = MeetingApi
          .getUrl(ip, port, MeetingApi.STOP_MEETING, uuid, scheduleId, accessToken);
      return MeetingApiHandler
          .requestCommonReturn(url, null, meetingEntity, RefUtil.getMethodName());
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 开启辅流 ip 账号 port 端口 uuid 用户id accessToken tocke scheduleId 会议id loginName 登陆账号 loginPwd 登陆密码
   * masterId 主席id
   */
  public static ResultEntity startStream(MeetingEntity meetingEntity) {
    try {
      String ip = meetingEntity.getIp();
      String uuid = meetingEntity.getUuid();
      String accessToken = meetingEntity.getAccessToken();
      Integer port = meetingEntity.getPort();
      String loginName = meetingEntity.getLoginName();
      String loginPwd = meetingEntity.getLoginPwd();
      String scheduleId = meetingEntity.getScheduleId();
      Integer masterId = meetingEntity.getMasterId();
      if (StringUtil.isEmptyByArr(ip, uuid, accessToken, scheduleId, loginName, loginPwd)
          || IntegerUtil.isEmptyByArr(port, masterId)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      Map<String, Object> extendMap = new HashMap<String, Object>(1);
      //设备ID
      extendMap.put("deviceId", masterId);
      String url = MeetingApi
          .getUrl(ip, port, MeetingApi.START_STREAM, uuid, scheduleId, accessToken, extendMap);
      return MeetingApiHandler
          .requestCommonReturn(url, null, meetingEntity, RefUtil.getMethodName());
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 停止辅流 ip 账号 port 端口 uuid 用户id accessToken token scheduleId 会议id loginName 登陆账号 loginPwd 登陆密码
   */
  public static ResultEntity stopStream(MeetingEntity meetingEntity) {
    try {
      String ip = meetingEntity.getIp();
      String uuid = meetingEntity.getUuid();
      String accessToken = meetingEntity.getAccessToken();
      Integer port = meetingEntity.getPort();
      String loginName = meetingEntity.getLoginName();
      String loginPwd = meetingEntity.getLoginPwd();
      String scheduleId = meetingEntity.getScheduleId();
      if (StringUtil.isEmptyByArr(ip, uuid, accessToken, scheduleId, loginName, loginPwd)
          || IntegerUtil.isEmptyByArr(port)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String url = MeetingApi
          .getUrl(ip, port, MeetingApi.STOP_STREAM, uuid, scheduleId, accessToken);
      return MeetingApiHandler
          .requestCommonReturn(url, null, meetingEntity, RefUtil.getMethodName());
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 动态入会 & 动态切换音频 ip 账号 port 端口 uuid 用户id accessToken tocke scheduleId 会议id loginName 登陆账号 loginPwd
   * 登陆密码 masterId 主席id slaveId 参会方id dynamicId 动态入会id deviceRole 终端角色 deviceType 终端类型
   */
  public static ResultEntity dynamicAdd(MeetingEntity meetingEntity) {
    try {
      String ip = meetingEntity.getIp();
      Integer port = meetingEntity.getPort();

      String uuid = meetingEntity.getUuid();
      String accessToken = meetingEntity.getAccessToken();

      String loginName = meetingEntity.getLoginName();
      String loginPwd = meetingEntity.getLoginPwd();

      String scheduleId = meetingEntity.getScheduleId();

      Integer masterId = meetingEntity.getMasterId();
      Integer slaveId = meetingEntity.getSlaveId();
      Integer dynamicId = meetingEntity.getDynamicId();

      Integer deviceRole = meetingEntity.getDeviceRole();
      Integer deviceType = meetingEntity.getDeviceType();

      if (StringUtil.isEmptyByArr(ip, uuid, accessToken, scheduleId, loginName, loginPwd)
          || IntegerUtil.isEmptyByArr(port, masterId, slaveId, dynamicId, deviceRole, deviceType)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String url = MeetingApi
          .getUrl(ip, port, MeetingApi.DYNAMIC_ADD, uuid, scheduleId, accessToken);
      Map<String, Object> paramsMap = new HashMap<String, Object>();
      //动态入会
      String devices = dynamicId + "," + deviceType + "," + deviceRole;
      paramsMap.put("devices", devices);
      ResultEntity<MeetingInfo> resultEntity = MeetingApiHandler
          .requestCommonReturn(url, paramsMap, meetingEntity, RefUtil.getMethodName());
      if (!resultEntity.getResult()) {
        return resultEntity;
      }
      //动态切音视频
      String videoUrl = MeetingApi.getUrl(ip, port, MeetingApi.DYNAMIC_SWITCHING, uuid, scheduleId,
          resultEntity.getData().getAccess_token());
      Integer[] devNumberArray = {masterId, slaveId, dynamicId};
      for (int i = 0; i < devNumberArray.length; i++) {
        Integer devNumber = devNumberArray[i];
        paramsMap.clear();
        paramsMap.put("deviceId", devNumber);
        String video = masterId + ",1;" + masterId + ",2;" + slaveId + ",2;" + dynamicId + ",2";
        paramsMap.put("video", video);
        String audio = "";
        switch (i) {
          case 0: {
            audio = slaveId + ",0;" + dynamicId + ",0";
            break;
          }
          case 1: {
            audio = masterId + ",0;" + dynamicId + ",0";
            break;
          }
          case 2: {
            audio = masterId + ",0;" + slaveId + ",0";
            break;
          }
          default:
            break;
        }
        paramsMap.put("audio", audio);
        paramsMap.put("screenNum", 5);
        paramsMap.put("audioNum", 2);
        ResultEntity videoResult = MeetingApiHandler.requestCommonReturn(videoUrl, paramsMap);
        if (!videoResult.getResult()) {
          return videoResult;
        }
      }
      return resultEntity;
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 动态出会 ip 账号 port 端口 uuid 用户id accessToken tocke scheduleId 会议id loginName 登陆账号 loginPwd 登陆密码
   * masterId 主席id
   */
  public static ResultEntity dynamicDel(MeetingEntity meetingEntity) {
    try {
      String ip = meetingEntity.getIp();
      String uuid = meetingEntity.getUuid();
      String accessToken = meetingEntity.getAccessToken();
      Integer port = meetingEntity.getPort();
      String loginName = meetingEntity.getLoginName();
      String loginPwd = meetingEntity.getLoginPwd();
      String scheduleId = meetingEntity.getScheduleId();
      Integer masterId = meetingEntity.getMasterId();
      if (StringUtil.isEmptyByArr(ip, uuid, accessToken, scheduleId, loginName, loginPwd)
          || IntegerUtil.isEmptyByArr(port, masterId)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      Map<String, Object> extendMap = new HashMap<String, Object>(1);
      //设备ID
      extendMap.put("deviceId", masterId);
      String url = MeetingApi
          .getUrl(ip, port, MeetingApi.DYNAMIC_DEL, uuid, scheduleId, accessToken, extendMap);
      return MeetingApiHandler
          .requestCommonReturn(url, null, meetingEntity, RefUtil.getMethodName());
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 开始录制 ip 账号 port 端口 uuid 用户id accessToken tocke scheduleId 会议id loginName 登陆账号 loginPwd 登陆密码
   * masterId 主席id slaveId 参会id
   */
  public static ResultEntity startRecording(MeetingEntity meetingEntity) {
    try {
      String ip = meetingEntity.getIp();
      String uuid = meetingEntity.getUuid();
      String accessToken = meetingEntity.getAccessToken();
      Integer port = meetingEntity.getPort();
      String loginName = meetingEntity.getLoginName();
      String loginPwd = meetingEntity.getLoginPwd();
      String scheduleId = meetingEntity.getScheduleId();
      Integer masterId = meetingEntity.getMasterId();
      Integer slaveId = meetingEntity.getSlaveId();
      if (StringUtil.isEmptyByArr(ip, uuid, accessToken, scheduleId, loginName, loginPwd)
          || IntegerUtil.isEmptyByArr(port, masterId, slaveId)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String url = MeetingApi
          .getUrl(ip, port, MeetingApi.START_RECORDING, uuid, scheduleId, accessToken);
      Map<String, Object> paramsMap = new HashMap<>(3);
      //视频录制列表（参数格式：”1(号码),2(通道);2(号码),2(通道);…”）
      String video = masterId + ",0;" + slaveId + ",0";
      paramsMap.put("video", video);
      //音频录制列表 （参数格式：”1(号码),2(通道);2(号码),2(通道);…”）
      String audio = masterId + ",0;" + slaveId + ",0";
      paramsMap.put("audio", audio);
      //分屏模式 1：一分屏 21：大小屏 22：左右分屏 31：三分屏41：四分屏等分42：四分屏上一下三43：四分屏左一右三（默认为1 一分屏）
      paramsMap.put("disPlayMod", 22);
      return MeetingApiHandler
          .requestCommonReturn(url, paramsMap, meetingEntity, RefUtil.getMethodName());
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 停止录制 ip 账号 port 端口 uuid 用户id accessToken tocke scheduleId 会议id loginName 登陆账号 loginPwd 登陆密码
   */
  public static ResultEntity stopRecording(MeetingEntity meetingEntity) {
    try {
      String ip = meetingEntity.getIp();
      String uuid = meetingEntity.getUuid();
      String accessToken = meetingEntity.getAccessToken();
      Integer port = meetingEntity.getPort();
      String loginName = meetingEntity.getLoginName();
      String loginPwd = meetingEntity.getLoginPwd();
      String scheduleId = meetingEntity.getScheduleId();
      if (StringUtil.isEmptyByArr(ip, uuid, accessToken, scheduleId, loginName, loginPwd)
          || IntegerUtil.isEmptyByArr(port)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String url = MeetingApi
          .getUrl(ip, port, MeetingApi.STOP_RECORDING, uuid, scheduleId, accessToken);
      return MeetingApiHandler
          .requestCommonReturn(url, null, meetingEntity, RefUtil.getMethodName());
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 同步终端 ip 账号 port 端口 accessToken tocke groupId 分组id loginName 登陆账号 loginPwd 登陆密码
   */
  public static ResultEntity synDevice(MeetingEntity meetingEntity) {
    try {
      String ip = meetingEntity.getIp();
      String accessToken = meetingEntity.getAccessToken();
      Integer port = meetingEntity.getPort();
      String loginName = meetingEntity.getLoginName();
      String loginPwd = meetingEntity.getLoginPwd();
      String groupId = meetingEntity.getGroupId();
      if (StringUtil.isEmptyByArr(ip, accessToken, groupId, loginName, loginPwd) || IntegerUtil
          .isEmptyByArr(port)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      Map<String, Object> extendMap = new HashMap<String, Object>(1);
      //分组id
      extendMap.put("groupId", groupId);
      String url = MeetingApi.getUrl(ip, port, MeetingApi.SYN_DEVICE, accessToken, extendMap);
      return MeetingApiHandler
          .requestCommonReturn(url, null, meetingEntity, RefUtil.getMethodName(), IS_GET);
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }


  /**
   * 会议查询 ip 账号 port 端口 loginName 登陆账号 loginPwd 登陆密码 accessToken tocke uuid 用户id scheduleId 会议id
   */
  public static ResultEntity checkMeetingStatus(MeetingEntity meetingEntity) {
    try {
      String ip = meetingEntity.getIp();
      Integer port = meetingEntity.getPort();
      String loginName = meetingEntity.getLoginName();
      String loginPwd = meetingEntity.getLoginPwd();
      String uuid = meetingEntity.getUuid();
      String accessToken = meetingEntity.getAccessToken();
      String scheduleId = meetingEntity.getScheduleId();
      if (StringUtil.isEmptyByArr(ip, accessToken, scheduleId, uuid, loginName, loginPwd)
          || IntegerUtil.isEmptyByArr(port)) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String url = MeetingApi
          .getUrl(ip, port, MeetingApi.MEETING_STATUS, uuid, scheduleId, accessToken);
      return MeetingApiHandler
          .requestCommonReturn(url, null, meetingEntity, RefUtil.getMethodName(), IS_GET);
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }


  /**
   * 测试
   */
  public static void main(String[] args) {
    // ResultEntity<MeetingInfo> resultEntity = fun3("10.1.24.149", 8080, "jiayao",
    //     "e10adc3949ba59abbe56e057f20f883e");
    // System.out.println("请求结果：" + resultEntity);
    // if (resultEntity.getResult()) {
    //   System.out.println("请求成功");
    //   MeetingInfo data = resultEntity.getData();
    //   System.out.println(data);
    // } else {
    //   System.out.println("请求失败");
    //   if (resultEntity.getData() != null) {
    //     System.out.println("失败细节:" + resultEntity.getData());
    //   }
    // }
    String ip = "172.20.103.253";
    int port = 8080;
    String name = "ycsb";
    String pwd = "e10adc3949ba59abbe56e057f20f883e";
    ResultEntity<MeetingInfo> meetingInfoResultEntity = fun1(ip, port, name, pwd);
    System.out.println("meetingInfoResultEntity = " + meetingInfoResultEntity);
  }

  /**
   * 注册
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun1(String ip, int port, String name, String pwd) {
    return MeetingApiHandler.doRegister(ip, port, name, pwd);
  }

  /**
   * 开会
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun2(String ip, int port, String name, String pwd) {
    MeetingEntity meetingEntity = new MeetingEntity(ip, port, name, pwd,
        "5a3df085a42611e8b90aac1f6b659e94", "c557fefdbbb111e8b69eac1f6b659e94", 526, "4.27.169.40",
        20024, 2);
    ResultEntity<MeetingInfo> resultEntity2 = MeetingApiHandler.startMeeting(meetingEntity);
    return resultEntity2;
  }

  /**
   * 停会
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun3(String ip, int port, String name, String pwd) {
    MeetingEntity stop = new MeetingEntity(ip, port, name, pwd, "5a3df085a42611e8b90aac1f6b659e94",
        "165e3746bc9f11e8b69eac1f6b659e94", "7e4ea17abca111e8b69eac1f6b659e94");
    ResultEntity<MeetingInfo> resultEntity3 = MeetingApiHandler.stopMeeting(stop);
    return resultEntity3;
  }

  /**
   * 开启辅流
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun4(String ip, int port, String name, String pwd) {
    MeetingEntity startStream = new MeetingEntity(ip, port, name, pwd,
        "5a3df085a42611e8b90aac1f6b659e94", "c557fefdbbb111e8b69eac1f6b659e94",
        "8beebb8dbbb411e8b69eac1f6b659e94", 20024);
    ResultEntity<MeetingInfo> resultEntity3 = MeetingApiHandler.startStream(startStream);
    return resultEntity3;
  }

  /**
   * 停止辅流
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun5(String ip, int port, String name, String pwd) {
    MeetingEntity startStream = new MeetingEntity(ip, port, name, pwd,
        "5a3df085a42611e8b90aac1f6b659e94", "c557fefdbbb111e8b69eac1f6b659e94",
        "8beebb8dbbb411e8b69eac1f6b659e94");
    ResultEntity<MeetingInfo> resultEntity3 = MeetingApiHandler.stopStream(startStream);
    return resultEntity3;
  }

  /**
   * 动态入会 & 动态切音视频
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun6(String ip, int port, String name, String pwd) {
    MeetingEntity dynamicAdd = new MeetingEntity(ip, port, name, pwd,
        "5a3df085a42611e8b90aac1f6b659e94", "c557fefdbbb111e8b69eac1f6b659e94", 526, 20024, 2,
        "8beebb8dbbb411e8b69eac1f6b659e94", 20098, 0);
    ResultEntity<MeetingInfo> resultEntity3 = MeetingApiHandler.dynamicAdd(dynamicAdd);
    return resultEntity3;
  }

  /**
   * 动态出会
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun7(String ip, int port, String name, String pwd) {
    MeetingEntity dynamicDel = new MeetingEntity(ip, port, name, pwd,
        "5a3df085a42611e8b90aac1f6b659e94", "c557fefdbbb111e8b69eac1f6b659e94",
        "8beebb8dbbb411e8b69eac1f6b659e94", 20098);
    ResultEntity<MeetingInfo> resultEntity3 = MeetingApiHandler.dynamicDel(dynamicDel);
    return resultEntity3;
  }

  /**
   * 开始录制
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun8(String ip, int port, String name, String pwd) {
    MeetingEntity startRecording = new MeetingEntity(ip, port, name, pwd,
        "5a3df085a42611e8b90aac1f6b659e94", "c557fefdbbb111e8b69eac1f6b659e94", 526, 20024,
        "8beebb8dbbb411e8b69eac1f6b659e94");
    ResultEntity<MeetingInfo> resultEntity3 = MeetingApiHandler.startRecording(startRecording);
    return resultEntity3;
  }


  /**
   * 停止录制
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun9(String ip, int port, String name, String pwd) {
    MeetingEntity stopRecording = new MeetingEntity(ip, port, name, pwd,
        "5a3df085a42611e8b90aac1f6b659e94", "c557fefdbbb111e8b69eac1f6b659e94",
        "8beebb8dbbb411e8b69eac1f6b659e94");
    ResultEntity<MeetingInfo> resultEntity3 = MeetingApiHandler.stopRecording(stopRecording);
    return resultEntity3;
  }


  /**
   * 同步终端
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun10(String ip, int port, String name, String pwd) {
    MeetingEntity synDevice = new MeetingEntity(ip, port, name, pwd,
        "c557fefdbbb111e8b69eac1f6b659e94", "5a404757a42611e8b90aac1f6b659e94");
    ResultEntity<MeetingInfo> resultEntity3 = MeetingApiHandler.synDevice(synDevice);
    return resultEntity3;
  }


  /**
   * 会议查询
   *
   * @return
   */
  static ResultEntity<MeetingInfo> fun11(String ip, int port, String name, String pwd) {
    MeetingEntity checkMeetingStatus = new MeetingEntity(ip, port, name, pwd,
        "5a3df085a42611e8b90aac1f6b659e94", "74a6d424bbea11e8b69eac1f6b659e94",
        "8beebb8dbbb411e8b69eac1f6b659e94");
    ResultEntity<MeetingInfo> resultEntity3 = MeetingApiHandler
        .checkMeetingStatus(checkMeetingStatus);
    return resultEntity3;
  }
}


