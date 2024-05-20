import { NativeModules, Platform } from 'react-native';
const { AdManager } = NativeModules;

type appInfo = {
  appid: string;
  app?: string | null; //app名称
  uid?: string | null; //有些uid和穿山甲商务有合作的需要
  amount?: number | null; //奖励数量
  reward?: string | null; //奖励是啥
  debug?: boolean;
};

const init = (appInfo: appInfo): Promise<Boolean | string> => {
  return AdManager.init(appInfo);
};

type feedInfo = {
  appid: string;
  codeid: string;
  adWidth?: string;
};

const loadFeedAd = (info: feedInfo) => {
  //提前加载信息流FeedAd, 结果返回promise
  return AdManager.loadFeedAd(info);
};

const loadDrawFeedAd = (info: feedInfo) => {
  //提前加载视频刷信息流DrawFeedAd, 无返回，暂时只写完android
  if (Platform.OS === 'android') {
    return AdManager.loadDrawFeedAd(info);
  }
};

// 主动看激励视频时，才检查这个权限
const requestPermission = () => {
  AdManager.requestPermission();
};
export { init, loadFeedAd, loadDrawFeedAd, requestPermission };
