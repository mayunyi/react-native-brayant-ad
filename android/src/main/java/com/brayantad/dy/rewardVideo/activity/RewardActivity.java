package com.brayantad.dy.rewardVideo.activity;

import static com.brayantad.dy.rewardVideo.RewardVideoModule.sendEvent;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.brayantad.R;
import com.brayantad.dy.DyADCore;
import com.brayantad.utils.RewardBundleModel;
import com.brayantad.utils.TToast;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;


public class RewardActivity extends Activity {
  private static final String TAG = "RewardVideo";
  private boolean adShowing = false;

  private boolean isEnableAdvancedReward = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //先渲染个黑色背景的view
    setContentView(R.layout.video_view);
    //关联boss处理回调
    DyADCore.hookActivity(this);
    // 读取 codeId
    Bundle extras = getIntent().getExtras();
    String codeId = extras.getString("codeId");
    if (DyADCore.TTAdSdk == null) {
      // TTAdSdk 未 init，直接跳出加载，避免导致空异常
      fireEvent("onAdError", 500, "TTAdSdk 未 init");
      DyADCore.rewardActivity.finish();
      return;
    }

    boolean sdkReady = TTAdSdk.isSdkReady();
    if(!sdkReady) {
      TTAdSdk.start(new TTAdSdk.Callback(){
        @Override
        public void success() {
          // 开始加载广告，如果已用缓存的展示过，给下次展示激励视频提前缓存广告
          loadAd(
            codeId,
            () -> {
              showAd(DyADCore.rewardAd);
            }
          );
          //有缓存的广告,并且代码位和init的相同，直接展示
          if (DyADCore.rewardAd != null && codeId.equals(DyADCore.codeid_reward_video)) {
            Log.d(TAG, "直接展示提前加载的广告");
            showAd(DyADCore.rewardAd);
          }
        }
        @Override
        public void fail(int code, String msg) {
          Log.i(TAG, "fail:  code = " + code + " msg = " + msg);
        }
      });
    } else {
      // 开始加载广告，如果已用缓存的展示过，给下次展示激励视频提前缓存广告
      loadAd(
        codeId,
        () -> {
          showAd(DyADCore.rewardAd);
        }
      );

      //有缓存的广告,并且代码位和init的相同，直接展示
      if (DyADCore.rewardAd != null && codeId.equals(DyADCore.codeid_reward_video)) {
        Log.d(TAG, "直接展示提前加载的广告");
        showAd(DyADCore.rewardAd);
      }
    }
  }

  public static void loadAd(String codeId, Runnable callback) {
    if (codeId.isEmpty()) {
      // 广告位 CodeId 未传, 抛出error
      DyADCore.getRewardResult();
      fireEvent("onAdError", 1001, "广告位 CodeId 未传");
      return;
    }
    // 创建广告请求参数 AdSlot, 具体参数含义参考文档
    AdSlot adSlot = new AdSlot.Builder()
      .setCodeId(codeId)
      .setExpressViewAcceptedSize(500, 500)
      .setRewardName(DyADCore.rewardName) // 奖励的名称
      .setRewardAmount(DyADCore.rewardAmount) // 奖励的数量
      .setUserID(DyADCore.userId) // 用户id,必传参数
      .setMediaExtra("media_extra") // 附加参数，可选
      .setOrientation(TTAdConstant.VERTICAL) // 必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
      .build();

    //FIXME:  穿山甲需要全面替换 express 模式
    // 请求广告

    DyADCore.TTAdSdk.loadRewardVideoAd(
      adSlot,
      new TTAdNative.RewardVideoAdListener() {
        @Override
        public void onError(int code, String message) {
          Log.d("reward onError ", message);
          fireEvent("onAdError", 1002, message);
          if (DyADCore.rewardActivity != null) {
            DyADCore.rewardActivity.finish();
          }
        }
        @Override
        public void onRewardVideoCached() {}

        // 视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
        @Override
        public void onRewardVideoCached(TTRewardVideoAd ad) {
          Log.d("reward Cached ", "穿山甲激励视频缓存成功");
          fireEvent("onAdVideoCached", 201, "穿山甲激励视频缓存成功");
        }
        // 视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
        @Override
        public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
          Log.d("reward AdLoad ", ad.toString());
          sendEvent("AdLoaded", null);
          fireEvent("onAdLoaded", 200, "视频广告的素材加载完毕");
          // 缓存的更新为最新加载成功的广告
          DyADCore.rewardAd = ad;
          callback.run();
        }
      }
    );
  }

  /**
   * 展示已加载好的激励视频广告
   * @param ad
   */
  private void showAd(TTRewardVideoAd ad) {
    if (adShowing) {
      return;
    }
    adShowing = true;

    // 激励视频必须全屏 activity 无法加底部...
    final RewardActivity _this = this;

    if (ad == null) {
      String msg = "头条奖励视频还没加载好,请先加载...";
      Log.d(TAG, msg);
      // TToast.show(this, msg);

      fireEvent("onAdError", 1003, msg);
      finish();
      return;
    }

    // ad.setShowDownLoadBar(false);
    ad.setRewardAdInteractionListener(
      new TTRewardVideoAd.RewardAdInteractionListener() {
        @Override
        public void onAdShow() {
          String msg = "开始展示奖励视频";
          Log.d(TAG, msg);
          fireEvent("onAdLoaded", 202, msg);
        }

        @Override
        public void onAdVideoBarClick() {
          DyADCore.is_click = true;
          String msg = "头条奖励视频查看成功,奖励即将发放";
          Log.d(TAG, msg);
          fireEvent("onAdClick", 203, msg);
        }

        @Override
        public void onAdClose() {
          DyADCore.is_close = true;
          fireEvent("onAdClose", 204, "关闭激励视频");
          DyADCore.getRewardResult();
        }

        // 视频播放完成回调
        @Override
        public void onVideoComplete() {
          DyADCore.is_show = true;
          String msg = "头条奖励视频成功播放完成";
          // TToast.show(_this, msg);
          Log.d(TAG, msg);
          fireEvent("onVideoComplete", 205, msg);
        }

        @Override
        public void onVideoError() {
          fireEvent("onAdError", 1004, "激励视频播放出错了");
          if (DyADCore.rewardActivity != null) {
            DyADCore.rewardActivity.finish();
          }
        }

        // 视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励数量，rewardName：奖励名称
        @Override
        public void onRewardVerify(
          boolean rewardVerify,
          int rewardAmount,
          String rewardName,
          int rewardAA,
          String aa
        ) {
          if (rewardVerify) {
             TToast.show(_this, "验证:成功  数量:" + rewardAmount + " 奖励:" + rewardName, Toast.LENGTH_LONG);
          } else {
             TToast.show(_this, "头条激励视频验证:" + "失败 ...", Toast.LENGTH_SHORT);
          }
          DyADCore.is_reward = true;
        }

        @Override
        public void onRewardArrived(boolean isRewardValid, int rewardType, Bundle bundle) {

          RewardBundleModel rewardBundleModel = new RewardBundleModel(bundle);
          Log.e(TAG, "Callback --> rewardVideoAd has onRewardArrived " +
            "\n奖励是否有效：" + isRewardValid +
            "\n奖励类型：" + rewardType +
            "\n奖励名称：" + rewardBundleModel.getRewardName() +
            "\n奖励数量：" + rewardBundleModel.getRewardAmount() +
            "\n建议奖励百分比：" + rewardBundleModel.getRewardPropose());
          String message = "\n奖励是否有效：" + isRewardValid +
            "\n奖励类型：" + rewardType +
            "\n奖励名称：" + rewardBundleModel.getRewardName() +
            "\n奖励数量：" + rewardBundleModel.getRewardAmount() * rewardBundleModel.getRewardPropose() +
            "\n建议奖励百分比：" + rewardBundleModel.getRewardPropose();
          // 奖励发放
          if (!isRewardValid) {
            fireEvent("onRewardArrived", 200, message);
            TToast.show(_this, "头条激励视频验证:" + "失败 ...", Toast.LENGTH_SHORT);
            return;
          }
          isEnableAdvancedReward = rewardType != 0;
          if (!isEnableAdvancedReward) {
            // 未使用进阶奖励功能
            fireEvent("onRewardArrived", 200, message);

            if (rewardType == TTRewardVideoAd.REWARD_TYPE_DEFAULT) {
              Log.d(TAG, "普通奖励发放，name:" + rewardBundleModel.getRewardName() +
                "\namount:" + rewardBundleModel.getRewardAmount());
            }
          } else {
            // 使用了进阶奖励功能
            Log.d(TAG, "进阶奖励功能，name:" + rewardBundleModel.getRewardName() +
              "\namount:" + rewardBundleModel.getRewardAmount());
          }
          DyADCore.is_reward = true;
        }

        @Override
        public void onSkippedVideo() {
          //激励视频不允许跳过...
          DyADCore.is_show = false;
        }
      }
    );

    ad.setDownloadListener(
      new TTAppDownloadListener() {

        @Override
        public void onIdle() {
          DyADCore.is_download_idle = true;
        }

        @Override
        public void onDownloadActive(
          long totalBytes,
          long currBytes,
          String fileName,
          String appName
        ) {
          if (!DyADCore.is_download_active) {
            DyADCore.is_download_active = true;
            fireEvent("onDownloadActive", 300, "下载中，点击下载区域暂停");
          }
        }

        @Override
        public void onDownloadPaused(
          long totalBytes,
          long currBytes,
          String fileName,
          String appName
        ) {
          fireEvent("onDownloadActive", 301, "下载暂停，点击下载区域继续");
        }

        @Override
        public void onDownloadFailed(
          long totalBytes,
          long currBytes,
          String fileName,
          String appName
        ) {
          fireEvent("onDownloadActive", 304, "下载失败，点击下载区域重新下载");
        }

        @Override
        public void onDownloadFinished(
          long totalBytes,
          String fileName,
          String appName
        ) {
          fireEvent("onDownloadActive", 302, "下载完成，点击下载区域重新下载");
        }

        @Override
        public void onInstalled(String fileName, String appName) {
          String msg = "安装完成，点击下载区域打开";
          Log.d(TAG, "onInstalled: " + msg);
          fireEvent("onDownloadActive", 303, msg);
          DyADCore.is_install = true;
        }
      }
    );

    // 开始显示广告,会铺满全屏...
    ad.showRewardVideoAd(
      this,
      TTAdConstant.RitScenes.CUSTOMIZE_SCENES,
      "scenes_test"
    );
  }

  // 二次封装发送到RN的事件函数
  public static void fireEvent(
    String eventName,
    int startCode,
    String message
  ) {
    WritableMap params = Arguments.createMap();
    params.putInt("code", startCode);
    params.putString("message", message);
    sendEvent(eventName, params);
  }

}
