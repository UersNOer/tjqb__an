package com.market.ssvip.white.mode.manager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import reversesuper.ReverseImpl;

/**
 * @author LiCola
 * @date 2018/10/31
 */
@ReverseImpl
public class UserPersistManagerImpl implements UserPersistManager {

  private static final String LATEST_USER_ID = "latest_user_id";
  private static final String LATEST_USER_NAME = "latest_user_name";
  private static final String LATEST_DEVICE_ID = "latest_device_id";

  private static final String EMPTY_VALUE = "";


  private SharedPreferences sharedPreferences;

  @Override
  public void init(SharedPreferences sharedPreferences) {
    this.sharedPreferences = sharedPreferences;
  }

  public SharedPreferences getPersistInstance() {
    return sharedPreferences;
  }

  @Override
  public String getLatestUserId() {
    SharedPreferences persistInstance = getPersistInstance();
    return persistInstance.getString(LATEST_USER_ID, EMPTY_VALUE);
  }

  @Override
  public boolean setLatestUserId(String userId) {
    SharedPreferences persistInstance = getPersistInstance();
    Editor edit = persistInstance.edit();
    edit.putString(LATEST_USER_ID, userId);
    return edit.commit();
  }

  @Override
  public String getLatestUserName() {
    SharedPreferences persistInstance = getPersistInstance();
    return persistInstance.getString(LATEST_USER_NAME, EMPTY_VALUE);
  }

  @Override
  public boolean setLatestUserName(String userName) {
    SharedPreferences persistInstance = getPersistInstance();
    Editor edit = persistInstance.edit();
    edit.putString(LATEST_USER_NAME, userName);
    return edit.commit();
  }

  @Override
  public String getLatestDeviceId() {
    SharedPreferences persistInstance = getPersistInstance();
    return persistInstance.getString(LATEST_DEVICE_ID, EMPTY_VALUE);
  }

  @Override
  public boolean setLatestDeviceId(String deviceId) {
    SharedPreferences persistInstance = getPersistInstance();
    Editor edit = persistInstance.edit();
    edit.putString(LATEST_DEVICE_ID, deviceId);
    return edit.commit();
  }
}
