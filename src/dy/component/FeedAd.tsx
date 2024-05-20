/**
 * @Author: 马海
 * @createdTime: 2024-05-2024/5/20 22:00
 * @description: description
 */
import React, { useState } from 'react';
import { Platform, requireNativeComponent, UIManager } from 'react-native';
import type { ViewStyle } from 'react-native';
const ComponentName = 'FeedAdViewManager';

export interface FeedAdProps {
  codeid: string;
  style?: ViewStyle;
  adWidth?: number;
  visible?: boolean;
  onAdLayout?: Function;
  onAdError?: Function;
  onAdClose?: Function;
  onAdClick?: Function;
}

const LINKING_ERROR =
  `The package 'react-native-view' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const FeedAdView = (props: FeedAdProps) => {
  const {
    codeid,
    style,
    adWidth = 375,
    onAdLayout,
    onAdError,
    onAdClose,
    onAdClick,
    visible = true,
  } = props;

  const [closed, setClosed] = useState(false);
  const [height, setHeight] = useState(0);

  // FeedAd是否显示，外部和内部均可控制，外部visible、内部closed
  if (!visible || closed) return null;

  const FeedAdComponent =
    UIManager.getViewManagerConfig(ComponentName) != null
      ? requireNativeComponent<FeedAdProps>(ComponentName)
      : () => {
          throw new Error(LINKING_ERROR);
        };
  return (
    <FeedAdComponent
      codeid={codeid}
      // 里面素材的宽度，减30是有些情况下，里面素材过宽贴边显示不全
      adWidth={adWidth - 30}
      // 为了不影响广告宽度占满屏幕的情况，style的width可单独控制
      style={{ width: adWidth, height, ...style }}
      onAdError={(e: any) => {
        onAdError && onAdError(e.nativeEvent);
      }}
      onAdClick={(e: any) => {
        onAdClick && onAdClick(e.nativeEvent);
      }}
      onAdClose={(e: any) => {
        setClosed(true);
        onAdClose && onAdClose(e.nativeEvent);
      }}
      onAdLayout={(e: any) => {
        if (e.nativeEvent.height) {
          setHeight(e.nativeEvent.height + 10);
          onAdLayout && onAdLayout(e.nativeEvent);
        }
      }}
    />
  );
};
export default FeedAdView;
