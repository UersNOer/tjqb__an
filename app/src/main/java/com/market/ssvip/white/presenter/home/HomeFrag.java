package com.market.ssvip.white.presenter.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.licola.llogger.LLogger;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.BaseFragment;
import com.market.ssvip.white.component.Timer;
import com.market.ssvip.white.component.Timer.OnTimeListener;
import com.market.ssvip.white.component.adapter.BaseRecyclerAdapter;
import com.market.ssvip.white.component.adapter.BaseViewHolder;
import com.market.ssvip.white.component.image.ImageLoadBuilder;
import com.market.ssvip.white.mode.EnvManager;
import com.market.ssvip.white.mode.api.BasicsApi;
import com.market.ssvip.white.mode.bean.ProductItemBean;
import com.market.ssvip.white.mode.bean.UserOrderStateBean;
import com.market.ssvip.white.presenter.apply.ApplyGroupAty;
import com.market.ssvip.white.presenter.web.WebAty;
import com.market.ssvip.white.utils.CheckUtils;
import com.market.ssvip.white.utils.ExceptionHelper;
import com.market.ssvip.white.utils.TimeUtils;
import com.market.ssvip.white.view.WindowsController;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class HomeFrag extends BaseFragment {

  private static final int PAGE_STATE_NO_MORE = 0;
  private static final int PAGE_STATE_MORE = 1;

  private static final int USER_STATE_APPLY = -2;
  private static final int USER_STATE_REJECT = 0;

  private static final long COUNT_DOWN_TIME = 5 * 60 * 1000;//5分钟
  private static final String COUNT_DOWN_FORMAT = "审核中（倒计时%s）";


  private static final int MIN_VALUE = 500;
  private static final int MAX_VALUE = 1200;

  private static final float SCALE = 100.0f;

  @BindView(R.id.layout_content_group)
  LinearLayout layoutContentGroup;
  @BindView(R.id.tv_home_valid_number)
  TextView tvHomeValidNumber;
  @BindView(R.id.iv_home_loading)
  ImageView ivHomeLoading;
  @BindView(R.id.layout_home_loading_group)
  RelativeLayout layoutHomeLoadingGroup;
  @BindView(R.id.seek_home)
  SeekBar seekHome;
  @BindView(R.id.layout_home_seek_group)
  RelativeLayout layoutHomeSeekGroup;
  @BindView(R.id.tv_home_operate)
  TextView tvHomeOperate;
  @BindView(R.id.iv_home)
  ImageView ivHome;
  @BindView(R.id.layout_head_group)
  LinearLayout layoutHeadGroup;
  @BindView(R.id.recycler_container)
  RecyclerView recyclerContainer;
  @BindView(R.id.layout_more_group)
  LinearLayout layoutMoreGroup;
  @BindView(R.id.layout_apply_group)
  LinearLayout layoutApplyGroup;
  @BindView(R.id.layout_over_group)
  LinearLayout layoutOverGroup;
  @BindView(R.id.layout_more_group_title)
  LinearLayout layoutMoreGroupTitle;
  @BindView(R.id.tv_head_more)
  TextView tvHeadMore;

  private boolean showMore = false;


  @Override
  protected int getLayoutId() {
    return R.layout.module_frag_home;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    Log.i("-------------","------");

    int statusBarHeight = WindowsController.getStatusBarHeight(layoutContentGroup.getContext());
    layoutContentGroup.setPadding(layoutContentGroup.getPaddingLeft(), statusBarHeight,
        layoutContentGroup.getPaddingRight(), layoutContentGroup.getPaddingBottom());
    layoutHeadGroup.setPadding(layoutHeadGroup.getPaddingLeft(), statusBarHeight,
        layoutHeadGroup.getPaddingRight(), layoutHeadGroup.getPaddingBottom());

    seekHome.setOnSeekBarChangeListener(new MySeekChangeListener());
    int max = MAX_VALUE - MIN_VALUE;
    seekHome.setMax(max);
    seekHome.setProgress(max);
    updateValidNumber(MAX_VALUE);

    return rootView;
  }

  public void setShowMore(int pageState) {
    this.showMore = pageState == PAGE_STATE_MORE;
    LLogger.d("是否显示更多:" + showMore);
    onFetchUserApplyState();
  }

  private void onFetchUserApplyState() {
    EnvManager envManager = EnvManager.getEnvManager();
    envManager.getBasicsApi().fetchUserApplyOrder(envManager.getEnvDeviceId(),envManager.getEnvUserId())
        .enqueue(new Callback<UserOrderStateBean>() {
          @Override
          public void onResponse(Call<UserOrderStateBean> call,
              Response<UserOrderStateBean> response) {
            UserOrderStateBean body = response.body();
            int status = body.getStatus();
//            status=USER_STATE_APPLY;
            if (status == USER_STATE_APPLY) {
              onShowApply();
            } else if (status == USER_STATE_REJECT) {
              onShowReject();
            }
          }

          @Override
          public void onFailure(Call<UserOrderStateBean> call, Throwable t) {
            onShowApply();
            showToast(ExceptionHelper.mapperException(t));
          }
        });
  }

  private static final int REQUEST_CODE = 100;

  @OnClick(R.id.tv_home_operate)
  public void onOperateClick(View view) {
    Integer validNumber = Integer.valueOf(tvHomeValidNumber.getText().toString());
    startActivityForResult(ApplyGroupAty.makeIntent(mContext, validNumber), REQUEST_CODE);
  }

  @OnClick(R.id.layout_more_group_title)
  public void onMoreTitleClick(View view) {
    String url = "http://api.lpkjb.cn/dc/index.html";
    startActivity(WebAty.makeIntent(mContext, url));
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      onShowCount();
    }
  }

  private void onShowApply() {
    LLogger.d("申请模式");
    layoutApplyGroup.setVisibility(View.VISIBLE);
    layoutOverGroup.setVisibility(View.GONE);

    layoutHomeSeekGroup.setVisibility(View.VISIBLE);
    layoutHomeLoadingGroup.setVisibility(View.GONE);

    tvHomeOperate.setEnabled(true);
    tvHomeOperate.setText("立即拿钱");
  }

  private void onShowCount() {
    layoutHomeSeekGroup.setVisibility(View.GONE);
    layoutHomeLoadingGroup.setVisibility(View.VISIBLE);

    tvHomeOperate.setEnabled(false);
    tvHomeOperate.setText(
        String.format(Locale.CHINA, COUNT_DOWN_FORMAT, TimeUtils.formatTime(COUNT_DOWN_TIME)));
    Timer timer = new Timer(new Handler(), new OnTimeListener() {
      @Override
      public void onWork(long duration) {
        LLogger.d(duration);
        tvHomeOperate.setText(String.format(Locale.CHINA, COUNT_DOWN_FORMAT,
            TimeUtils.formatTime(COUNT_DOWN_TIME - duration)));
      }

      @Override
      public void onEnd() {
        onShowReject();
      }
    });
    timer.restart(COUNT_DOWN_TIME);

  }

  private void onShowReject() {
    LLogger.d("拒绝模式");

    layoutApplyGroup.setVisibility(View.GONE);
    layoutOverGroup.setVisibility(View.VISIBLE);

    onShowRejectDefault();
    if (!showMore) {
      return;
    }

    EnvManager envManager = EnvManager.getEnvManager();
    envManager.getBasicsApi().fetchProductList(envManager.getEnvDeviceId(),BasicsApi.SIGN).enqueue(
        new Callback<List<ProductItemBean>>() {
          @Override
          public void onResponse(Call<List<ProductItemBean>> call,
              Response<List<ProductItemBean>> response) {
            List<ProductItemBean> beanList = response.body();
            if (CheckUtils.isEmpty(beanList)) {
              onShowRejectDefault();
            } else {
              onShowRejectProduct(beanList);
            }
          }

          @Override
          public void onFailure(Call<List<ProductItemBean>> call, Throwable t) {
            onShowRejectDefault();
            showToast(ExceptionHelper.mapperException(t));
          }
        });
  }

  private void onShowRejectDefault() {
    tvHeadMore.setText("审核未通过，请下次再试");
    layoutMoreGroup.setVisibility(View.GONE);
  }

  private void onShowRejectProduct(List<ProductItemBean> beans) {
    tvHeadMore.setText("更多借款请查看以下推荐");
    layoutMoreGroup.setVisibility(View.VISIBLE);

    final RecyclerAdapter adapter = new RecyclerAdapter(mContext);
    recyclerContainer.setAdapter(adapter);
    recyclerContainer.setLayoutManager(new GridLayoutManager(mContext, 4));

    adapter.initData(beans);
  }


  private class MySeekChangeListener implements OnSeekBarChangeListener {

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//      LLogger.d(progress, fromUser);
      if (!fromUser) {
        int validScale = getValidScale(progress);
        updateValidNumber(MIN_VALUE + validScale);
      } else {
        if (progress <= 0) {
          updateValidNumber(MIN_VALUE);
        } else if (progress >= seekBar.getMax()) {
          updateValidNumber(MAX_VALUE);
        }
      }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
      int progress = seekBar.getProgress();
      int progressOffset = getValidScale(progress);
      LLogger.d(progress, progressOffset);
      seekBar.setProgress(progressOffset);
    }
  }

  private void updateValidNumber(int minValue) {
    tvHomeValidNumber.setText(String.valueOf(minValue));
  }

  private int getValidScale(int progress) {
    return (int) (Math.round(progress / SCALE) * SCALE);
  }


  private final class RecyclerAdapter extends BaseRecyclerAdapter {

    public RecyclerAdapter(Context context) {
      super(context);
    }

    public void initData(List<ProductItemBean> models) {
      typeMaintainer.replace(NormalViewHolder.ID, models);
      notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      switch (viewType) {
        case NormalViewHolder.ID:
          return new NormalViewHolder(inflate(NormalViewHolder.ID, parent));
        default:
          return null;
      }
    }

    private class NormalViewHolder extends BaseViewHolder<ProductItemBean> {

      public static final int ID = R.layout.module_recycler_item_product;

      ImageView ivItemCover;
      TextView tvItemName;

      public NormalViewHolder(View itemView) {
        super(itemView);
      }

      @Override
      protected void onFindView(View itemView) {
        ivItemCover = itemView.findViewById(R.id.iv_item_cover);
        tvItemName = itemView.findViewById(R.id.tv_item_name);
      }

      @Override
      protected void onBindView(View itemView) {
        itemView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            String url = data.getUrl();
            startActivity(WebAty.makeIntent(mContext, url));
          }
        });
      }

      @Override
      public void onBindData(ProductItemBean data, int typePosition, int position) {
        ImageLoadBuilder.load(ivItemCover, data.getLogo())
            .build();
        tvItemName.setText(data.getName());
      }


    }

  }
}
