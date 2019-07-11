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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.gson.Gson;
import com.licola.llogger.LLogger;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.BaseFragment;
import com.market.ssvip.white.mode.EnvManager;
import com.market.ssvip.white.mode.bean.BannerBean;
import com.market.ssvip.white.mode.bean.RedrectBean;
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
    private ArrayList<BannerBean> body = new ArrayList<>();

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.ry_topfour)
    RecyclerView ryTopFout;
    TopFourAdapter topFourAdapter;
    GlideImageLoader glideImageLoader = new GlideImageLoader();


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
                startActivity(WebAty.makeIntent(mContext, body.get(position - 1).accessUrl));
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

        ryTopFout.setLayoutManager(new GridLayoutManager(getContext(), 4));
        topFourAdapter = new TopFourAdapter();
        ryTopFout.setAdapter(topFourAdapter);
        getBannerData();
        getRedirectData();
    }


    public void getRedirectData() {
        EnvManager.getEnvManager().getBasicsApi().getRedirect(EnvManager.getEnvManager().getEnvDeviceId(), "1", "com.market.ttdk", "_meizu", EnvManager.getEnvManager().getEnvUserId())
                .enqueue(new Callback<ArrayList<RedrectBean>>() {


                    @Override
                    public void onResponse(Call<ArrayList<RedrectBean>> call, Response<ArrayList<RedrectBean>> response) {
                        topFourAdapter.setData(getContext(), (ArrayList<RedrectBean>) response.body().subList(0, 4));
                    }

                    @Override
                    public void onFailure(Call<ArrayList<RedrectBean>> call, Throwable t) {
                        Log.i("TAG", "失败" + t.toString());
                    }
                });
    }


    private void getBannerData() {
        EnvManager.getEnvManager().getBasicsApi().getBannerList("2", "1", "15")
                .enqueue(new Callback<ArrayList<BannerBean>>() {


                    @Override
                    public void onResponse(Call<ArrayList<BannerBean>> call, Response<ArrayList<BannerBean>> response) {
                        body.clear();
                        body.addAll(response.body());
                        SetBannerViewImages();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<BannerBean>> call, Throwable t) {
                    }
                });

    }


    private void SetBannerViewImages() {
        ArrayList<String> bannerImageList = new ArrayList<>();
        for (BannerBean q : body) {
            bannerImageList.add(q.bannerLink);
        }
        banner.setImages(bannerImageList).setImageLoader(glideImageLoader).start();
    }

}
