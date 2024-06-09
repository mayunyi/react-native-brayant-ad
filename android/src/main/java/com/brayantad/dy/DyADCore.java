package com.brayantad.dy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import java.util.Objects;

import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.CSJSplashAd;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;

import android.app.Activity;

public class DyADCore {
  public static String TAG = "DyADCore";

  public static Boolean debug = false;

  // 头条广告init需要传的参数
  public static String userId = "";
  public static String appName = "穿山甲媒体APP";
  public static int rewardAmount = 1000;
  public static String rewardName = "金币";

  public static String tt_appid;

  public static TTAdNative TTAdSdk;
  public static TTAdManager ttAdManager;

  // 信息流广告回调
  public static Promise feedPromise;

  // 存激励视频，全屏视频的回调
  public static Promise rewardPromise;
  @SuppressLint("StaticFieldLeak")
  public static Activity rewardActivity;

  // SplashAd config
  public static int splashAd_anim_in = 0;
  public static int splashAd_anim_out = 0;

  // 激励视频类的状态
  public static boolean is_show = false;
  public static boolean is_click = false;
  public static boolean is_close = false;
  public static boolean is_reward = false;
  public static boolean is_download_idle = false;
  public static boolean is_download_active = false;
  public static boolean is_install = false;

  //codeids
  public static String codeid_splash;
  public static String codeid_feed;
  public static String codeid_draw_video;
  public static String codeid_full_video;
  public static String codeid_reward_video;
  public static String codeid_stream;

  // 缓存加载的头条广告数据
  public static TTRewardVideoAd rewardAd;
  public static TTFullScreenVideoAd fullAd;
  public static TTNativeExpressAd feedAd;
  public static TTNativeExpressAd drawfeedAd;
  public static CSJSplashAd splashAd;
  public static ReactContext reactContext;

  public static void initSdk(Context context, String appId, Boolean debug) {
    if (TTAdSdk != null && Objects.equals(tt_appid, appId)) {
      //已初始化
      Log.d(TAG, "已初始化 TTAdSdk tt_appid " + tt_appid);
      return;
    }
    tt_appid = appId;
    if (context.getClass().getName().equals("ReactApplicationContext")) {
      reactContext = (ReactContext) context;
    }

    //初始化回调结果
    resetRewardResult();

    // step1: 初始化sdk appid
    TTAdManagerHolder.init(context, appId, debug);
    // step2:创建TTAdNative对象，createAdNative(Context context)
    // feed广告context需要传入Activity对象
    ttAdManager = TTAdManagerHolder.get();

    TTAdSdk = ttAdManager.createAdNative(context);
  }


  /**
   * 准备新的激励(全屏)视频回调
   *
   * @param promise
   */
  public static void prepareReward(Promise promise, Context context) {
    rewardPromise = promise;
    resetRewardResult();
  }

  public static void resetRewardResult() {
    is_show = false;
    is_click = false;
    is_close = false;
    is_reward = false;
    is_download_idle = false;
    is_download_active = false;
    is_install = false;
  }

  /**
   * 关联页面，返回页面用
   *
   * @param activity
   */
  public static void hookActivity(Activity activity) {
    rewardActivity = activity;
  }

  public static String getRewardResult() {
    String json =
      "{\"video_play\":" +
        is_show +
        ",\"ad_click\":" +
        is_click +
        ",\"apk_install\":" +
        is_install +
        ",\"verify_status\":" +
        is_reward +
        "}";
    if (rewardPromise != null) rewardPromise.resolve(json); //返回当前窗口加载的
    if (rewardActivity != null) {
      rewardActivity.finish();
    }
    Log.d(TAG, "getRewardResult: " + json);
    return json;
  }
}
