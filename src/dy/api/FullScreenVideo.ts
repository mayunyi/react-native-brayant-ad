/**
 * @Author: 马海
 * @createdTime: 2024-05-2024/5/19 00:34
 * @description: 全屏广告
 */

import { NativeModules, NativeEventEmitter } from 'react-native';
import type { EventSubscription } from 'react-native';
const { FullScreenVideoModule } = NativeModules;

export enum AD_EVENT_TYPE {
  onAdError = 'onAdError', // 广告加载失败监听
  onAdLoaded = 'onAdLoaded', // 广告加载成功监听
  onAdClick = 'onAdClick', // 广告被点击监听
  onAdClose = 'onAdClose', // 广告关闭监听
}

interface FullScreenProps {
  codeid: string;
  orientation?: 'HORIZONTAL' | 'VERTICAL';
  provider?: '头条' | '腾讯' | '快手';
}

type ListenerCache = {
  [K in AD_EVENT_TYPE]: EventSubscription | undefined;
};

let listenerCache: ListenerCache = {} as ListenerCache;

export default (props: FullScreenProps) => {
  const { provider, codeid, orientation = 'VERTICAL' } = props;
  const eventEmitter = new NativeEventEmitter(FullScreenVideoModule);
  let result = FullScreenVideoModule.startAd({ codeid, orientation, provider });
  return {
    result,
    subscribe: (type: AD_EVENT_TYPE, callback: (event: any) => void) => {
      if (listenerCache[type]) {
        listenerCache[type]?.remove();
      } else {
        console.warn(`Listener for ${type} not found in the cache.`);
      }
      return (listenerCache[type] = eventEmitter.addListener(
        'FullScreenVideo-' + type,
        (event: any) => {
          callback(event);
        }
      ));
    },
  };
};
