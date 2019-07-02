package com.market.ssvip.white.presenter.web;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;

import com.licola.llogger.LLogger;
import com.loveplusplus.update.DownloadService;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.AppBaseActivity;
import com.market.ssvip.white.mode.EnvManager;
import com.market.ssvip.white.mode.api.BasicsApi;
import com.market.ssvip.white.utils.CheckUtils;
import com.market.ssvip.white.utils.NotificationSetUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author LiCola
 * @date 2018/11/7
 */
public class WebAty extends AppBaseActivity {

    private static final String KEY_URL = "key_url";

    @BindView(R.id.iv_toolbar_left)
    ImageView ivToolbarLeft;
    @BindView(R.id.layout_toolbar_left)
    FrameLayout layoutToolbarLeft;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar_center)
    LinearLayout toolbarCenter;
    @BindView(R.id.iv_toolbar_right)
    ImageView ivToolbarRight;
    @BindView(R.id.layout_toolbar_right)
    FrameLayout layoutToolbarRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_toolbar_group)
    FrameLayout layoutToolbarGroup;
    @BindView(R.id.layout_toolbar_root)
    LinearLayout layoutToolbarRoot;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    public static Intent makeIntent(Context context, String url) {

        Intent intent = new Intent(context, WebAty.class);
        intent.putExtra(KEY_URL, url + "&key=com.market.ttdk&user_id=" + EnvManager.getEnvManager().getEnvUserId() + "&market=_meizu");
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.module_aty_web;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebAty.this.deleteDatabase("webview.db");
        WebAty.this.deleteDatabase("webviewCache.db");
        webView.clearHistory();
        webView.clearFormData();
        getCacheDir().delete();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        //判断是否需要开启通知栏功能
            closeAndroidPDialog();
        }
        initSettings(webView);
        initClient(webView);
        Intent intent = getIntent();
        onLoadUrl(intent);
    }

    private void onLoadUrl(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String url = extras.getString(KEY_URL);
            webView.loadUrl(url);
        }
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private boolean flag = true;

    private void initClient(final WebView webView) {

        final EnvManager envManager = EnvManager.getEnvManager();
        final BasicsApi basicsApi = envManager.getBasicsApi();

        final Callback<Void> placeCallback = new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        };

        WebViewClient client = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("-------------", url);
                webView.loadUrl(url);
                basicsApi.operateActionClickRecord(envManager.getEnvDeviceId(), envManager.getEnvUserId(), url).enqueue(placeCallback);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LLogger.d(url);
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                LLogger.d(url);
                if (url.contains("apk")) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        //判断是否需要开启通知栏功能
//                        NotificationSetUtil.OpenNotificationSetting(mContext, new NotificationSetUtil.OnNextLitener() {
//                            @Override
//                            public void onNext() {
//                                goToDownload(WebAty.this, url);
//
//                            }
//                        });
//                    }
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);

                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                LLogger.d(errorCode, description, failingUrl);
            }


        };

        WebChromeClient chromeClient = new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                LLogger.d(newProgress);
                if (newProgress >= 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            /**
             * 支持javascript的警告框
             * @param view
             * @param url
             * @param message
             * @param result
             * @return
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                return super.onJsAlert(webView, url, message, result);
            }

            /**
             * 支持javascript的确认框
             * @param view
             * @param url
             * @param message
             * @param result
             * @return
             */
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            /**
             * 支持javascript输入框
             * @param view
             * @param url
             * @param message
             * @param defaultValue
             * @param result
             * @return
             */
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      JsPromptResult result) {

                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!CheckUtils.isEmpty(title)) {
                    tvToolbarTitle.setText(title);
                } else {
                    tvToolbarTitle.setText("");
                }
            }
        };

        webView.setWebChromeClient(chromeClient);

        webView.setWebViewClient(client);


//    webView.setDownloadListener(new DownloadListener() {
////      @Override
////      public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
////        goToDownload(WebAty.this,url);
////      }
////    });
    }

    private String downloadUrl;

    private void initSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //允许JS代码
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.clearCache(true);
        //禁用缩放
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);

        //禁用文字缩放
        webSettings.setTextZoom(100);

        //10M缓存，api 18后，系统自动管理。
//    webSettings.setAppCacheMaxSize(10 * 1024 * 1024);

        //允许缓存，设置缓存位置
//    webSettings.setAppCacheEnabled(true);
//    webSettings.setAppCachePath(webView.getContext().getDir("appcache", 0).getPath());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);

        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        clearWebViewCache();
    }

    private void startDownloadService() {
        flag = false;
        Log.i("tag", "开始下载");
        Intent intent = new Intent(WebAty.this.getApplicationContext(), DownloadService.class);
        intent.putExtra("url", downloadUrl);
        WebAty.this.startService(intent);
    }

    public void clearWebViewCache() {
// 清除cookie即可彻底清除缓存
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();

    }

    public void goToDownload(Context context, String downloadUrl) {

        if (!flag) {
            return;
        }


        WebAty.this.downloadUrl = downloadUrl;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                toInstallPermissionSettingIntent();
                return;
            }
        }
        startDownloadService();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, 1009);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1009) {
            startDownloadService();
        }
    }

    public static void initNoti() {
    }
}
