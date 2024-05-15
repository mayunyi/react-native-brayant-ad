package com.brayantad.dy.service;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTClientBidding;

import java.util.Map;

public interface CSJSplashAd extends TTClientBidding {
  /**
   * 获取开屏广告
   *
   * @return
   */

  View getSplashView();

  /**
   * 获取点睛view
   * @return
   */

  View getSplashClickEyeView();

  /**
   * 获取卡片view
   *
   * @return
   */

  View getSplashCardView();

  /**
   * 得到Splash广告的交互类型
   *
   * @return 2在浏览器内打开 （普通类型）3落地页（普通类型），5:拨打电话 -1 未知类型
   */
  int getInteractionType();

  /**
   * 设置开屏广告不开启倒计时功能、不显示跳过按钮
   */
  void hideSkipButton();

  /**
   * 返回广告额外信息
   */
  Map<String, Object> getMediaExtraInfo();

  interface SplashAdListener {

    //开屏展示
    void onSplashAdShow(CSJSplashAd ad);

    //开屏点击
    void onSplashAdClick(CSJSplashAd ad);

    //开屏关闭
    void onSplashAdClose(CSJSplashAd ad, int closeType);
  }

  /**
   * 注册Splash广告的下载回调
   *
   * @param downloadListener 下载回调监听器
   */
  void setDownloadListener(TTAppDownloadListener downloadListener);

  /**
   * 注册开屏阶段回调
   *
   * @param splashAdListener
   */
  void setSplashAdListener(SplashAdListener splashAdListener);

  int[] getSplashClickEyeSizeToDp();

  /**
   * 通知sdk开始点睛动画
   */
  void startClickEye();

  interface SplashClickEyeListener {
    //通知媒体可以展示点睛
    void onSplashClickEyeReadyToShow(CSJSplashAd bean);

    //媒体点睛点击回调
    void onSplashClickEyeClick();

    //点睛关闭回调
    void onSplashClickEyeClose();
  }

  interface SplashCardListener {
    //通知媒体可以展示卡片
    void onSplashCardReadyToShow(CSJSplashAd bean);

    //媒体卡片点击回调
    void onSplashCardClick();

    //卡片关闭回调
    void onSplashCardClose();
  }

  /**
   * 注册点睛阶段回调
   * @param clickEyeListener
   */
  void setSplashClickEyeListener(SplashClickEyeListener clickEyeListener);


  /**
   * 注册卡片阶段回调
   *
   * @param cardListener
   */
  void setSplashCardListener(SplashCardListener cardListener);

  /**
   * 告知sdk展示开屏
   * @param viewGroup
   */
  void showSplashView(ViewGroup viewGroup);

  /**
   * 告知sdk展示点睛
   * @param viewGroup
   */
  void showSplashClickEyeView(ViewGroup viewGroup);

  /**
   * 告知sdk展示卡片
   * @param viewGroup
   */
  void showSplashCardView(ViewGroup viewGroup, Activity activity);

}
