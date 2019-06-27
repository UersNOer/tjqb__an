package com.market.ssvip.white.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author LiCola
 * @date 2018/8/27
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

  /**
   * 数据项
   */
  protected T data;

  /**
   * 在type中（该type类的数据列表中）的实际位置
   */
  protected int typePosition;

  /**
   * 在Recycler的实际位置
   */
  protected int position;


  public BaseViewHolder(View itemView) {
    super(itemView);
    onFindView(itemView);
    onBindView(itemView);
  }

  protected void onBindView(View itemView) {

  }

  protected abstract void onFindView(View itemView);

  protected void bindData(T data, int typePosition, int position) {
    this.data = data;
    this.typePosition = typePosition;
    this.position = position;
    onBindData(data, typePosition, position);
  }

  public abstract void onBindData(T data, int typePosition, int position);

}
