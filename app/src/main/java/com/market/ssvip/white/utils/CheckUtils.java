package com.market.ssvip.white.utils;

import android.support.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 李可乐 on 2017/3/9 0009.
 */

public class CheckUtils {

  public static boolean isNull(@Nullable Object object) {
    return object == null;
  }

  public static boolean equals(@Nullable Object a, Object b) {
    return (a == b) || (a != null && a.equals(b));
  }

  public static boolean isEmpty(@Nullable Object[] collection) {
    return collection == null || collection.length == 0;
  }

  public static boolean isEmpty(@Nullable int[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(@Nullable long[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(@Nullable CharSequence str) {
    return str == null || str.length() == 0;
  }

  /**
   * 建议所有的集合非空检查都 使用该方法
   */
  public static boolean isEmpty(@Nullable Collection collection) {
    return collection == null || collection.isEmpty();
  }

  public static boolean isEmpty(@Nullable List list) {
    return list == null || list.isEmpty();
  }

  /**
   * 建议所有的集合非空检查都 使用该方法
   */
  public static boolean isEmpty(@Nullable Map map) {
    return map == null || map.isEmpty();
  }

  public static boolean isEmpty(@Nullable Set set) {
    return set == null || set.isEmpty();
  }

  public static boolean isEmpty(@Nullable JSONArray jsonArray) {
    return jsonArray == null || jsonArray.length() == 0;
  }

  public static boolean isEmpty(@Nullable JSONObject jsonObject) {
    return jsonObject == null || jsonObject.length() == 0;
  }
}
