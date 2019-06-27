package com.market.ssvip.white.mode.bean;

/**
 * @author LiCola
 * @date 2018/11/2
 */
public class VersionBean {


  /**
   * model	Integer	应用模式 1 申请模式 2 贷超模式
   * status	Integer	应用状态 0 不展示申请模式贷超 1 展示申请模式贷超
   * model : 1
   * status : 1
   */

  private int model;
  private int status;

  public int getModel() {
    return model;
  }

  public void setModel(int model) {
    this.model = model;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
