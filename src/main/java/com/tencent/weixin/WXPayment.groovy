package com.tencent.weixin

import com.wxap.util.Sha1Util

/**
 * @author Administrator
 * @version 1.0 2014/7/14,16:37
 */
class WXPayment {
    String appId;
    final String timeStamp
    final String nonceStr
    final String signType = "SHA1"

    WXPayment() {
        timeStamp = Sha1Util.timeStamp;
        nonceStr = Sha1Util.nonceStr;
    }
}
