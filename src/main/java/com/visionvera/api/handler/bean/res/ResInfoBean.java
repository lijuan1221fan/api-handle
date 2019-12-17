package com.visionvera.api.handler.bean.res;

/**
 * 资源返回基类
 *
 * @author EricShen
 * @date 2018-12-25
 */
public class ResInfoBean {

  private String resId;
  private int type;
  private int resType;
  private String resUrl;
  private long time;
  private Object remark;
  private String videoImg;
  private int deleteState;
  private String name;
  private int width;
  private int height;

  public String getResId() {
    return resId;
  }

  public void setResId(String resId) {
    this.resId = resId;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getResType() {
    return resType;
  }

  public void setResType(int resType) {
    this.resType = resType;
  }

  public String getResUrl() {
    return resUrl;
  }

  public void setResUrl(String resUrl) {
    this.resUrl = resUrl;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public Object getRemark() {
    return remark;
  }

  public void setRemark(Object remark) {
    this.remark = remark;
  }

  public String getVideoImg() {
    return videoImg;
  }

  public void setVideoImg(String videoImg) {
    this.videoImg = videoImg;
  }

  public int getDeleteState() {
    return deleteState;
  }

  public void setDeleteState(int deleteState) {
    this.deleteState = deleteState;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public String toString() {
    return "ResInfoBean{" + "resId='" + resId + '\'' + ", type=" + type + ", resType=" + resType
        + ", resUrl='" + resUrl + '\'' + ", time=" + time + ", remark=" + remark + ", videoImg='"
        + videoImg + '\'' + ", deleteState=" + deleteState + ", name='" + name + '\'' + ", width="
        + width + ", height=" + height + '}';
  }
}