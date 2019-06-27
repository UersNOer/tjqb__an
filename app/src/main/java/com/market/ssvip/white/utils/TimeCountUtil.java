package com.market.ssvip.white.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/10.
 * 获取验证码提示  倒计时
 */

public class TimeCountUtil extends CountDownTimer {
    private TextView btn;//按钮
    private int flg;
    public String aginGet = "重新获取";

    public TimeCountUtil(long millisInFuture, long countDownInterval, TextView btn, int flg) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.flg = flg;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setEnabled(false);//设置不能点击
        btn.setText("重发(" + millisUntilFinished / 1000 + ")");//设置倒计时时间

        //设置按钮为灰色，这时是不能点击的
//        btn.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_duck_back));
        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
        if (btn.getText().toString().length() == 9) {
            span.setSpan(new ForegroundColorSpan(Color.parseColor("#176FF3")), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时时间显示为红色
        } else if (btn.getText().toString().length() == 10) {
            span.setSpan(new ForegroundColorSpan(Color.parseColor("#176FF3")), 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时时间显示为红色
        }
        btn.setText(span);
    }

    @Override
    public void onFinish() {
        if (flg == 0) {
            btn.setText(aginGet);
            btn.setEnabled(true);//重新获得点击
            //btn.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_btn_back));// 还原背景色
        } else if (flg == 1) {
            btn.setText("重新获取语音验证码");
            btn.setClickable(true);//重新获得点击
        } else if (flg == 2) {
            btn.setText("重新获取邮箱验证码");
            btn.setClickable(true);//重新获得点击
        }
    }

    public void reSet() {
        this.cancel();
        this.onFinish();
    }


}

