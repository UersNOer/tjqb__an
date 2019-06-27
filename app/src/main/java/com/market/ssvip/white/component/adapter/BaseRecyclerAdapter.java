package com.market.ssvip.white.component.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author LiCola
 * @date 2018/8/21
 */
public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

  protected final TypeMaintainer typeMaintainer;

  protected LayoutInflater inflater;
  protected Context context;

  public BaseRecyclerAdapter(Context context) {
    this.context = context;
    this.inflater = LayoutInflater.from(context);
    this.typeMaintainer = TypeMaintainer.create();
  }

  public View inflate(@LayoutRes int layoutId, @NonNull ViewGroup parent) {
    return inflater.inflate(layoutId, parent, false);
  }

  @Override
  public int getItemViewType(int position) {
    return typeMaintainer.getType(position);
  }

  @Override
  public int getItemCount() {
    return typeMaintainer.getCount();
  }


  @Override
  public void onBindViewHolder(@NonNull BaseViewHolder viewHolder,
      int position) {

    int typePosition = typeMaintainer.getTypePosition(position);
    Object data = typeMaintainer
        .getTypeDataByTypePosition(viewHolder.getItemViewType(), typePosition);
    viewHolder.bindData(data, typePosition, position);

  }


}
