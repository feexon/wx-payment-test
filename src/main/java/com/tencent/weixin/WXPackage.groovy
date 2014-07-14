package com.tencent.weixin

import com.wxap.util.MD5Util

/**
 * @author Administrator
 * @version 1.0 2014/7/14,16:44
 */
class WXPackage {
    String bank_type = "WX"
    String fee_type = "1"
    String input_charset = "GBK"
    String body
    String notify_url
    String out_trade_no
    String partner
    String spbill_create_ip
    String total_fee
    String time_start;

    String signKey(String partnerKey) {
        return MD5Util.MD5Encode(params("key=$partnerKey", { "$it.key=$it.value" }), 'UTF-8').toUpperCase();
    }

    def params() {
        return new TreeMap(this.properties.findAll { !(it.key in ['class'])&&it.value!=null});
    }

    String sign(String partnerKey) {
        return params("sign=${signKey(partnerKey)}", {
            return "$it.key=${encode(it.value)}"
        });
    }

    private String encode(String value) {
        if(value==null){
            return "";
        }
        return URLEncoder.encode(value, "UTF-8").replaceAll(/\+/, "%20")
    }

    String params(CharSequence addsOn, Closure<Map.Entry> closure) {
        return [*params().collect(closure), addsOn].join("&");
    }
}
