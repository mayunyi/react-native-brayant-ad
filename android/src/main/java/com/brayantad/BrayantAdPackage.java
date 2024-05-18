package com.brayantad;

import androidx.annotation.NonNull;

import com.brayantad.dy.DrawFeedViewManager;
import com.brayantad.dy.DrawFeedViewModule;
import com.brayantad.dy.FullScreenVideoModule;
import com.brayantad.dy.RewardVideoModule;
import com.brayantad.dy.SplashAdModule;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BrayantAdPackage implements ReactPackage {
  @NonNull
  @Override
  public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
    List<NativeModule> modules = new ArrayList<>();
    modules.add(new BrayantAdModule(reactContext));
    modules.add(new AdManager(reactContext));
    modules.add(new SplashAdModule(reactContext));
    modules.add(new RewardVideoModule(reactContext));
    modules.add(new FullScreenVideoModule(reactContext));
    modules.add(new DrawFeedViewModule(reactContext));
    return modules;
  }

  @NonNull
  @Override
  public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
    return Arrays.<ViewManager>asList(
      new DrawFeedViewManager()
    );
//    return Collections.emptyList();
  }
}
