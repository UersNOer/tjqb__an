package com.market.ssvip.white.mode.api;

import com.market.ssvip.white.mode.bean.BannerBean;
import com.market.ssvip.white.mode.bean.ProductItemBean;
import com.market.ssvip.white.mode.bean.RedrectBean;
import com.market.ssvip.white.mode.bean.UserBean;
import com.market.ssvip.white.mode.bean.UserOrderApplyBean;
import com.market.ssvip.white.mode.bean.UserOrderStateBean;
import com.market.ssvip.white.mode.bean.VersionBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author LiCola
 * @date 2018/10/30
 */
public interface BasicsApi {

    int CODE_TYPE_BASE = 0;
    int CODE_TYPE_REGISTER = 1;
    int CODE_TYPE_LOGIN = 2;

    //获得短信验证码
    @GET("/s1/sms/sendSms")
    Call<Void> operateSendSmsCode(@Query("deviceId") String deviceId, @Query("phone") String phone, @Query("type") int type);

    //验证码登陆
    @GET("/s1/clientUser/smsLogin/app")
    Call<UserBean> fetchUserByWithCode(@Query("deviceId") String deviceId, @Query("phone") String phone, @Query("code") String code, @Query("type") int type);

    @GET("/s1/action/save")
    Call<Void> operateActionClickRecord(@Query("deviceId") String deviceId, @Query("userId") String userId,
                                        @Query("action") String action);

    @GET("/s1/apps/version/model")
    Call<VersionBean> fetchVersion(@Query("deviceId") String deviceId);

    @GET("/s1/order/query")
    Call<UserOrderStateBean> fetchUserApplyOrder(@Query("deviceId") String deviceId, @Query("userId") String userId);

    String SIGN = "RgUfwcoJbWVPmT2dghPm4y6jRKF6i4";

    @GET("http://interface.vip-black.com/products/list")
    Call<List<ProductItemBean>> fetchProductList(@Query("deviceId") String deviceId, @Query("sign") String sign);

    @GET("/s1/order/submit")
    Call<UserOrderApplyBean> operateUserApply(@Query("deviceId") String deviceId, @Query("userId") String userId,
                                              @Query("amount") int amount, @Query("contacts") String contactsJson,
                                              @Query("idCard") String idCardJson, @Query("zhima") String zhimaJson);


    @GET("/s3/product/group/redirect")
    Call<ArrayList<RedrectBean>> getRedirect(@Query("deviceId") String deviceId, @Query("groupId") String groupId, @Query("appKey") String appkey, @Query("appMarket") String appMarket, @Query("userId") String userId);


    @GET("/backEnd/banner/bannerList")
    Call<ArrayList<BannerBean>> getBannerList(@Query("operation") String operation, @Query("pageNo") String paegNo, @Query("pageSize") String pageSize);
}
