package com.example.alipaytest.channel.alipay

import android.app.Activity
import com.alipay.sdk.app.AuthTask
import com.alipay.sdk.app.EnvUtils
import com.example.alipaytest.channel.ChannelName
import com.example.alipaytest.util.OrderInfoUtil
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

@Suppress("UNCHECKED_CAST")
class Pay(private var activity: Activity) {

    private companion object {
        /**
         * 用于支付宝支付业务的入参 app_id。
         * todo 输入商户号
         */
        const val APPID = ""

        // todo 输入 private key
        const val RSA2_PRIVATE = ""
        const val RSA_PRIVATE = ""
    }

    fun init(flutterEngine: FlutterEngine) {
        // todo 生产环境下请注释掉 设置支付宝环境
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, ChannelName.API_PAY_METHOD_CHANNEL)
                .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->

                    println("method" + call.method)

                    if (call.method == "pay") {
                        this.pay()
                    } else {
                        result.notImplemented()
                    }
                }

        EventChannel(flutterEngine.dartExecutor.binaryMessenger, ChannelName.API_PAY_EVENT_CHANNEL)
                .setStreamHandler(object : EventChannel.StreamHandler {
                    override fun onListen(o: Any?, eventSink: EventChannel.EventSink) {
                        sink = eventSink
                    }

                    override fun onCancel(o: Any?) {
                        sink = null
                    }
                })
    }

    var sink: EventChannel.EventSink? = null

    private fun pay() {

        // todo 应该从服务端获取 authInfo
        val rsa2: Boolean = RSA2_PRIVATE.isNotEmpty()
        val params: Map<String, String> = OrderInfoUtil.buildOrderParamMap(APPID, rsa2)
        val privateKey: String = if (rsa2) RSA2_PRIVATE else RSA_PRIVATE
        val sign: String = OrderInfoUtil.getSign(params, privateKey, rsa2)

        val info = OrderInfoUtil.buildOrderParam(params)
        val authInfo = "$info&$sign"

        val payRunnable = Runnable {
            val authTask = AuthTask(this.activity)
            // 调用授权接口，获取授权结果
            sink?.success(authTask.authV2(authInfo, true))
        }

        Thread(payRunnable).start()
    }
}