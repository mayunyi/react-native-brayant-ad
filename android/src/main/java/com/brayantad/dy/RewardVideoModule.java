package com.brayantad.dy;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.brayantad.dy.activities.RewardActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

public class RewardVideoModule extends ReactContextBaseJavaModule {
  private static final String TAG = "RewardVideo";
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
    startTT(codeId);
  }

  /**
   * 启动穿山甲激励视频
   *
   * @param codeId
   */
  public static void startTT(String codeId) {
    Activity context = mContext.getCurrentActivity();
    try {
      Intent intent = new Intent(mContext, RewardActivity.class);
      intent.putExtra("codeId", codeId);
      // 不要过渡动画
      context.overridePendingTransition(0, 0);
      context.startActivityForResult(intent, 10000);
    } catch (Exception e) {
      e.printStackTrace();
      Log.e(TAG, "start reward Activity error: ", e);
    }
  }

}
