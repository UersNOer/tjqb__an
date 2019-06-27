package com.market.ssvip.white.component.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import com.market.ssvip.white.utils.CheckUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiCola on 2017/6/5. 按照FragmentActivity和FragmentPagerAdapter 对子Fragment的生命周期和重建的顺序 特性
 * 抽象的父类，建议项目中所有的有关FragmentPagerAdapter 都直接继承该抽象类，或按照该思路管理Fragment
 */

public abstract class FragmentPagerRebuildAdapter<T extends Fragment> extends FragmentPagerAdapter {

  protected final int pageSize;

  public List<T> fragments;

  public FragmentPagerRebuildAdapter(FragmentManager fm, int pageSize) {
    super(fm);
    this.pageSize = pageSize;

    fragments = loadFragment(pageSize);

  }

  private List<T> loadFragment(int pageSize) {
    ArrayList<T> placeList = new ArrayList<>(pageSize);

    for (int i = 0; i < pageSize; i++) {
      placeList.add(null);
    }
    return placeList;
  }

  public List<T> getFragmentList() {
    return fragments;
  }

  /**
   * 根据传入位置 得到Fragment
   */
  public T getFragmentByPosition(int position) {
    if (CheckUtils.isEmpty(fragments)) {
      return null;
    }

    if (position >= fragments.size()) {
      return null;
    }

    return fragments.get(position);
  }

  /**
   * 得到ViewPager当前页的Fragment
   */
  public T getFragmentByCurrentItem(ViewPager viewPager) {
    if (viewPager == null) {
      return null;
    }
    return getFragmentByPosition(viewPager.getCurrentItem());
  }

  /**
   * 根据位置参数创建并返回一个Fragment实例 该方法FragmentActivity在新建Fragment时调用，销毁后重建时不会调用
   *
   * @param position 位置参数
   * @return 创建好的Fragment实例
   */
  protected abstract T createFragment(int position);

  /**
   * 操作某个Fragment，设置或绑定操作或数据 该方法，新建或重建都调用
   *
   * @param fragment 对某个位置的Fragment
   * @param position 某个位置的位置参数
   */
  protected abstract void bindFragment(T fragment, int position);

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Object object = super.instantiateItem(container, position);
    bindFragment((T) object, position);
    fragments.set(position, (T) object);
    return object;
  }

  @Override
  public Fragment getItem(int position) {
    Fragment fragment = createFragment(position);
    if (fragment == null) {
      throw new UnsupportedOperationException("createFragment(position="
          + position
          + " 没有返回Fragment实例),检查代码确保createFragment方法覆盖所有position");
    }

    return fragment;
  }

  @Override
  public int getCount() {
    return pageSize;
  }
}
