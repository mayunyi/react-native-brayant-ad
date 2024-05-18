package com.brayantad.dy.activities;

import static com.brayantad.dy.FullScreenVideoModule.sendEvent;

import com.brayantad.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;

import com.brayantad.dy.DyADCore;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import java.util.Objects;

public class FullScreenActivity extends Activity {
  private final String TAG = "FullScreenVideo";
  private boolean adShowing = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //先渲染个白屏
    setContentView(R.layout.full_video_view);

    DyADCore.hookActivity(this);

    // 读取 codeId
    Bundle extras = getIntent().getExtras();
      assert extras != null;
      String codeId = extras.getString("codeId");
    String orientation = extras.getString("orientation");

    if (DyADCore.TTAdSdk == null) {
      // TTAdSdk 未 init，直接跳出加载，避免导致空异常
      fireEvent("onAdError", "TTAdSdk 未 init");
      DyADCore.rewardActivity.finish();
      return;
    }

    // 有缓存的广告直接展示
    if (DyADCore.fullAd != null) {
      // Log.d(TAG, "直接展示提前加载的广告");
      showAd(DyADCore.fullAd);
      DyADCore.rewardActivity.finish(); // 关闭全屏视频广告 Activity
    } else {
      // 否则直接加载视频广告
      loadAd(
        codeId,
        orientation,
        () -> {
          showAd(DyADCore.fullAd);
        }
      );
    }
  }

  public static void loadAd(
    String codeId,
    String orientation,
    Runnable callback
  ) {
    // 创建广告请求参数 AdSlot ,具体参数含义参考文档
    AdSlot adSlot = new AdSlot.Builder()
      .setCodeId(codeId)
      .setSupportDeepLink(true)
      .setImageAcceptedSize(1080, 1920)
      .setRewardName(DyADCore.rewardName) // 奖励的名称
      .setRewardAmount(DyADCore.rewardAmount) // 奖励的数量
      .setUserID(DyADCore.userId) // 用户id,必传参数
      .setOrientation(
              Objects.equals(orientation, "HORIZONTAL")
          ? TTAdConstant.HORIZONTAL
          : TTAdConstant.VERTICAL
      ) // 必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
      .build();

    // 请求广告
    DyADCore.TTAdSdk.loadFullScreenVideoAd(
      adSlot,
      new TTAdNative.FullScreenVideoAdListener() {

        @Override
        public void onError(int code, String message) {
          //加载出错
          Log.e(
            "fullscreen",
            "Callback --> onError: " + code + ", " + String.valueOf(message)
          );
          fireEvent("onAdError", message);

          if (DyADCore.rewardActivity != null) {
            // 启动了 FullScreenActivity 的话就需要将 Activity finish 掉
            DyADCore.rewardActivity.finish();
          }
        }

        @Override
        public void onFullScreenVideoCached(TTFullScreenVideoAd ad) {
          String msg = "成功缓存全屏广告的视频";
          fireEvent("onVideoCached", msg);
        }

        @Override
        public void onFullScreenVideoCached() {}

        @Override
        public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
          String msg = "成功加载的全屏广告";
          fireEvent("onAdShow", msg);

          DyADCore.fullAd = ad;
          callback.run();
        }
      }
    );
  }

  /**
   * 展示全屏视频广告
   *
   * @param ad
   */
  private void showAd(TTFullScreenVideoAd ad) {
    if (adShowing) {
      return;
    }
    adShowing = true;

    final FullScreenActivity _this = this; // 全屏广告也必须全屏..

    if (ad == null) {
      // TToast.show(this, "广告加载错误");
      DyADCore.getRewardResult();
      return;
    }

    ad.setFullScreenVideoAdInteractionListener(
      new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

        @Override
        public void onAdShow() {
          String msg = "展示全屏视频广告";
          fireEvent("onAdShow", msg);
          DyADCore.is_show = true;
        }

        @Override
        public void onAdVideoBarClick() {
          String msg = "查看详情成功,奖励即将发放";
          fireEvent("onAdClick", msg);
          DyADCore.is_click = true;
          DyADCore.fullAd = null; // 显示广告之后将之前加载的广告清空
        }

        @Override
        public void onAdClose() {
          String msg = "全屏视频广告已关闭";
          fireEvent("onAdClose", msg);
          DyADCore.getRewardResult();
          DyADCore.fullAd = null; // 显示广告之后将之前加载的广告清空
        }

        // 视频播放完成回调
        @Override
        public void onVideoComplete() {
          String msg = "全屏视频广告播放完成";
          fireEvent("onVideoComplete", msg);
          DyADCore.is_show = true;
          DyADCore.fullAd = null; // 显示广告之后将之前加载的广告清空
        }

        @Override
        public void onSkippedVideo() {
          String msg = "跳过全屏视频广告播放";
          DyADCore.is_show = true; // 主动跳过也算看完了
          DyADCore.fullAd = null; // 显示广告之后将之前加载的广告清空

          fireEvent("onAdSkip", msg);
        }
      }
    );

    // 开始显示广告
    ad.showFullScreenVideoAd(_this);
  }

  // 二次封装发送到RN的事件函数
  public static void fireEvent(String eventName, String message) {
    WritableMap params = Arguments.createMap();
    params.putString("message", message);
    sendEvent(eventName, params);
  }
}
