package com.market.ssvip.white.mode;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.licola.llogger.LLogger;
import com.market.ssvip.white.base.MyApplication;
import com.market.ssvip.white.component.interceptor.OnlySuccessInterceptor;
import com.market.ssvip.white.component.interceptor.StatusInterceptor;
import com.market.ssvip.white.mode.api.BasicsApi;
import com.market.ssvip.white.mode.bean.UserBean;
import com.market.ssvip.white.mode.manager.UserPersistManager;
import com.market.ssvip.white.mode.manager.UserPersistManagerImpl;
import com.market.ssvip.white.utils.CheckUtils;
import com.market.ssvip.white.utils.IPUtils;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author LiCola
 * @date 2018/10/30
 */
public class EnvManager {

    private static final EnvManager envManager = new EnvManager();

    private EnvManager() {

    }

    private BasicsApi basicsApi;

    private String envDeviceId;
    private String envUserName;
    private String envUserId;

    private UserPersistManager userPersistManager;

    public static EnvManager getEnvManager() {
        return envManager;
    }

    public void initEnvironment(Application application) {
        //注入依赖组件
        inject(application);

        initEnvDeviceId();
        initEnvUser();
    }

    private void initEnvDeviceId() {
        String latestDeviceId = userPersistManager.getLatestDeviceId();
        if (CheckUtils.isEmpty(latestDeviceId)) {
            LLogger.d("初始化设备ID");
            userPersistManager.setLatestDeviceId(UUID.randomUUID().toString());
        } else {
            LLogger.d("取出设备ID");
            this.envDeviceId = latestDeviceId;
        }
    }

    private void initEnvUser() {
        String latestUserId = userPersistManager.getLatestUserId();
        String latestUserName = userPersistManager.getLatestUserName();
        if (CheckUtils.isEmpty(latestUserId) || CheckUtils.isEmpty(latestUserName)) {
            LLogger.d("非登录用户");
        } else {
            LLogger.d("登录用户");
            this.envUserId = latestUserId;
            this.envUserName = latestUserName;
        }
    }

    public void updateEnvUser(UserBean userBean) {
        String userId = userBean.getUserId();
        String userName = userBean.getUserName();
        this.envUserId = userId;
        this.envUserName = userName;
        userPersistManager.setLatestUserId(userId);
        userPersistManager.setLatestUserName(userName);
    }

    private void inject(Application application) {

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .addHeader("Accept-Encoding", "gzip")
                        .addHeader("Accept", "application/json")
                        .addHeader("appId", "5")
                        .addHeader("appKey", "com.market.ttdk")
                        .addHeader("appMarket", "_vivo")
                        .addHeader("deviceId", userPersistManager.getLatestDeviceId())
                        .addHeader("platform", "Android")
                        .addHeader("version", getLocalVersionName(MyApplication.getApplication()))
                        .addHeader("Content-Type", "application/json;charset=utf-8")
//                        .addHeader("",isEmptyUser())
                        .method(originalRequest.method(), originalRequest.body());
                if (!isEmptyUser())
                    requestBuilder.addHeader("userId", getEnvUserId());
                if (IPUtils.getLocalIpAddress() != null)
                    requestBuilder.addHeader("remoteIp", IPUtils.getLocalIpAddress());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(new OnlySuccessInterceptor())
                .addInterceptor(new StatusInterceptor())
                .build();

        String BASE_URL = "http://api.lpkjb.cn";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.basicsApi = retrofit.create(BasicsApi.class);

        final String PREFERENCES_NAME = "black-sp";

        SharedPreferences sharedPreferences = application
                .getSharedPreferences(PREFERENCES_NAME,
                        Context.MODE_PRIVATE);

        UserPersistManagerImpl userPersistManager = new UserPersistManagerImpl();
        userPersistManager.init(sharedPreferences);
        this.userPersistManager = userPersistManager;
    }

    private static String localVersion = null;

    public static String getLocalVersionName(Context ctx) {

        if (localVersion != null) {
            return localVersion;
        }

        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public BasicsApi getBasicsApi() {
        return basicsApi;
    }

    public String getEnvUserId() {
        return envUserId;
    }

    public String getEnvUserName() {
        return envUserName;
    }

    public String getEnvDeviceId() {
        return envDeviceId;
    }

    public boolean isEmptyUser() {
        return CheckUtils.isEmpty(envUserId);
    }


    public void loginOut() {
        this.envUserName = null;
        this.envUserId = null;
        userPersistManager.setLatestUserId("");
        userPersistManager.setLatestUserName("");
    }
}
