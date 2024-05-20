import React, { useEffect, useState } from 'react';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';
import {
  init,
  startRewardVideo,
  requestPermission,
  dyLoadSplashAd,
  startFullScreenVideo,
  FeedAdView,
} from 'react-native-brayant-ad';

export default function App() {
  const [showFeedView, setShowFeedView] = useState(false);
  useEffect(() => {
    init({
      appid: '5519001',
      app: '猪猪进步',
    })
      .then((res) => {
        console.log(res);
        setShowFeedView(true);
        requestPermission();
      })
      .catch((e) => {
        console.log(e);
      });
  }, []);
  // 开屏广告
  const onOpenScren = () => {
    const splashAd = dyLoadSplashAd({
      codeid: '889272631',
      anim: 'default',
    });

    splashAd.subscribe('onAdClose', (event) => {
      console.log('广告关闭', event);
    });

    splashAd.subscribe('onAdSkip', (i) => {
      console.log('用户点击跳过监听', i);
    });

    splashAd.subscribe('onAdError', (e) => {
      console.log('开屏加载失败监听', e);
    });

    splashAd.subscribe('onAdClick', (e) => {
      console.log('开屏被用户点击了', e);
    });

    splashAd.subscribe('onAdShow', (e) => {
      console.log('开屏开始展示', e);
    });
  };
  return (
    <View style={styles.container}>
      <FeedAdView
        codeid={'957782005'}
        adWidth={400}
        visible={showFeedView}
        onAdLayout={(data: any) => {
          console.log('Feed 广告加载成功！', data);
        }}
        onAdClose={(data: any) => {
          console.log('Feed 广告关闭！', data);
        }}
        onAdError={(err: any) => {
          console.log('Feed 广告加载失败！', err);
        }}
        onAdClick={(val: any) => {
          console.log('Feed 广告被用户点击！', val);
        }}
      />
      <TouchableOpacity
        style={{
          marginVertical: 20,
          paddingHorizontal: 30,
          paddingVertical: 15,
          backgroundColor: '#F96',
          borderRadius: 50,
        }}
        onPress={onOpenScren}
      >
        <Text style={{ textAlign: 'center' }}> 开屏</Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={{
          marginVertical: 20,
          paddingHorizontal: 30,
          paddingVertical: 15,
          backgroundColor: '#F96',
          borderRadius: 50,
        }}
        onPress={() => {
          let fullVideo = startFullScreenVideo({
            codeid: '957781965',
          });
          console.log('FullVideoAd rs:', fullVideo);
          fullVideo.result?.then((val: any) => {
            console.log('FullVideoAd rs then val', val);
          });

          fullVideo.subscribe('onAdLoaded' as any, (event) => {
            console.log('广告加载成功监听', event);
          });

          fullVideo.subscribe('onAdError' as any, (event) => {
            console.log('广告加载失败监听', event);
          });

          fullVideo.subscribe('onAdClose' as any, (event) => {
            console.log('广告被关闭监听', event);
          });

          fullVideo.subscribe('onAdClick' as any, (event) => {
            console.log('广告点击查看详情监听', event);
          });
        }}
      >
        <Text style={{ textAlign: 'center' }}> Start 全屏视频广告</Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={{
          marginVertical: 20,
          paddingHorizontal: 30,
          paddingVertical: 15,
          backgroundColor: '#F96',
          borderRadius: 50,
        }}
        onPress={() => {
          const rewardVideo = startRewardVideo({
            codeid: '956956876',
          });

          rewardVideo.result.then((val: any) => {
            console.log('RewardVideo 回调结果', val);
          });

          rewardVideo.subscribe('onAdLoaded' as any, (event) => {
            console.log('广告加载成功监听', event);
          });

          rewardVideo.subscribe('onAdError' as any, (event) => {
            console.log('广告加载失败监听', event);
          });

          rewardVideo.subscribe('onAdClose' as any, (event) => {
            console.log('广告被关闭监听', event);
          });

          rewardVideo.subscribe('onAdClick' as any, (event) => {
            console.log('广告点击查看详情监听', event);
          });
        }}
      >
        <Text style={{ textAlign: 'center' }}> Start RewardVideoAd</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
