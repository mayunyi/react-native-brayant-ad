package com.brayantad.dy;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.List;

public class DrawFeedViewModule extends ReactContextBaseJavaModule {
  public static final String TAG = "DrawFeedAdModule";
  private static ReactApplicationContext mContext;
  @NonNull
  @Override
  public String getName() {
    return TAG;
  }


  public DrawFeedViewModule(ReactApplicationContext context) {
    super(context);
    mContext = context;
  }
  /**
   * 方便从RN主动预加载第一个draw feed广告，后面每个都为下一个提前缓存广告
   * （需要注意在展示弹层前才预加载）
   */
  @ReactMethod
  public void loadDrawFeedAd(ReadableMap options) {
    String codeId = options.getString("codeId");
    loadTTDrawFeedAd(codeId);
  }

  /**
   * 加载穿山甲的Draw信息流广告
   *
   * @param codeId
   */
  private static void loadTTDrawFeedAd(String codeId) {
    if (DyADCore.TTAdSdk == null) {
      Log.e(TAG, "TTAdSdk 还没初始化");
      return;
    }
    // 创建广告请求参数AdSlot,具体参数含义参考文档
    float expressViewWidth = 1080;
    float expressViewHeight = 1920;
    AdSlot adSlot = new AdSlot.Builder()
      .setCodeId(codeId)
      .setSupportDeepLink(true)
      .setImageAcceptedSize(1080, 1920)
      .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) // 期望模板广告view的size,单位dp
      .setAdCount(1) // 请求广告数量为1到3条
      .build();

    // 请求广告,对请求回调的广告作渲染处理
    DyADCore.TTAdSdk.loadExpressDrawFeedAd(
      adSlot,
      new TTAdNative.NativeExpressAdListener() {
        @Override
        public void onError(int code, String message) {
          Log.d(TAG, message);
        }

        @Override
        public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
          if (ads == null || ads.isEmpty()) {
            // TToast.show(mContext, " ad is null!");
            Log.d(TAG, "没有请求到drawfeed广告");
            return;
          }
          //成功加载到drawfeed广告 缓存
          DyADCore.drawfeedAd = ads.get(0);
        }
      }
    );
  }

}
