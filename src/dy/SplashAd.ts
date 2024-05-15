/**
 * @Author: 马海
 * @createdTime: 2024-05-2024/5/15 22:51
 * @description: 开屏广告
 */
import { NativeModules, NativeEventEmitter } from 'react-native';
const { SplashAd } = NativeModules;

export interface AD_EVENT_TYPE {
  onAdError: string; // 广告加载失败监听
  onAdClick: string; // 广告被点击监听
  onAdClose: string; // 广告关闭
  onAdSkip: string; // 用户点击跳过广告监听
  onAdShow: string; // 开屏广告开始展示
}

export interface SPLASHAD_PROPS_TYPE {
  codeid: string;
  anim?: 'default' | 'none' | 'catalyst' | 'slide' | 'fade';
}

const listenerCache: any = {};

const dyLoadSplashAd = ({ codeid, anim = 'default' }: SPLASHAD_PROPS_TYPE) => {
  const eventEmitter = new NativeEventEmitter(SplashAd);
  let result = SplashAd.loadSplashAd({ codeid, anim });
  return {
    result,
    subscribe: (type: keyof AD_EVENT_TYPE, callback: (event: any) => void) => {
      if (listenerCache[type]) {
        listenerCache[type].remove();
      }
      return (listenerCache[type] = eventEmitter.addListener(
        'SplashAd-' + type,
        (event: any) => {
          console.log('SplashAd event type ', type);
          console.log('SplashAd event ', event);
          callback(event);
        }
      ));
    },
  };
};
export { dyLoadSplashAd };
