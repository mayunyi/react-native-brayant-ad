/**
 * @Author: 马海
 * @createdTime: 2024-05-2024/5/18 13:39
 * @description: description
 */
import React from 'react';
import {
  type ViewStyle,
  UIManager,
  StyleSheet,
  NativeModules,
  Platform,
  requireNativeComponent,
} from 'react-native';
const { DrawFeedAdModule } = NativeModules;

const LINKING_ERROR =
  `The package 'react-native-view' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ComponentName = 'DrawFeedAdViewManager';

type DrawFeedAdProps = {
  codeid: string;
  appid: string;
  visible?: boolean;
  style?: ViewStyle;
  onAdError?: Function;
  onAdShow?: Function;
  onAdClick?: Function;
};

type ViewProps = Omit<DrawFeedAdProps, 'appid'>;

export const loadDrawFeedAd = (info: { appid: string; codeid: string }) => {
  DrawFeedAdModule.loadDrawFeedAd(info);
};

export const DrawFeedView = (props: DrawFeedAdProps) => {
  const {
    codeid,
    onAdError,
    onAdShow,
    onAdClick,
    visible = true,
    style,
  } = props;
  if (!visible) return null;

  const styleObj = style ? style : styles.container;
  const ViewView =
    UIManager.getViewManagerConfig(ComponentName) != null
      ? requireNativeComponent<ViewProps>(ComponentName)
      : () => {
          throw new Error(LINKING_ERROR);
        };

  return (
    <ViewView
      codeid={codeid}
      onAdError={(e: any) => {
        console.log('onAdError DrawFeed', e.nativeEvent);
        onAdError && onAdError(e.nativeEvent);
      }}
      onAdClick={(e: any) => {
        console.log('onAdClick DrawFeed', e.nativeEvent);
        onAdClick && onAdClick(e.nativeEvent);
      }}
      onAdShow={(e: any) => {
        console.log('onAdShow DrawFeed', e.nativeEvent);
        onAdShow && onAdShow(e.nativeEvent);
      }}
      style={{ ...styleObj }}
    />
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: '100%',
  },
});

export default DrawFeedView;
