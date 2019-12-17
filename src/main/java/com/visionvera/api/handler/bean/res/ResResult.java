package com.visionvera.api.handler.bean.res;

import com.visionvera.api.handler.utils.ResReturnBaseEntity;
import java.util.List;

/**
 * 存储网关资源操作返回体
 *
 * @author EricShen
 * @date 2018-12-25
 */
public class ResResult extends ResReturnBaseEntity {

  private ResInfoBean data;

  private List<ResInfoBean> list;

  public ResInfoBean getData() {
    return data;
  }

  public void setData(ResInfoBean data) {
    this.data = data;
  }

  public List<ResInfoBean> getList() {
    return list;
  }

  public void setList(List<ResInfoBean> list) {
    this.list = list;
  }
}
