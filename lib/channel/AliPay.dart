import 'package:flutter/services.dart';

class AliPay {

  static const _API_PAY_METHOD_CHANNEL = 'com.sendroids.alipaytest/alipay.method';
  static const _API_PAY_EVENT_CHANNEL = 'com.sendroids.alipaytest/alipay.event';

  static const _PAY_METHOD_NAME = 'pay';

  static const _platform = const MethodChannel(_API_PAY_METHOD_CHANNEL);
  static const _eventChannel = const EventChannel(_API_PAY_EVENT_CHANNEL);

  static void pay() {
    _platform.invokeMethod(_PAY_METHOD_NAME);
  }

  static void getPayResult() {
    _eventChannel.receiveBroadcastStream().listen((event) {
      print("It is Flutter -  receiveBroadcastStream $event");
    });
  }
}
