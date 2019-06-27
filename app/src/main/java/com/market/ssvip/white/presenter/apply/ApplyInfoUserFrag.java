package com.market.ssvip.white.presenter.apply;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.market.ssvip.white.R;
import com.market.ssvip.white.base.BaseFragment;
import com.market.ssvip.white.utils.CheckUtils;

/**
 * @author LiCola
 * @date 2018/10/31
 */
public class ApplyInfoUserFrag extends BaseFragment {

  @BindView(R.id.tv_input_name)
  TextView tvInputName;
  @BindView(R.id.et_input_name)
  EditText etInputName;
  @BindView(R.id.tv_input_id)
  TextView tvInputId;
  @BindView(R.id.et_input_id)
  EditText etInputId;
  @BindView(R.id.tv_apply_operate)
  TextView tvApplyOperate;

  private OnInputOperateListener onInputOperateListener;

  @Override
  protected int getLayoutId() {
    return R.layout.module_frag_apply_info_user;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    return rootView;
  }

  @OnClick(R.id.tv_apply_operate)
  public void onOperateClick(View view){
    String userName = etInputName.getText().toString();
    String userId = etInputId.getText().toString();
    if (CheckUtils.isEmpty(userName)){
      showToast("请填写您的真实姓名");
      return;
    }

    if(CheckUtils.isEmpty(userId)){
      showToast("请填写您的身份证号码");
      return;
    }

    if (onInputOperateListener!=null){
      onInputOperateListener.onOperate(userName,userId );
    }
  }

  public void setOnInputOperateListener(
      OnInputOperateListener onInputOperateListener) {
    this.onInputOperateListener = onInputOperateListener;
  }

  public interface OnInputOperateListener{
    void onOperate(String userName,String userId);
  }

}
