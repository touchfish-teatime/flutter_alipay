package com.example.alipaytest.util

import SignUtils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object OrderInfoUtil {
    public fun buildAuthInfoMap(
            pid: String,
            app_id: String,
            target_id: String,
            rsa2: Boolean
    ): Map<String, String> {

        val keyValues: MutableMap<String, String> = HashMap()

        // 商户签约拿到的app_id，如：2013081700024223

        // 商户签约拿到的app_id，如：2013081700024223
        keyValues["app_id"] = app_id

        // 商户签约拿到的pid，如：2088102123816631

        // 商户签约拿到的pid，如：2088102123816631
        keyValues["pid"] = pid

        // 服务接口名称， 固定值

        // 服务接口名称， 固定值
        keyValues["apiname"] = "com.alipay.account.auth"

        // 服务接口名称， 固定值

        // 服务接口名称， 固定值
        keyValues["methodname"] = "alipay.open.auth.sdk.code.get"

        // 商户类型标识， 固定值

        // 商户类型标识， 固定值
        keyValues["app_name"] = "mc"

        // 业务类型， 固定值

        // 业务类型， 固定值
        keyValues["biz_type"] = "openservice"

        // 产品码， 固定值

        // 产品码， 固定值
        keyValues["product_id"] = "APP_FAST_LOGIN"

        // 授权范围， 固定值

        // 授权范围， 固定值
        keyValues["scope"] = "kuaijie"

        // 商户唯一标识，如：kkkkk091125

        // 商户唯一标识，如：kkkkk091125
        keyValues["target_id"] = target_id

        // 授权类型， 固定值

        // 授权类型， 固定值
        keyValues["auth_type"] = "AUTHACCOUNT"

        // 签名类型

        // 签名类型
        keyValues["sign_type"] = if (rsa2) "RSA2" else "RSA"

        return keyValues
    }

    /**
     * 构造支付订单参数列表
     */
    fun buildOrderParamMap(app_id: String, rsa2: Boolean): Map<String, String> {
        val keyValues: MutableMap<String, String> = HashMap()
        keyValues["app_id"] = app_id
        keyValues["biz_content"] = "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"0.01\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"" + getOutTradeNo() + "\"}"
        keyValues["charset"] = "utf-8"
        keyValues["method"] = "alipay.trade.app.pay"
        keyValues["sign_type"] = if (rsa2) "RSA2" else "RSA"
        keyValues["timestamp"] = "2016-07-29 16:55:53"
        keyValues["version"] = "1.0"
        return keyValues
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map
     * 支付订单参数
     * @return
     */
    fun buildOrderParam(map: Map<String, String?>): String? {
        val keys: List<String> = ArrayList(map.keys)
        val sb = StringBuilder()
        for (i in 0 until keys.size - 1) {
            val key = keys[i]
            val value = map[key]
            sb.append(buildKeyValue(key, value, true))
            sb.append("&")
        }
        val tailKey = keys[keys.size - 1]
        val tailValue = map[tailKey]
        sb.append(buildKeyValue(tailKey, tailValue, true))
        return sb.toString()
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private fun buildKeyValue(key: String, value: String?, isEncode: Boolean): String? {
        val sb = StringBuilder()
        sb.append(key)
        sb.append("=")
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                sb.append(value)
            }
        } else {
            sb.append(value)
        }
        return sb.toString()
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map
     * 待签名授权信息
     *
     * @return
     */
    fun getSign(map: Map<String, String?>, rsaKey: String?, rsa2: Boolean): String {
        val keys: List<String> = ArrayList(map.keys)
        // key排序
        Collections.sort(keys)
        val authInfo = StringBuilder()
        for (i in 0 until keys.size - 1) {
            val key = keys[i]
            val value = map[key]
            authInfo.append(buildKeyValue(key, value, false))
            authInfo.append("&")
        }
        val tailKey = keys[keys.size - 1]
        val tailValue = map[tailKey]
        authInfo.append(buildKeyValue(tailKey, tailValue, false))

        val oriSign: String? = SignUtils.sign(authInfo.toString(), rsaKey, rsa2)
        var encodedSign = ""
        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return "sign=$encodedSign"
    }

    /**
     * 要求外部订单号必须唯一。
     * @return
     */
    private fun getOutTradeNo(): String {
        val format = SimpleDateFormat("MMddHHmmss", Locale.getDefault())
        val date = Date()
        var key: String = format.format(date)
        val r = Random()
        key = key + r.nextInt()
        key = key.substring(0, 15)
        return key
    }
}