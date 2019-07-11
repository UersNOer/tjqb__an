package com.market.ssvip.white.presenter.loan;

import android.content.Intent;
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
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.licola.llogger.LLogger;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.BaseFragment;
import com.market.ssvip.white.mode.EnvManager;
import com.market.ssvip.white.mode.bean.UserBean;
import com.market.ssvip.white.presenter.web.WebAty;
import com.market.ssvip.white.utils.ExceptionHelper;
import com.market.ssvip.white.utils.GlideImageLoader;
import com.market.ssvip.white.view.SwipeRefreshUtils;
import com.market.ssvip.white.view.WindowsController;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;

import static com.market.ssvip.white.mode.api.BasicsApi.CODE_TYPE_LOGIN;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class LoanFrag extends BaseFragment {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.banner)
    Banner banner;
    GlideImageLoader glideImageLoader = new GlideImageLoader();


    ArrayList<String> bannerImageList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.module_frag_web;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("-------------", "-");

        View rootView = super.onCreateView(inflater, container, savedInstanceState);


        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
            }
        });


        swipeRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                SwipeRefreshUtils.delayedLoading(swipeRefresh, false);
            }





        });


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBannerData();
    }


    private void getBannerData() {
//        EnvManager.getEnvManager().getBasicsApi().fetchUserByWithCode(EnvManager.getEnvManager().getEnvDeviceId(), phone, code, CODE_TYPE_LOGIN)
//                .enqueue(new Callback<UserBean>() {
//                    @Override
//                    public void onResponse(Call<UserBean> call, Response<UserBean> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserBean> call, Throwable t) {
//                    }
//                });

        SetBannerViewImages();
    }

    private void SetBannerViewImages() {
        bannerImageList.clear();
        bannerImageList.add("http://img5.imgtn.bdimg.com/it/u=3300305952,1328708913&fm=26&gp=0.jpg");
        bannerImageList.add("http://img2.imgtn.bdimg.com/it/u=1718395925,3485808025&fm=26&gp=0.jpg");
        bannerImageList.add("http://img2.imgtn.bdimg.com/it/u=180868167,273146879&fm=214&gp=0.jpg");
        bannerImageList.add("http://img0.imgtn.bdimg.com/it/u=3357786243,3135716437&fm=26&gp=0.jpg");
//        banner.no

        banner.setImages(bannerImageList).setImageLoader(glideImageLoader).start();
    }

}
