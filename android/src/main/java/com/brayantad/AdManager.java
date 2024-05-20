package com.brayantad;

import android.util.Log;

import androidx.annotation.NonNull;

import com.brayantad.dy.DyADCore;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.List;
import java.util.Objects;

public class AdManager extends ReactContextBaseJavaModule {
  public static ReactApplicationContext reactAppContext;
  public static final String TAG = "AdManager";

  public AdManager(ReactApplicationContext reactContext) {
    super(reactContext);
    reactAppContext = reactContext;
  }

  @NonNull
  @Override
  public String getName() {
    return TAG;
  }
  @ReactMethod
  public void init(ReadableMap options, Promise promise) {
    //默认头条穿山甲
    DyADCore.tt_appid = options.hasKey("appid") ? options.getString("appid") : DyADCore.tt_appid;
    DyADCore.debug = options.hasKey("debug") ? options.getBoolean("debug") : DyADCore.debug;
    DyADCore.rewardName = options.hasKey("reward") ? options.getString("reward") : DyADCore.rewardName;
    DyADCore.rewardAmount = options.hasKey("amount") ? options.getInt("amount") : DyADCore.rewardAmount;
    DyADCore.appName = options.hasKey("app") ? options.getString("app") : DyADCore.appName;

    if (DyADCore.tt_appid != null) {
      DyADCore.initSdk(reactAppContext, DyADCore.tt_appid, DyADCore.debug);
      boolean sdkReady = TTAdSdk.isSdkReady();
      if(!sdkReady) {
        TTAdSdk.start(new TTAdSdk.Callback() {
          @Override
          public void success() {
            Log.i(TAG, "success: " + TTAdSdk.isSdkReady());
            promise.resolve(true);
          }

          @Override
          public void fail(int code, String msg) {
            Log.i(TAG, "fail:  code = " + code + " msg = " + msg);
            promise.reject(TAG, "fail:  code = " + code + " msg = " + msg);
          }
        });
      }
      promise.resolve(true);
//      if (options.hasKey("codeid_reward_video")) {
//        DyADCore.codeid_reward_video = options.getString("codeid_reward_video");
//        //提前加载
//        assert DyADCore.codeid_reward_video != null;
//
//        if(!sdkReady) {
//          TTAdSdk.start(new TTAdSdk.Callback() {
//            @Override
//            public void success() {
//              Log.i(TAG, "success: " + TTAdSdk.isSdkReady());
//              RewardActivity.loadAd(
//                DyADCore.codeid_reward_video,
//                () -> {
//                  Log.d(
//                    TAG,
//                    "提前加载 成功 codeid_reward_video " +
//                      DyADCore.codeid_reward_video
//                  );
//                }
//              );
//            }
//
//            @Override
//            public void fail(int code, String msg) {
//              Log.i(TAG, "fail:  code = " + code + " msg = " + msg);
//            }
//            private static TTAdConfig buildConfig(Context context) {
//              return new TTAdConfig.Builder()
//                .appId(DyADCore.tt_appid)//应用ID
//                .supportMultiProcess(true)//开启多进程
//                .build();
//            }
//          });
//        }
//      }
    }
  }


  /**
   * 方便从RN主动预加载第一个广告，避免用户第一个签到的信息流广告加载+图片显示感觉很慢
   * （需要注意在展示弹层前才预加载）
   */
  @ReactMethod
  public void loadFeedAd(ReadableMap options, final Promise promise) {
    String codeId = options.getString("codeid");
    float width = 0;
    if (options.hasKey("adWidth")) {
      width = Float.parseFloat(Objects.requireNonNull(options.getString("adWidth")));
    }
    DyADCore.feedPromise = promise;
//    if (DyADCore.feed_provider.equals("腾讯")) {
//      //FIXME ...
//      return;
//    }
//    if (DyADCore.feed_provider.equals("百度")) {
//      //百度的是横幅banner，不需要预加载
//      return;
//    }
    loadTTFeedAd(codeId, width);
  }

  /**
   * 加载穿山甲的信息流广告
   *
   * @param codeId
   * @param width
   */
  private static void loadTTFeedAd(String codeId, float width) {
    if (DyADCore.TTAdSdk == null) {
      Log.e(TAG, "TTAdSdk 还没初始化");
      return;
    }

    // step4:创建广告请求参数AdSlot,具体参数含义参考文档
    // 默认宽度，兼容大部分弹层的宽度即可
    float expressViewWidth = width > 0 ? width : 280;
    float expressViewHeight = 0; // 自动高度

    AdSlot adSlot = new AdSlot.Builder()
      .setCodeId(codeId) // 广告位id
      .setSupportDeepLink(true)
      .setAdCount(1) // 请求广告数量为1到3条
      .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) // 期望模板广告view的size,单位dp,高度0自适应
      .setImageAcceptedSize(640, 320)
      .setNativeAdType(AdSlot.TYPE_INTERACTION_AD) // 坑啊，不设置这个，feed广告native出不来，一直差量无效，文档太烂
      .build();

    // step5:请求广告，对请求回调的广告作渲染处理
    DyADCore.TTAdSdk.loadNativeExpressAd(
      adSlot,
      new TTAdNative.NativeExpressAdListener() {
        @Override
        public void onError(int code, String message) {
          Log.d(TAG, message);
          DyADCore.feedPromise.reject("101", "feed ad error" + message);
        }

        @Override
        public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
          Log.d(TAG, "onNativeExpressAdLoad: FeedAd !!!");
          if (ads == null || ads.size() == 0) {
            return;
          }
          // 缓存加载成功的信息流广告
          DyADCore.feedAd = ads.get(0);
          DyADCore.feedPromise.resolve(true);
        }
      }
    );
  }

  /**
   * 主动看激励视频时，才检查这个权限
   */
  @ReactMethod
  public void requestPermission() {
    // step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
    DyADCore.ttAdManager.requestPermissionIfNecessary(reactAppContext);
  }
}
