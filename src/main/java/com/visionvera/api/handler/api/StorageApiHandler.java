package com.visionvera.api.handler.api;

import com.alibaba.fastjson.JSON;
import com.visionvera.api.handler.bean.StorageEntity;
import com.visionvera.api.handler.bean.StorageInfo;
import com.visionvera.api.handler.bean.res.ResRequest;
import com.visionvera.api.handler.bean.res.ResResult;
import com.visionvera.api.handler.constant.StorageApi;
import com.visionvera.api.handler.constant.StorageApi.PlatformType;
import com.visionvera.api.handler.constant.StorageApi.ResType;
import com.visionvera.api.handler.constant.StorageApi.UploadType;
import com.visionvera.api.handler.utils.HttpUtils;
import com.visionvera.api.handler.utils.IntegerUtil;
import com.visionvera.api.handler.utils.RefUtil;
import com.visionvera.api.handler.utils.ResponseCode;
import com.visionvera.api.handler.utils.ResultEntity;
import com.visionvera.api.handler.utils.StringUtil;
import java.io.File;

/**
 * 存储网关接口处理类
 *
 * @author EricShen
 */
public class StorageApiHandler {

  /**
   * 通用请求返回
   *
   * @param uri uri
   * @param storageEntity 参数
   * @return
   */
  private static ResultEntity requestCommonReturn(String uri, StorageEntity storageEntity) {
    String ip = storageEntity.getIp();
    Integer port = storageEntity.getPort();
    storageEntity.setIp(null);
    storageEntity.setPort(null);
    storageEntity.setSessionID(StorageApi.CMS_SESSION_ID);
    String url = StorageApi.PROTOCOL + ip + ":" + port + "/" + StorageApi.CMS_BASE_URL + uri;
    String resultString = HttpUtils.sendPostByMap(url, RefUtil.getProperty(storageEntity));
    boolean result = HttpUtils.checkoutResult(resultString);
    if (result) {
      StorageInfo responseInfo = JSON.parseObject(resultString, StorageInfo.class);
      if (responseInfo.getResult()) {
        return ResultEntity.success(responseInfo);
      }
      //响应错误
      return ResultEntity.error(ResponseCode.RESPONSE_ERROR, resultString);
    }
    //请求错误
    return ResultEntity.error(ResponseCode.REQUEST_ERROR, resultString);
  }

  /**
   * 取身份证信息
   *
   * guid 业务id( 会议id ：scheduleId) v2vid 终端号码
   */
  public static ResultEntity getCardInfo(StorageEntity storage) {
    try {
      if (StringUtil.isEmptyByArr(storage.getIp(), storage.getGuid(), storage.getV2vid())
          || IntegerUtil.isEmptyByArr(storage.getPort())) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      return requestCommonReturn(StorageApi.CMS_READ_CARD_URL, storage);
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 获取图片
   *
   * guid 业务id ( 会议id ：scheduleId) v2vid 终端号码 type  扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照  (支持
   * 1、3、4、5) materialsId  证件类型id 具体参考t_materials 材料表 materials_type 为3的数据(只在传5时候必填)
   */
  public static ResultEntity getPicture(StorageEntity storage) {
    try {
      if (StringUtil.isEmptyByArr(storage.getIp(), storage.getGuid(), storage.getV2vid())
          || IntegerUtil.isEmptyByArr(storage.getPort(), storage.getType())) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String uri;
      switch (storage.getType()) {
        //截图
        case StorageApi.VIDEO_SNAPSHOT:
          uri = StorageApi.CMS_READ_VIDEO_SNAPSHOTS_URL;
          break;
        //签名照
        case StorageApi.AUTOGRAPHED_PHOTO:
          uri = StorageApi.CMS_SIGNED_PHOTO_URL;
          break;
        //指纹照
        case StorageApi.FINGERPRINT_PHOTO:
          uri = StorageApi.CMS_SCAN_FINGERPRINT_URL;
          break;
        //证件照
        case StorageApi.CERTIFICATE_PHOTO:
          if (IntegerUtil.isEmpty(storage.getMaterialsId())) {
            //参数错误
            return ResultEntity.error(ResponseCode.PARAM_ERROR);
          }
          storage.setType(StorageApi.CERTIFICATE_PHOTO * 1000 + storage.getMaterialsId());
          uri = StorageApi.CMS_HIGH_SPEED_PHOTOGRAPHIC_URL;
          break;
        default:
          //参数错误
          return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      return requestCommonReturn(uri, storage);
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 删除图片
   *
   * ids 图片信息的id，多个以逗号隔开 type 扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照  (支持 1、3、4、5)
   */
  public static ResultEntity deletePicture(StorageEntity storage) {
    try {
      if (StringUtil.isEmptyByArr(storage.getIp(), storage.getIds()) || IntegerUtil
          .isEmptyByArr(storage.getPort(), storage.getType())) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String uri = "";
      switch (storage.getType()) {
        //截图
        case StorageApi.VIDEO_SNAPSHOT:
          uri = StorageApi.CMS_DELETE_VIDEO_SNAPSHOTS_URL;
          break;
        //签名照
        case StorageApi.AUTOGRAPHED_PHOTO:
          uri = StorageApi.CMS_DELETE_SIGNED_PHOTO_URL;
          break;
        //指纹照
        case StorageApi.FINGERPRINT_PHOTO:
          uri = StorageApi.CMS_DELETE_SCAN_FINGERPRINT_URL;
          break;
        //证件照
        case StorageApi.CERTIFICATE_PHOTO:
          uri = StorageApi.CMS_DELETE_HIGH_SPEED_PHOTOGRAPHIC_URL;
          break;
        default:
          //参数错误
          return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      return requestCommonReturn(uri, storage);
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 获取图片列表和录像地址
   *
   * guid 业务id ( 会议id ：scheduleId) type 视频类型，1正在录制的视频，默认值；2录制完毕的视频 ( 获取方式：businessInfo.getStatu())
   */
  public static ResultEntity getPhotosAndVideoPath(StorageEntity storage) {

    try {
      if (StringUtil.isEmptyByArr(storage.getIp(), storage.getGuid()) || IntegerUtil
          .isEmptyByArr(storage.getPort(), storage.getType())) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      return requestCommonReturn(StorageApi.CMS_READ_VIDEO_URL, storage);
    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  /**
   * 将图片、视频等素材上传到存储网关，存储网关只存储素材的基本信息， 如名称、大小、宽高、存储地址等信息； 该接口只支持单个文件上传
   *
   * sessionID 免登陆标识，由内容管理平台提供、 platform 0-16位；1-64位；2-流媒体、 resType 素材类型0文字1图片；2视频；3ppt;4word;5excel;6pdf、
   * resContent 具体资源内容、 type 是否新增(1-新增，2—更新)、 resId  需要更新的资源ID、 sendParam 上传的对象
   */
  public static ResultEntity uploadRes(ResRequest request) {
    try {
      if (IntegerUtil.isEmptyByArr(request.getPlatform(), request.getResType(), request.getType())
          || request.getResContent() == null || !request.getResContent().exists()) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }
      String ip = request.getIp();
      Integer port = request.getPort();
      if (StringUtil.isEmptyByArr(request.getSessionID())) {
        request.setSessionID(StorageApi.CMS_SESSION_ID);
      }
      String url = StorageApi.PROTOCOL + ip + ":" + port + "/" + StorageApi.RES_BASE_URL
          + StorageApi.RES_UPLOAD_URL + "?sessionID=" + request.getSessionID();
      String sendParam = JSON.toJSONString(request);
      String resultString = HttpUtils.sendFilePost(url, sendParam, request.getResContent());
      return getResResultEntity(resultString);

    } catch (Exception e) {
      e.printStackTrace();
      //异常
      return ResultEntity.error();
    }
  }

  private static ResultEntity getResResultEntity(String resultString) {
    if (StringUtil.isNotEmpty(resultString) && resultString.contains("errcode")) {
      ResResult result = JSON.parseObject(resultString, ResResult.class);
      if (result.getErrcode() == 0) {
        return ResultEntity.success(result);
      }
      //响应错误
      return ResultEntity.error(ResponseCode.RESPONSE_ERROR,
          new StorageInfo(false, "错误码:" + result.getErrcode() + "; 错误信息:" + result.getErrmsg()));
    }

    //请求错误
    return ResultEntity.error(ResponseCode.REQUEST_ERROR, resultString);
  }

  /**
   *
   */
  public static ResultEntity getOrDeleteRes(ResRequest request) {
    try {
      if (IntegerUtil.isEmptyByArr(request.getPlatform()) || StringUtil
          .isEmptyByArr(request.getResIds())) {
        //参数错误
        return ResultEntity.error(ResponseCode.PARAM_ERROR);
      }

      String ip = request.getIp();
      Integer port = request.getPort();
      if (StringUtil.isEmptyByArr(request.getSessionID())) {
        request.setSessionID(StorageApi.CMS_SESSION_ID);
      }
      String url = StorageApi.PROTOCOL + ip + ":" + port + "/" + StorageApi.RES_BASE_URL + (
          request.isDelete() ? StorageApi.RES_DELETE_URL : StorageApi.RES_GET_URL) + "?sessionID="
          + request.getSessionID() + "&platform=" + request.getPlatform() + "&resIds=" + request
          .getResIds();
      String resultString = HttpUtils.sendGet(url);
      return getResResultEntity(resultString);

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
    // ResultEntity<StorageInfo> resultEntity = function4("10.1.24.125", 8080);
    // System.out.println("请求结果：" + resultEntity);
    // if (resultEntity.getResult()) {
    //   System.out.println("请求成功");
    //   StorageInfo data = resultEntity.getData();
    //   System.out.println(data);
    // } else {
    //   System.out.println("请求失败");
    //   if (resultEntity.getData() != null) {
    //     System.out.println("失败细节:" + resultEntity.getData());
    //   }
    // }
    String ip = "172.20.102.75";
    int port = 8080;
    ResultEntity resultEntity = function5(ip, port);
    System.out.println("resultEntity = " + resultEntity);
    ResResult data = (ResResult) resultEntity.getData();
    String resId = data.getData().getResId();
    System.out.println("resId = " + resId);
    ResultEntity r = function6(ip, port, resId, false);
    ResResult resResult = (ResResult) r.getData();
    System.out.println("resGetResult.getErrmsg() = " + resResult.getErrmsg());
    System.out.println("resResult.getList() = " + resResult.getList());
  }

  /**
   * 获取图片
   *
   * guid 业务id(会议id ：scheduleId) v2vid 终端号码
   */
  private static ResultEntity<StorageInfo> function1(String ip, int port) {
    return getCardInfo(new StorageEntity(ip, port, "1", "20024"));
  }

  /**
   * 根据业务id获取身份证信息
   *
   * guid 业务id( 会议id ：scheduleId) v2vid 终端号码 type  扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照  (支持
   * 1、3、4、5) materialsId  证件类型id 具体参考t_materials 材料表 materials_type 为3的数据
   */
  private static ResultEntity<StorageInfo> function2(String ip, int port) {
    //  1 （52、53、54、55） 3（84）  5
    return getPicture(
        new StorageEntity(ip, port, "7e4ea17abca111e8b69eac1f6b659e94", "20024", 1, null));
  }

  /**
   * 删除图片
   *
   * ids 图片信息的id，多个以逗号隔开 type 扫描照类型 1:截图 2：身份证头像   3：签名照  4：指纹照 5:证件照  (支持 1、3、4、5)
   */
  private static ResultEntity<StorageInfo> function3(String ip, int port) {
    //  1 （52、53、54、55） 3（84）  5
    return deletePicture(new StorageEntity(ip, port, "84", 5));
  }

  /**
   * 获取图片列表和录像地址
   *
   * guid 业务id ( 会议id ：scheduleId) type 视频类型，1正在录制的视频，默认值；2录制完毕的视频 ( 获取方式：businessInfo.getStatu())
   */
  private static ResultEntity<StorageInfo> function4(String ip, int port) {
    //  1 （52、53、54、55） 3（84）  5
    return getPhotosAndVideoPath(
        new StorageEntity(ip, port, "a460d293bc9f11e8b69eac1f6b659e94", 2, null));
  }

  /**
   * 上传图片
   */
  private static ResultEntity function5(String ip, int port) {
    File file = new File("C:\\Users\\ahsbt\\Desktop\\test.png");

    ResRequest resUploadRequest = new ResRequest(ip, port, PlatformType.SIXTEEN_BIT.getValue(),
        ResType.IMG.getValue(), UploadType.ADD.getValue(), null, file);
    return uploadRes(resUploadRequest);
  }

  /**
   * 上传图片
   */
  private static ResultEntity function6(String ip, int port, String resIds, boolean isDelete) {
    ResRequest resRequest = new ResRequest(ip, port, PlatformType.SIXTEEN_BIT.getValue(), resIds,
        isDelete);
    return getOrDeleteRes(resRequest);
  }


}
