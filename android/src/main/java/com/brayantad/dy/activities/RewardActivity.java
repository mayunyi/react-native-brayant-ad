package com.brayantad.dy.activities;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.os.Bundle;

import com.brayantad.R;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;


public class RewardActivity extends Activity {
  private static final String TAG = "RewardVideo";
  private boolean adShowing = false;
  private TTRewardVideoAd adObj = null;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    //先渲染个黑色背景的view
    setContentView(R.layout.video_view);
  }

}
