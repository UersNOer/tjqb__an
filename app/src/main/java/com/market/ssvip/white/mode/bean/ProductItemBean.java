package com.market.ssvip.white.mode.bean;

/**
 * @author LiCola
 * @date 2018/11/6
 */
public class ProductItemBean {

  /**
   * logo : https://img1.ultimavip.cn/FjC30DzqhKdu03xoNf3rmrEqlEak
   * name : 好借呗
   * sign : 7Bd3En8iOfSc26P9xNplPX2eK6Ehx4
   * status : 2
   * url : http://interface.vip-black.com/product/redirectUrlBySign?sign=7Bd3En8iOfSc26P9xNplPX2eK6Ehx4
   */

  private String logo;
  private String name;
  private String sign;
  private int status;
  private String url;

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
