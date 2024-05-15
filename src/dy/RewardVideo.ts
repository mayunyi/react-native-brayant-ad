import { NativeModules, NativeEventEmitter } from 'react-native';
import type { EventSubscription } from 'react-native';
const { RewardVideoModule } = NativeModules;
export enum AD_EVENT_TYPE {
  onAdError = 'onAdError', // 广告加载失败监听
  onAdLoaded = 'onAdLoaded', // 广告加载成功监听
  onAdClick = 'onAdClick', // 广告被点击监听
  onAdClose = 'onAdClose', // 广告关闭监听
}

type ListenerCache = {
  [K in AD_EVENT_TYPE]: EventSubscription | undefined;
};

let listenerCache: ListenerCache = {} as ListenerCache;

type rewardInfo = {
  codeid: string;
  app?: string; //头条穿山甲参数appName (目前安卓可用，可选)
  uid?: string; //头条穿山甲参数userId (目前安卓可用，可选);
  reward?: string; //头条穿山甲参数rewardName (目前安卓可用，可选)
  amount?: number; //头条穿山甲参数rewardAmount (目前安卓可用，可选)
  provider?: '头条' | '百度' | '腾讯' | '快手';
};

export default function (info: rewardInfo) {
  const eventEmitter = new NativeEventEmitter(RewardVideoModule);
  let result = RewardVideoModule.startAd(info);
  return {
    result,
    subscribe: (type: AD_EVENT_TYPE, callback: (event: any) => void) => {
      if (listenerCache[type]) {
        listenerCache[type]?.remove();
      } else {
        console.warn(`Listener for ${type} not found in the cache.`);
      }
      return (listenerCache[type] = eventEmitter.addListener(
        'RewardVideo-' + type,
        (event: any) => {
          callback(event);
        }
      ));
    },
  };
}
