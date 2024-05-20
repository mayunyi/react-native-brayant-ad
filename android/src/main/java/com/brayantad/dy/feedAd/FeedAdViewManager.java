package com.brayantad.dy.feedAd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactContext;

import com.brayantad.dy.feedAd.view.FeedAdView;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class FeedAdViewManager extends ViewGroupManager<FeedAdView> {
  public static final String TAG = "FeedAdViewManager";
  private ReactContext mContext;

  @NonNull
  @Override
  public String getName() {
    return TAG;
  }

  @NonNull
  @Override
  protected FeedAdView createViewInstance(@NonNull ThemedReactContext themedReactContext) {
    return new FeedAdView(themedReactContext);
  }

  @Override
  public void removeAllViews(FeedAdView parent) {
    super.removeAllViews(parent);
  }

  @Override
  public boolean needsCustomLayoutForChildren() {
    return true;
  }

  @ReactProp(name = "codeid")
  public void setCodeId(FeedAdView view, @Nullable String codeid) {
    view.setCodeId(codeid);
  }

  @ReactProp(name = "adWidth")
  public void setAdWidth(FeedAdView view, int adWidth) {
    view.setWidth(adWidth);
  }

  @Override
  public Map getExportedCustomBubblingEventTypeConstants() {
    return MapBuilder
      .builder()
      .put("onAdClick", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onAdClick")))
      .put("onAdError", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onAdError")))
      .put("onAdClose", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onAdClose")))
      .put("onAdLayout", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onAdLayout")))
      .build();
  }
}
