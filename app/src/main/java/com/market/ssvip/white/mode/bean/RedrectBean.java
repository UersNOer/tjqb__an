package com.market.ssvip.white.mode.bean;

import java.util.List;

/**
 * Created by 赵凯 on 2019/7/11.
 */

public class RedrectBean {

    /**
     * company : null
     * name : 安薪花
     * amountRange : 2000-30000
     * rate : 0.03%
     * successRate : 7-60天
     * explains : 最快一分钟放款
     * accessUrl : http://api.lpkjb.cn/s2/access/url?m=aHR0cDovL2FueGgucXh5a2p6LmNvbS9tZXJjaGFudFByb2R1Y3QyMDE5MDUyMDE4MjEwMi9tZXJjaGFudFByb2R1Y3QyMDE5MDUyMDE4MjEwMl9lMjAwMDdhNzIzNmU0ZjVmOGZiYS9tZXJjaGFudFByb2R1Y3QyMDE5MDUyMDE4MjEwMl9lMjAwMDdhNzIzNmU0ZjVmOGZiYV8yLmh0bWwjQWNjZXNzI3Byb2R1Y3RJZD0xOSZpZD0xODEmdXJsVHlwZT1Qcm9kdWN0UHJveHk=&appMarket=_meizu&remoteIp=117.158.208.96&groupId=1&deviceId=2c23d7fb-250b-4ac5-871d-e33db0951679&userId=5725065962794971be1bdee44e2e3b4a&version=1.0.0&platform=Android&mode=redirect&appKey=com.market.ttdk
     * logo : https:market-01.oss-cn-zhangjiakou.aliyuncs.com/ProductLogo/fc69411c726a4597b4931aabd2d94619.png
     * lowerMoneyTime : 3
     * applyCondition : 身份证，银行卡，手机运营商，芝麻信用
     * applyQualifications : 全流程线上自动审核
     * productCode : null
     * explainList : ["最快一分钟放款"]
     * tagList : ["hot"]
     * productId : 19
     */

    private Object company;
    private String name;
    private String amountRange;
    private String rate;
    private String successRate;
    private String explains;
    private String accessUrl;
    private String logo;
    private int lowerMoneyTime;
    private String applyCondition;
    private String applyQualifications;
    private Object productCode;
    private int productId;
    private List<String> explainList;
    private List<String> tagList;

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmountRange() {
        return amountRange;
    }

    public void setAmountRange(String amountRange) {
        this.amountRange = amountRange;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getLowerMoneyTime() {
        return lowerMoneyTime;
    }

    public void setLowerMoneyTime(int lowerMoneyTime) {
        this.lowerMoneyTime = lowerMoneyTime;
    }

    public String getApplyCondition() {
        return applyCondition;
    }

    public void setApplyCondition(String applyCondition) {
        this.applyCondition = applyCondition;
    }

    public String getApplyQualifications() {
        return applyQualifications;
    }

    public void setApplyQualifications(String applyQualifications) {
        this.applyQualifications = applyQualifications;
    }

    public Object getProductCode() {
        return productCode;
    }

    public void setProductCode(Object productCode) {
        this.productCode = productCode;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public List<String> getExplainList() {
        return explainList;
    }

    public void setExplainList(List<String> explainList) {
        this.explainList = explainList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }
}
