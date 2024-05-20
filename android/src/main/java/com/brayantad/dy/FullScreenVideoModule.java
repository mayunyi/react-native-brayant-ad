package com.brayantad.dy;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brayantad.dy.activities.FullScreenActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class FullScreenVideoModule extends ReactContextBaseJavaModule {
  private static final String TAG = "FullScreenVideoModule";
  protected static ReactApplicationContext mContext;

  public FullScreenVideoModule(ReactApplicationContext reactContext) {
    super(reactContext);
    mContext = reactContext;
  }

  @NonNull
  @Override
  public String getName() {
    return TAG;
  }

  @ReactMethod
  public void startAd(ReadableMap options, final Promise promise) {
    String codeId = options.getString("codeid");
    String orientation = options.getString("orientation");
    DyADCore.prepareReward(promise, mContext);
    // 启动激励视频页面
    startTT(codeId, orientation);
  }

  /**
   * 启动穿山甲激励视频
   */
  public static void startTT(String codeId, String orientation) {
    Intent intent = new Intent(mContext, FullScreenActivity.class);
    try {
      intent.putExtra("codeId", codeId);
      intent.putExtra("orientation", orientation);
      Activity context = mContext.getCurrentActivity();
      // 不要过渡动画
      assert context != null;
      context.overridePendingTransition(0, 0);
      context.startActivityForResult(intent, 10000);
    } catch (Exception e) {
      e.printStackTrace();
      Log.e(TAG, "start FullScreen Activity error: ", e);
    }
  }

  // 发送事件到RN
  public static void sendEvent(String eventName, @Nullable WritableMap params) {
    mContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(TAG + "-" + eventName, params);
  }
}
