### flutter 支付宝支付 demo（安卓 only now）

##### 一、 相关账号申请流程
1. 入驻平台  [入驻平台](https://opendocs.alipay.com/open/00hqwq)

2. 创建应用 [创建应用](https://opendocs.alipay.com/open/200/105310) 

  创建应用时，需要上传密钥。可以使用阿里提供的生成工具进行生成 [密钥生成工具](https://opendocs.alipay.com/open/291/105971)。生成的密钥将作为调用 alipay sdk 的参数。

3. 绑定应用 [绑定应用](https://opendocs.alipay.com/open/0128wr)

##### 二、flutter 端代码设计思路

由于 alipay sdk 没有提供官方的 flutter 插件，所以需要自行造轮子。主要实现两个功能：

1. flutter 端调用 Alipay sdk 调出支付页面
2. 支付完成后 flutter 可以接受支付回调，自定义处理方式

代码设计思路 : 

1. alipay sdk 提供调起支付宝功能
2. 通过 MethodChannel 实现 flutter 调用 alipay sdk 方法实现支付窗口调起
3. 通过 EventChannel 实现 flutter 监听原生代码支付结果

##### 三、安卓原生代码

1. 添加aar 到 android/app/ 目录下

![添加 aar](https://user-gold-cdn.xitu.io/2020/7/29/1739a01010a01c31?w=892&h=756&f=png&s=68215)

2. android/build.gradle 文件中添加如下配置

   ```java
   ...
   allprojects {
       repositories {
   
           flatDir {
               dirs 'libs'
           }
           
   				...
       }
   }
   ...
   ```

3. android/app/build.gradle 文件中添加如下配置

   ```java
   ... 
   dependencies {
       implementation(name: 'alipaySdk-15.7.7-20200702160044', ext: 'aar')
       ...
   }
   ```

4. 添加 MethodChannel EventChannel 代码，参考 com.example.alipaytest.channel.alipay.Pay.kt

##### 四、 iOS原生代码

todo



##### 五、flutter 调用

```dart
class AliPay {
  static const _API_PAY_METHOD_CHANNEL =
      'com.sendroids.alipaytest/alipay.method';
  static const _API_PAY_EVENT_CHANNEL = 'com.sendroids.alipaytest/alipay.event';

  static const _PAY_METHOD_NAME = 'pay';

  static const _platform = const MethodChannel(_API_PAY_METHOD_CHANNEL);
  static const _eventChannel = const EventChannel(_API_PAY_EVENT_CHANNEL);

  static void pay() {
    _platform.invokeMethod(_PAY_METHOD_NAME);
  }

  static void getPayResult(Function(dynamic data) function) {
    _eventChannel.receiveBroadcastStream().listen((event) {
      function.call(event);
    });
  }
}


// 调用示例
	void _incrementCounter() {
    // 先监听事件
    AliPay.getPayResult((data) => {print(data)});
    // 调用Alipay sdk
    AliPay.pay();
  }
```