package com.market.ssvip.white.presenter.loan;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import butterknife.BindView;
import com.licola.llogger.LLogger;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.BaseFragment;
import com.market.ssvip.white.mode.EnvManager;
import com.market.ssvip.white.presenter.web.WebAty;
import com.market.ssvip.white.view.SwipeRefreshUtils;
import com.market.ssvip.white.view.WindowsController;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class LoanFrag extends BaseFragment {

  private static final String URL = "http://api.lpkjb.cn/dc/index.html?key=com.market.ttdk&user_id="+ EnvManager.getEnvManager().getEnvUserId()+"&market=_meizu";
  @BindView(R.id.swipe_refresh)
  SwipeRefreshLayout swipeRefresh;
  @BindView(R.id.layout_content_group)
  LinearLayout layoutContentGroup;

  WebView webView;

  @Override
  protected int getLayoutId() {
    return R.layout.module_frag_web;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    Log.i("-------------","-");

    View rootView = super.onCreateView(inflater, container, savedInstanceState);

    swipeRefresh.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        onLoadUrl(URL);
      }
    });

    int statusBarHeight = WindowsController.getStatusBarHeight(mContext);

    webView = new WebView(mContext);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT);
    params.topMargin = statusBarHeight;
    layoutContentGroup.addView(webView, params);

    webView.setPadding(0, statusBarHeight, 0, 0);
    initSettings(webView);
    initClient(webView);
    onLoadUrl(URL);
    return rootView;
  }

  private void onLoadUrl(String url) {
    if (webView == null) {
      return;
    }
    webView.loadUrl(url);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (KeyEvent.KEYCODE_BACK == keyCode) {
      if (onGoBack()) {
        return true;
      }
    }

    return super.onKeyDown(keyCode, event);
  }

  private boolean onGoBack() {
    if (webView == null) {
      return false;
    }

    if (webView.canGoBack()) {
      webView.goBack();
      return true;
    }

    return false;
  }

  private void initClient(final WebView webView) {
    WebViewClient client = new WebViewClient() {

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        webView.loadUrl(url);


        Log.i("TAG",url);
        startActivity(WebAty.makeIntent(mContext, url));
        return true;
      }

      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        LLogger.d(url);
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        LLogger.d(url);
        SwipeRefreshUtils.delayedLoading(swipeRefresh, false);
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
//        LLogger.d(newProgress);
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
        return super.onJsAlert(view, url, message, result);
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
    };

    webView.setWebChromeClient(chromeClient);

    webView.setWebViewClient(client);
  }

  private void initSettings(WebView webView) {
    WebSettings webSettings = webView.getSettings();
    webSettings.setLoadWithOverviewMode(true);
    webSettings.setUseWideViewPort(true);
    //允许JS代码
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

//WebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); 默认不使用缓存！
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

    //允许WebView使用File协议
    webSettings.setAllowFileAccess(true);
    //不保存密码
    webSettings.setSavePassword(false);

    //自动加载图片
    webSettings.setLoadsImagesAutomatically(true);

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }
}
