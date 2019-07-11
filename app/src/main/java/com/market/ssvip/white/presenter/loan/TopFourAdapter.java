package com.market.ssvip.white.presenter.loan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.market.ssvip.white.R;
import com.market.ssvip.white.mode.bean.RedrectBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.GET;

public class TopFourAdapter extends RecyclerView.Adapter<TopFourAdapter.ViewHolder> {
    ArrayList<RedrectBean> list = new ArrayList<>();
    Context context;

    public void setData(Context context, ArrayList<RedrectBean> list) {
        this.context = context;
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_topfour, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (context != null)
            Glide.with(context).load(list.get(i).getLogo()).into(viewHolder.ivHead);
        viewHolder.tvName.setText(list.get(i).getName());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
