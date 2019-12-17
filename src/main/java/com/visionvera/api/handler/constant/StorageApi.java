package com.visionvera.api.handler.constant;


/**
 * 存储网关api
 *
 * @author EricShen
 */
public interface StorageApi {

  /**
   * 协议
   */
  String PROTOCOL = "http://";
  /***
   *  cms_base_url 存储网关服务地址
   */
  String CMS_BASE_URL = "cms/api/remoteHandleApi/";
  /***
   *  存储网关上传服务地址
   */
  String RES_BASE_URL = "cms/api/resources/";

  /***
   *  cms_session_id 存储网关sessionID
   */
  String CMS_SESSION_ID = "268785df-bd29-49ef-b991-4ae38209c6b9";

  /***
   *  cms_read_card 存储网关读取身份证地址
   */
  String CMS_READ_CARD_URL = "scanIDCard.do";

  /***
   *  cms_read_card 存储网关读取视频地址及图片列表
   */
  String CMS_READ_VIDEO_URL = "getVideoInfo.do";

  /***
   *  cms_read_video_snapshots 存储网关读取录像截图
   */
  String CMS_READ_VIDEO_SNAPSHOTS_URL = "videoSnapshots.do";

  /***
   *  cms_read_video_snapshots 存储网关删除录像截图
   */
  String CMS_DELETE_VIDEO_SNAPSHOTS_URL = "deleteVideoInfo.do";

  /**
   * CMS_SIGNED_PHOTO_URL : 存储网关获取数字签名照片接口
   */
  String CMS_SIGNED_PHOTO_URL = "signedPhoto.do";

  /**
   * CMS_DELETE_SIGNED_PHOTO_URL : 存储网关删除签名照片
   */
  String CMS_DELETE_SIGNED_PHOTO_URL = "deleteSignedPhotoes.do";

  /**
   * CMS_SCAN_FINGERPRINT_URL : 采集指纹照片接口
   */
  String CMS_SCAN_FINGERPRINT_URL = "scanFingerprint.do";

  /**
   * CMS_DELETE_SCAN_FINGERPRINT_URL : 删除指纹照片接口
   */
  String CMS_DELETE_SCAN_FINGERPRINT_URL = "deleteFingerprintPhotos.do";

  /**
   * CMS_HIGH_SPEED_PHOTOGRAPHIC_URL : 高拍仪采集资料接口
   */
  String CMS_HIGH_SPEED_PHOTOGRAPHIC_URL = "highSpeedPhotographic.do";

  /**
   * CMS_DELETE_HIGH_SPEED_PHOTOGRAPHIC_URL : 删除高拍仪采集资料
   */
  String CMS_DELETE_HIGH_SPEED_PHOTOGRAPHIC_URL = "deleteHighSpeedPhotographic.do";

  /**
   * 素材上传
   */
  String RES_UPLOAD_URL = "uploadRes.do";
  /**
   * 根据素材ID查询信息
   */
  String RES_GET_URL = "getInfoList.do";
  /**
   * 删除素材
   */
  String RES_DELETE_URL = "delete.do";

  /**
   * VIDEO_SNAPSHOT : 截图
   */
  int VIDEO_SNAPSHOT = 1;

  /**
   * ID_CARD_HEAD : 身份证头像
   */
  int ID_CARD_HEAD = 2;

  /**
   * AUTOGRAPHED_PHOTO : 签名照
   */
  int AUTOGRAPHED_PHOTO = 3;

  /**
   * FINGERPRINT_PHOTO : 指纹照
   */
  int FINGERPRINT_PHOTO = 4;

  /**
   * CERTIFICATE_PHOTO :证件照
   */
  int CERTIFICATE_PHOTO = 5;


  /**
   * 素材类型0文字 1图片；2视频； 3ppt;4word;5excel;6pdf
   */
  public enum ResType {

    /**
     * 文字
     */
    TEXT(0),

    /**
     * 图片
     */
    IMG(1),

    /**
     * 视频
     */
    VIDEO(2),

    /**
     * PPT
     */
    PPT(3),

    /**
     * WORD
     */
    WORD(4),

    /**
     * EXCEL
     */
    EXCEL(5),

    /**
     * PDF
     */
    PDF(6);

    private int value;

    ResType(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

  }

  /**
   * 0-16位；1-64位；2-流媒体
   */
  public enum PlatformType {

    /**
     * 16位
     */
    SIXTEEN_BIT(0),

    /**
     * 64位
     */
    SIXTY_FOUR_BIT(1),

    /**
     * 流媒体
     */
    STREAMING_MEDIA(2);

    private int value;

    PlatformType(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

  }

  /**
   * 上传素材 是否新增(1-新增，2—更新)
   */
  public enum UploadType {

    /**
     * 新增
     */
    ADD(1),

    /**
     * 更新
     */
    UPDATE(2);

    private int value;

    UploadType(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }}


}
