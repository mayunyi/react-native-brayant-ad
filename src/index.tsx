import { NativeModules, Platform } from 'react-native';
import { init, loadFeedAd, requestPermission } from './dy/api/AdManager';
import startRewardVideo from './dy/api/RewardVideo';
import { dyLoadSplashAd } from './dy/api/SplashAd';
import { DrawFeedView, loadDrawFeedAd } from './dy/component/DrawFeedAd';
import FeedAdView from './dy/component/FeedAd';

import startFullScreenVideo from './dy/api/FullScreenVideo';
const LINKING_ERROR =
  `The package 'react-native-brayant-ad' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const BrayantAd = NativeModules.BrayantAd
  ? NativeModules.BrayantAd
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return BrayantAd.multiply(a, b);
}
export {
  init,
  loadFeedAd,
  requestPermission,
  loadDrawFeedAd,
  startRewardVideo,
  startFullScreenVideo,
  dyLoadSplashAd,
  DrawFeedView,
  FeedAdView
};
