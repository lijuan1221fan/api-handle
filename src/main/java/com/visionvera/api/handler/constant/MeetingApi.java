package com.visionvera.api.handler.constant;


import java.util.Map;


/**
 * 会管api
 *
 * @author EricShen
 */
public class MeetingApi {

  /**
   * 获取url 错误
   */
  public static final String URL_ERROR = "urlError:";
  /**
   * 协议
   */
  public static final String PROTOCOL = "http://";
  /**
   * uri
   */
  public static final String URI = "/cmsweb/restful/";
  /**
   * 1.注册
   */
  public static final int REGISTER = 1;
  /**
   * 2.开会
   */
  public static final int START_MEETING = 2;
  /**
   * 3.停会
   */
  public static final int STOP_MEETING = 3;
  /**
   * 4.开启录制
   */
  public static final int START_RECORDING = 4;
  /**
   * 5.停止录制
   */
  public static final int STOP_RECORDING = 5;
  /**
   * 6.开启辅流
   */
  public static final int START_STREAM = 6;
  /**
   * 7.停止辅流
   */
  public static final int STOP_STREAM = 7;
  /**
   * 8.动态入会
   */
  public static final int DYNAMIC_ADD = 8;
  /**
   * 9.动态出会
   */
  public static final int DYNAMIC_DEL = 9;
  /**
   * 10.动态切音视频
   */
  public static final int DYNAMIC_SWITCHING = 10;
  /**
   * 11.会议状态
   */
  public static final int MEETING_STATUS = 11;
  /**
   * 12.同步终端
   */
  public static final int SYN_DEVICE = 12;


  /**
   * 获取注册url
   *
   * @param ip ip
   * @param port 端口
   */
  public static String getRegisterUrl(String ip, int port) {
    return getUrl(ip, port, REGISTER, null, null, null, null);
  }

  /**
   * 获取url,无 用户id、 会议标识
   *
   * @param ip ip
   * @param port url类型
   * @param type 端口
   * @param accessToken token
   * @param extendMap 扩展参数
   */
  public static String getUrl(String ip, int port, int type, String accessToken,
      Map<String, Object> extendMap) {
    return getUrl(ip, port, type, null, null, accessToken, extendMap);
  }

  /**
   * 获取url,无 会议标识、扩展参数
   *
   * @param ip ip
   * @param port 端口
   * @param type url类型
   * @param uuid 用户id
   * @param accessToken token
   */
  public static String getUrl(String ip, int port, int type, String uuid, String accessToken) {
    return getUrl(ip, port, type, uuid, null, accessToken, null);
  }

  /**
   * 获取url,无 扩展参数
   *
   * @param ip ip
   * @param port 端口
   * @param type url类型
   * @param scheduleId 会议唯一标识ID，开会成功后返回
   * @param uuid 用户id
   * @param accessToken token
   */
  public static String getUrl(String ip, int port, int type, String uuid, String scheduleId,
      String accessToken) {
    return getUrl(ip, port, type, uuid, scheduleId, accessToken, null);
  }

  /**
   * 最终获取url
   *
   * @param ip ip
   * @param port 端口
   * @param type url类型
   * @param uuid 用户id
   * @param scheduleId 会议唯一标识ID，开会成功后返回
   * @param accessToken token
   * @param extendMap 扩展参数
   */
  public static String getUrl(String ip, int port, int type, String uuid, String scheduleId,
      String accessToken, Map<String, Object> extendMap) {
    try {
      StringBuilder url = new StringBuilder();
      url.append(PROTOCOL).append(ip).append(":").append(port).append(URI);
      if (REGISTER == type) {
        //注册
        url.append("user/regist.json");
      } else if (START_MEETING == type) {
        //开启会议
        url.append("schedule/").append(uuid).append("/telMedicineStartMeeting.json?access_token=")
            .append(accessToken);
      } else if (STOP_MEETING == type) {
        //停止会议
        url.append("schedule/").append(uuid).append("/").append(scheduleId)
            .append("/stopMeeting.json?access_token=").append(accessToken);
      } else if (START_RECORDING == type) {
        //开启录制
        url.append("schedule/").append(uuid).append("/").append(scheduleId)
            .append("/rec/startVcr.json?access_token=").append(accessToken);
      } else if (STOP_RECORDING == type) {
        //停止录制
        url.append("schedule/").append(uuid).append("/").append(scheduleId)
            .append("/rec/stopVcr.json?access_token=").append(accessToken);
      } else if (START_STREAM == type) {
        Integer deviceId = (Integer) extendMap.get("deviceId");
        //开启辅流
        url.append("schedule/").append(uuid).append("/").append(scheduleId).append("/")
            .append(deviceId).append("/changeSecStreamEX_M_G.json?access_token=")
            .append(accessToken);
      } else if (STOP_STREAM == type) {
        //县级设备关闭辅流
        url.append("schedule/").append(uuid).append("/").append(scheduleId)
            .append("/stopSecStreamEX_M_G.json?access_token=").append(accessToken);
      } else if (DYNAMIC_ADD == type) {
        //动态加入参会方
        url.append("schedule/").append(uuid).append("/").append(scheduleId)
            .append("/dynAddDeviceEX_M_G.json?access_token=").append(accessToken);
      } else if (DYNAMIC_DEL == type) {
        Integer deviceId = (Integer) extendMap.get("deviceId");
        //动态移除参会方
        url.append("schedule/").append(uuid).append("/").append(scheduleId).append("/")
            .append(deviceId).append("/dynDelDeviceEX_M_G.json?access_token=").append(accessToken);
      } else if (DYNAMIC_SWITCHING == type) {
        //动态切音视频
        url.append("schedule/").append(uuid).append("/").append(scheduleId)
            .append("/changeDisPlayModeMed.json?access_token=").append(accessToken);
      } else if (MEETING_STATUS == type) {
        //查询会议状态
        url.append("schedule/").append(uuid).append("/").append(scheduleId)
            .append("/meetingInfo.json?access_token=").append(accessToken);
      } else if (SYN_DEVICE == type) {
        String groupId = (String) extendMap.get("groupId");
        //同步终端
        url.append("schedule/").append(groupId).append("/deviceList.json?access_token=")
            .append(accessToken).append("&pageSize=-1");
      }
      return url.toString();
    } catch (Exception e) {
      return URL_ERROR + e;
    }
  }

  /**
   * 测试
   *
   * @param args
   */
  public static void main(String[] args) {
    //http://192.168.10.125:8080/cmsweb/restful/user/regist.json
    //http://127.0.0.1:8080/cmsweb/restful/user/regist.json
    final String url = getUrl("127.0.0.1", 8080, 1, null, null, null, null);
    System.out.println(url);
    final String url2 = getRegisterUrl("127.0.0.1", 8080);
    System.out.println(url2);
  }


}
