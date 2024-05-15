package com.brayantad.dy;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brayantad.dy.activities.RewardActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RewardVideoModule extends ReactContextBaseJavaModule {
  private static final String TAG = "RewardVideoModule";
  private static ReactApplicationContext mContext;
  public static Promise promise;

  @NonNull
  @Override
  public String getName() {
    return TAG;
  }

  public RewardVideoModule(ReactApplicationContext context) {
    super(context);
    mContext = context;
  }

  @ReactMethod
  public void startAd(ReadableMap options, final Promise promise) {
    //拿到参数
    String codeId = options.getString("codeid");
    String provider = options.getString("provider");
    Log.d(TAG, "startAd:codeId: " + codeId + provider);
    //准备激励回调
    DyADCore.prepareReward(promise, mContext);
    //准备激励回调
    startTT(codeId);
  }

  /**
   * 启动穿山甲激励视频
   *
   * @param codeId
   */
  public static void startTT(String codeId) {
    Activity currentActivity = mContext.getCurrentActivity();
    if (currentActivity == null) {
      Log.e(TAG, "startTT: currentActivity is null");
      return;
    }

    try {
      Intent intent = new Intent(currentActivity, RewardActivity.class);
      intent.putExtra("codeId", codeId);
      // Disable transition animation
      currentActivity.overridePendingTransition(0, 0);
      currentActivity.startActivityForResult(intent, 10000);
    } catch (Exception e) {
      e.printStackTrace();
      Log.e(TAG, "start reward Activity error: ", e);
    }
  }


  // 发送事件到RN
  public static void sendEvent(String eventName, @Nullable WritableMap params) {
    mContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(TAG + "-" + eventName, params);
  }

}
