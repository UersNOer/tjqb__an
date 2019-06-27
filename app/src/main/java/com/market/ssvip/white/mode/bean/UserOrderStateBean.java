package com.market.ssvip.white.mode.bean;

/**
 * @author LiCola
 * @date 2018/11/2
 */
public class UserOrderStateBean {


  /**
   * status	Integer	状态 -2 - 暂时订单 0 - 审核拒绝
   * status : 0
   */

  private int status;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
