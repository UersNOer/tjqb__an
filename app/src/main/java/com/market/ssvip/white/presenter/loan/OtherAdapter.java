package com.market.ssvip.white.presenter.loan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.market.ssvip.white.R;
import com.market.ssvip.white.mode.bean.RedrectBean;
import com.market.ssvip.white.presenter.web.WebAty;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.ViewHolder> {
    ArrayList<RedrectBean> list = new ArrayList<>();
    Context context;

    public void setData(Context context, List<RedrectBean> list) {
        this.context = context;
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_other, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        if (context != null)
            Glide.with(context).load(list.get(i).getLogo()).into(viewHolder.ivHead);
        viewHolder.tvName.setText(list.get(i).getName());
        viewHolder.tvLimit.setText(list.get(i).getAmountRange());
        viewHolder.tvInfo.setText(list.get(i).getExplains().length()<=17?list.get(i).getExplains():list.get(i).getExplains().substring(0,16)+"...");
        viewHolder.tvLilv.setText(list.get(i).getRate());
        viewHolder.tvTime.setText("/ " + list.get(i).getSuccessRate());
        viewHolder.linBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(WebAty.makeIntent(context, list.get(i).getAccessUrl()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_head)
        ImageView ivHead;
        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.tv_limit)
        TextView tvLimit;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.tv_lilv)
        TextView tvLilv;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.lin_back)
        LinearLayout linBack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
