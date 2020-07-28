package com.example.alipaytest

import android.annotation.SuppressLint
import com.alipay.sdk.app.EnvUtils
import com.example.alipaytest.channel.ChannelName
import com.example.alipaytest.channel.alipay.Pay
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

@Suppress("UNCHECKED_CAST")
@SuppressLint("Registered")
class MainActivity : FlutterActivity() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        GeneratedPluginRegistrant.registerWith(flutterEngine)

        // 初始化支付宝支付通道
        Pay(this@MainActivity)
                .init(flutterEngine)
    }


}