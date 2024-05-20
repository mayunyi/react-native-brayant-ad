# react-native-brayant-ad

接入穿山甲SDK

## 后期代办
接入GeoMoreSdk

## 安装

```sh
npm install react-native-brayant-ad
```

## 必要配置
在项目入口文件中初始化init, 如果不是全局初始化的就需要在每次调用的时候传入
```js
import { init } from 'react-native-brayant-ad';

useEfect(() => {
  init({
    appid: "xxxx",
    app: "app名称",
    amount: 1000,
    reward: "金币",
    debug: true
  }).then((res) => {})
})

```
### init 方法配置

| 参数 | 说明                | 类型                             | 默认值      | 是否必填 |
| --- |-------------------|--------------------------------|----------|------|
| appid | 穿山甲中创建应用的appid    | string                         | -        | 是    |
| app | app名称             | string                         | 穿山甲媒体APP | 否    |
| uid | 有些uid和穿山甲商务有合作的需要 | string              | -        |   否   |
| amount | 奖励数量              | number | 1000     |   否   |
| reward | 奖励名称              | string                         | 金币       |   否   |
| debug | 是否是开发者模式          | boolean                        | false    |   否   |

init 成功会返回一个promise

# 1. 开屏广告
## API
### dyLoadSplashAd

#### 开屏广告事件类型
```ts
interface AD_EVENT_TYPE {
  onAdError: string; // 广告加载失败监听
  onAdClick: string; // 广告被点击监听
  onAdClose: string; // 广告关闭
  onAdSkip: string; // 用户点击跳过广告监听
  onAdShow: string; // 开屏广告开始展示
}

EmuAnim = 'default' | 'none' | 'catalyst' | 'slide' | 'fade';

```


| 参数 | 说明     | 类型                             | 默认值      | 是否必填 |
| --- |--------|--------------------------------|----------|------|
| codeid | 广告位id  | string                         | -        | 是    |
| anim | 广告进入方式 | EmuAnim                         | default | 否    |

## 如何使用

> 这边案列默认全部init初始化后
```tsx
import { dyLoadSplashAd } from 'react-native-brayant-ad';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';

const ScrenPage = () => {
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

  render(){
    return (
      <TouchableOpacity onPress={onOpenScren}>
        <Text style={{ textAlign: 'center' }}> 开屏</Text>
      </TouchableOpacity>
    )
  }

}

```

# 2. 激励视频
## API
### requestPermission
> 主动看激励视频时，才检查这个权限

无参数 ``requestPermission()``

### startRewardVideo 方法参数
> 开始看激励视频
## API
| 参数 | 说明     | 类型                             | 默认值      | 是否必填 |
| --- |--------|--------------------------------|----------|------|
| codeid | 广告位id  | string                         | -        | 是    |

#### 激励视频事件类型
```ts
export enum AD_EVENT_TYPE {
  onAdError = 'onAdError', // 广告加载失败监听
  onAdLoaded = 'onAdLoaded', // 广告加载成功监听
  onAdClick = 'onAdClick', // 广告被点击监听
  onAdClose = 'onAdClose', // 广告关闭监听
}

```
## 如何使用

> 这边案列默认全部init初始化后
```tsx
import { requestPermission, startRewardVideo } from 'react-native-brayant-ad';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';

const RewardVideoPage = () => {

  const onStartRewardVideo = () => {
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
  };

  render(){
    return (
      <TouchableOpacity onPress={onOpenScren}>
        <Text style={{ textAlign: 'center' }}> 开屏</Text>
      </TouchableOpacity>
    )
  }

}

```
# 3. 全屏视频广告
## api
### startFullScreenVideo 方法参数

| 参数          | 说明    | 类型            | 默认值      | 是否必填 |
|-------------|-------|---------------|----------|------|
| codeid      | 广告位id | string        | -        | 是    |
| orientation | 竖屏横屏 | 'HORIZONTAL'  \| 'VERTICAL' | VERTICAL    | 否                         | -        | 是    |

## 使用
```tsx
import { requestPermission, startRewardVideo } from 'react-native-brayant-ad';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';

const RewardVideoPage = () => {
  useEffect(() => {
    // step 1: 获取权限
    requestPermission()
  }, []);

  render() {
    return (
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
    )
  }

}
```

# 4. Draw广告
## api
### loadDrawFeedAd 方法参数

| 参数          | 说明    | 类型      | 默认值 | 是否必填 |
|-------------|-------|---------|-----|------|
| codeid      | 广告位id | string  | -   | 是    |
| appid | 应用id  | string  | -   | 是                         | -        | 是    |

## 组件
### DrawFeedView
| 参数          | 说明        | 类型     | 默认值 | 是否必填 |
|-------------|-----------|--------|-----|------|
| codeid      | 广告位id     | string | -   | 是    |
| appid | 应用id      | string | -   | 是    |
| visible      | 是否显示组件中广告 | boolean | -   | 否    |
| appid | 应用id      | string | -   | 是    |
| style      | 组件样式      | ViewStyle | -   | 否    |
| onAdError | 广告错误事件    | Function | -   | 否    |
| onAdShow      | 显示广告事件    | Function | -   | 否    |
| onAdClick | 点击广告事件    | Function | -   | 否    |
## 使用
```tsx
import { loadDrawFeedAd, DrawFeedView } from 'react-native-brayant-ad';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';

const RewardVideoPage = () => {
  useEffect(() => {
    loadDrawFeedAd({
      appid: '****',
      codeid: '****',
    });
  }, []);

  render() {
    return (
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
    )
  }

}
```

# 5. 信息流广告
## 组件
### FeedAdView
| 参数          | 说明        | 类型        | 默认值 | 是否必填 |
|-------------|-----------|-----------|-----|------|
| codeid      | 广告位id     | string    | -   | 是    |
| adWidth | 广告宽度      | number    | 375 | 否    |
| visible      | 是否显示组件中广告 | boolean   | -   | 否    |
| style      | 组件样式      | ViewStyle | -   | 否    |
| onAdLayout | 广告加载成功事件  | Function    | -   | 否    |
| onAdClose | 广告关闭事件    | Function  | -   | 否    |
| onAdClick | 广告被用户点击事件    | Function  | -   | 否    |
| onAdError | 广告加载失败事件    | Function  | -   | 否    |

## 使用
```tsx
import { FeedAdView } from 'react-native-brayant-ad';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';

const RewardVideoPage = () => {
  useEffect(() => {

  }, []);

  render() {
    return (
      <FeedAdView
        codeid={'****'}
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
    )
  }

}
```
## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
