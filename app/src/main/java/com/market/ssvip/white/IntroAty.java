package com.market.ssvip.white;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.licola.llogger.LLogger;
import com.market.ssvip.white.mode.EnvManager;
import com.market.ssvip.white.mode.bean.VersionBean;
import com.market.ssvip.white.presenter.MainAty;
import com.market.ssvip.white.presenter.sign.SignInAty;
import com.market.ssvip.white.view.WindowsController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author LiCola
 * @date 2018/10/29
 */

public class IntroAty extends AppCompatActivity {

    private static final long DELAY_MILLIS = 300;
    private static final long FAST_DELAY_MILLIS = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_aty_intro);

        WindowsController.setTranslucentWindows(this);

        boolean emptyUser = EnvManager.getEnvManager().isEmptyUser();
//    emptyUser = false;
        if (emptyUser) {
            delayToSign();
        } else {
            jumpMainByType();
        }
    }
    private void jumpMainByType() {
        LLogger.d("网络判断app模式");
        EnvManager envManager = EnvManager.getEnvManager();
        envManager.getBasicsApi().fetchVersion(envManager.getEnvDeviceId()).enqueue(new Callback<VersionBean>() {
            @Override
            public void onResponse(Call<VersionBean> call, Response<VersionBean> response) {
                toMain(response.body());
            }

            @Override
            public void onFailure(Call<VersionBean> call, Throwable t) {
                VersionBean versionBean = new VersionBean();
                versionBean.setModel(2);
                versionBean.setStatus(0);
                toMain(versionBean);
            }
        });
    }

    private void toMain(final VersionBean versionBean) {
        LLogger.d("延迟跳转主页面");
        findViewById(R.id.layout_intro_group).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MainAty.makeIntent(IntroAty.this, versionBean.getModel(), versionBean.getStatus()));
                finish();
            }
        }, FAST_DELAY_MILLIS);
    }

    private void delayToSign() {
        LLogger.d("延迟跳转登陆页面");
        findViewById(R.id.layout_intro_group).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroAty.this, SignInAty.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_MILLIS);
    }

}
