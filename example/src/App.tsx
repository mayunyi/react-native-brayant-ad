import React, { useEffect } from 'react';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';

import { multiply, init, startRewardVideo } from 'react-native-brayant-ad';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  useEffect(() => {
    multiply(3, 7).then(setResult);
    init({
      appid: '5519001',
      app: '猪猪进步',
      codeid_reward_video: '956956876',
    });
  }, []);

  const onOpenScren = () => {
    
  }
  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
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
        开屏
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

          rewardVideo.subscribe('onAdLoaded', (event) => {
            console.log('广告加载成功监听', event);
          });

          rewardVideo.subscribe('onAdError', (event) => {
            console.log('广告加载失败监听', event);
          });

          rewardVideo.subscribe('onAdClose', (event) => {
            console.log('广告被关闭监听', event);
          });

          rewardVideo.subscribe('onAdClick', (event) => {
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
