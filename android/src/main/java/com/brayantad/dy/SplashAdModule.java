package com.brayantad.dy;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.brayantad.dy.activities.SplashActivity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

// 开屏广告
public class SplashAdModule extends ReactContextBaseJavaModule {

  String TAG = "SplashAd";
  ReactApplicationContext mContext;

  public SplashAdModule(@NonNull ReactApplicationContext reactContext) {
    super(reactContext);
    mContext = reactContext;
  }

  @NonNull
  @Override
  public String getName() {
    return TAG;
  }

  @ReactMethod
  public void loadSplashAd(ReadableMap options) {
    String codeid = options.hasKey("codeid") ? options.getString("codeid") : null;
    String anim = options.hasKey("anim") ? options.getString("anim") : "default";
    // 设置开屏广告启动动画
    setAnim(anim);
    // 默认走穿山甲
    startSplash(codeid);
  }

  private void startSplash(String codeid) {
    Intent intent = new Intent(mContext, SplashActivity.class);
    try {
      intent.putExtra("codeid", codeid);
      final Activity context = getCurrentActivity();
      context.startActivity(intent);

      if (DyADCore.splashAd_anim_in != -1) {
        // 实现广告开启跳转 Activity 动画设置
        context.overridePendingTransition(DyADCore.splashAd_anim_in, DyADCore.splashAd_anim_out);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }





  private void setAnim(String animStr) {
    switch (animStr) {
      case "catalyst":
        DyADCore.splashAd_anim_in = 0;
        DyADCore.splashAd_anim_out = 0;
        break;
      case "none":
        DyADCore.splashAd_anim_in = 0;
        DyADCore.splashAd_anim_out = 0;
        break;
      case "slide":
        DyADCore.splashAd_anim_in = android.R.anim.slide_in_left;
        DyADCore.splashAd_anim_out = android.R.anim.slide_out_right;
        break;
      case "fade":
        DyADCore.splashAd_anim_in = android.R.anim.fade_in;
        DyADCore.splashAd_anim_out = android.R.anim.fade_in;
        break;
      default:
        DyADCore.splashAd_anim_in = 0;
        DyADCore.splashAd_anim_out = 0;
        break;
    }
  }

}
