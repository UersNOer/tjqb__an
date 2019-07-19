package com.market.ssvip.white.presenter.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.licola.llogger.LLogger;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.BaseFragment;
import com.market.ssvip.white.mode.EnvManager;
import com.market.ssvip.white.mode.api.BasicsApi;
import com.market.ssvip.white.mode.bean.RandomUrlBean;
import com.market.ssvip.white.presenter.about.AboutAty;
import com.market.ssvip.white.presenter.setting.SettingAty;
import com.market.ssvip.white.presenter.sign.SignInAty;
import com.market.ssvip.white.presenter.web.WebAty;
import com.market.ssvip.white.utils.TimeCountUtil;
import com.market.ssvip.white.view.WindowsController;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class UserFrag extends BaseFragment {

    @BindView(R.id.tv_user_number)
    TextView tvUserNumber;
    @BindView(R.id.tv_user_certification)
    TextView tvUserCertification;
    @BindView(R.id.layout_user_certification)
    FrameLayout layoutUserCertification;
    @BindView(R.id.layout_content_group)
    LinearLayout layoutContentGroup;
    @BindView(R.id.layout_user_apply)
    FrameLayout layoutUserApply;
    @BindView(R.id.layout_user_card)
    FrameLayout layoutUserCard;
    @BindView(R.id.layout_app_about)
    FrameLayout layoutAppAbout;
    @BindView(R.id.layout_app_set)
    FrameLayout layoutAppSet;

    @Override
    protected int getLayoutId() {
        return R.layout.module_frag_user_center;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        int statusBarHeight = WindowsController.getStatusBarHeight(layoutContentGroup.getContext());
        layoutContentGroup.setPadding(layoutContentGroup.getPaddingLeft(), statusBarHeight,
                layoutContentGroup.getPaddingRight(), layoutContentGroup.getPaddingBottom());

        tvUserNumber.setText(EnvManager.getEnvManager().getEnvUserName());

        return rootView;
    }


    @OnClick(R.id.layout_user_apply)
    public void onUserApplyClick(View view) {
        Intent intent = new Intent(getContext(), MineApplyAty.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_user_card)
    public void onUserCardClick(View view) {
        CrashReport.testJavaCrash();
        Intent intent = new Intent(getContext(), MineCardAty.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_app_about)
    public void onAppAboutClick(View view) {
        Intent intent = new Intent(getContext(), AboutAty.class);
        startActivity(intent);
    }

    private static final int REQUEST_CODE = 100;

    @OnClick(R.id.layout_app_set)
    public void onAppSetClick(View view) {
        Intent intent = new Intent(getContext(), SettingAty.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick(R.id.bt_goborrow)
    public void gotoBorrow(View view) {
        EnvManager envManager = EnvManager.getEnvManager();
        envManager.getBasicsApi().getRandomUrl("11")
                .enqueue(
                        new Callback<RandomUrlBean>() {
                            @Override
                            public void onResponse(Call<RandomUrlBean> call, Response<RandomUrlBean> response) {
                                if(response.body().productProxyUrl!=null){
                                    startActivity(WebAty.makeIntent(getContext(), response.body().productProxyUrl));
                                }
                            }

                            @Override
                            public void onFailure(Call<RandomUrlBean> call, Throwable t) {
                                LLogger.e(t);
                            }
                        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            EnvManager.getEnvManager().loginOut();
            getActivity().finish();
            startActivity(new Intent(getContext(), SignInAty.class));
        }

    }
}
