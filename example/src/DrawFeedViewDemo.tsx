/**
 * @Author: 马海
 * @createdTime: 2024-05-2024/5/19 00:30
 * @description: description
 */
import React, { useEffect, useState } from 'react';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';

import { init, DrawFeedView, loadDrawFeedAd } from 'react-native-brayant-ad';

export default function App() {
  const [showDrawFeedView, setShowDrawFeedView] = useState(false);
  useEffect(() => {
    init({
      appid: '5519001',
      app: '猪猪进步',
      codeid_reward_video: '956956876',
    });

    setTimeout(() => {
      loadDrawFeedAd({
        appid: '5519001',
        codeid: '957795405',
      });
    }, 10000);
  }, []);
  return (
    <View style={styles.container}>
      {showDrawFeedView && (
        <DrawFeedView
          codeid={'957795405'}
          appid={'5519001'}
          visible={true}
          onAdError={(e: any) => {
            console.log('DrawFeedAd 加载失败', e);
          }}
          onAdShow={(e: any) => {
            console.log('DrawFeedAd 开屏开始展示', e);
          }}
          onAdClick={(e: any) => {
            console.log('onAdClick DrawFeed', e.nativeEvent);
          }}
        />
      )}
      <TouchableOpacity onPress={() => setShowDrawFeedView(!showDrawFeedView)}>
        <Text>显示/隐藏DrawFeed</Text>
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
