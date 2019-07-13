package com.market.ssvip.white.presenter.moeny;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
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

import com.licola.llogger.LLogger;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.BaseFragment;
import com.market.ssvip.white.mode.EnvManager;
import com.market.ssvip.white.mode.bean.RedrectBean;
import com.market.ssvip.white.presenter.loan.OtherAdapter;
import com.market.ssvip.white.presenter.web.WebAty;
import com.market.ssvip.white.view.SwipeRefreshUtils;
import com.market.ssvip.white.view.WindowsController;

import java.util.ArrayList;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class MoneyFrag extends BaseFragment {


    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.ry_topfour)
    RecyclerView ryOther;
    OtherAdapter otherAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.framgnet_mony;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        swipeRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRedirectData();
            }
        });

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ryOther.setLayoutManager(new LinearLayoutManager(getContext()));

        otherAdapter = new OtherAdapter();
        ryOther.setAdapter(otherAdapter);
        getRedirectData();

    }
    public void getRedirectData() {
        EnvManager.getEnvManager().getBasicsApi().getRedirect(EnvManager.getEnvManager().getEnvDeviceId(), "2", "com.market.ttdk", "_meizu", EnvManager.getEnvManager().getEnvUserId())
                .enqueue(new Callback<ArrayList<RedrectBean>>() {


                    @Override
                    public void onResponse(Call<ArrayList<RedrectBean>> call, Response<ArrayList<RedrectBean>> response) {
                        otherAdapter.setData(getContext(),response.body());
                    }

                    @Override
                    public void onFailure(Call<ArrayList<RedrectBean>> call, Throwable t) {
                        Log.i("TAG", "失败" + t.toString());
                    }
                });

        SwipeRefreshUtils.delayedLoading(swipeRefresh,false);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
