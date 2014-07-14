package com.feexon.wxpay

import com.wxap.RequestHandler
import com.wxap.ResponseHandler
import com.wxap.util.Sha1Util
import com.wxap.util.TenpayUtil
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Administrator
 * @version 1.0 2014/7/14,11:20
 */
@Controller
class WXController{
    String APP_ID = "wx1ad8756ed58a657b"
    String APP_SECRET = "bfbcad0b92a3caa0753003a244716fb1"
    String PARTNER_ID = "1219186101";
    String PARTNER_KEY = "f83ef79e026f3930582d3e8d8f1d062c";
    String APP_KEY

    @RequestMapping("/feedback")
    def feedback() {
        System.out.println("request -> feedback");
        return "/ok";
    }

    def getAppId() {
        return APP_ID;
    }

    @RequestMapping("/pay")
    def pay(HttpServletRequest request, HttpServletResponse response, Model model) {
        System.out.println("request -> pay");
        String packageValue = makeWXPackage(request, response)
        String timestamp = Sha1Util.getTimeStamp();
        String nonce = Sha1Util.getNonceStr()
        model.addAttribute("packageValue", packageValue);
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("nonce", nonce);
        model.addAttribute("config", this);
        //设置支付参数
        SortedMap<String, String> signParams = new TreeMap<String, String>();
        signParams.put("appId", APP_ID);
        signParams.put("nonceStr", nonce);
        signParams.put("package", packageValue);
        signParams.put("timestamp", timestamp);

        //生成支付签名，要采用URLENCODER的原始值进行SHA1算法！
        String sign = Sha1Util.createSHA1Sign(signParams);
        model.addAttribute("sign", sign);

        //增加非参与签名的额外参数
        signParams.put("paySign", sign);
        signParams.put("signType", "sha1");
        return "/pay";
    }

    private String makeWXPackage(HttpServletRequest request, HttpServletResponse response) {
        RequestHandler reqHandler = new RequestHandler(request, response);
        //初始化
        reqHandler.init();
        reqHandler.init(APP_ID, APP_SECRET, PARTNER_KEY, APP_KEY);
        def packageParams = reqHandler.genPackage(createWXPackage(request));
        packageParams
    }

    private SortedMap createWXPackage(HttpServletRequest request) {
        SortedMap<String, String> wxPackage = new TreeMap<String, String>();
        wxPackage.put("bank_type", "WX");  //支付类型
        wxPackage.put("body", "商品名称"); //商品描述
        wxPackage.put("fee_type", "1");      //银行币种
        wxPackage.put("input_charset", "GBK"); //字符集
        wxPackage.put("notify_url", "$request.scheme://$request.serverName$request.contextPath/paid" as String);
        //通知地址
        wxPackage.put("out_trade_no", makeTradeNo()); //商户订单号
        wxPackage.put("partner", PARTNER_ID); //设置商户号
        wxPackage.put("total_fee", "1"); //商品总金额,以分为单位
        wxPackage.put("spbill_create_ip", request.getRemoteAddr()); //订单生成的机器IP，指用户浏览器端IP
        System.out.println("package:$wxPackage")
        return wxPackage;
    }

    private String makeTradeNo() {
//当前时间 yyyyMMddHHmmss
        //8位日期
        //四位随机数
        //10位序列号,可以自行调整。
        //订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
        return TenpayUtil.getCurrTime()[8..-1] + TenpayUtil.buildRandom(4);
    }

    @RequestMapping("/notification")
    def warn() {
        System.out.println("request -> warn");
        return "/ok";
    }

    @RequestMapping("/paid")
    void paid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("request -> paid");
        ResponseHandler resHandler = new ResponseHandler(request, response);
        resHandler.setKey(PARTNER_KEY);


        //商户订单号
        String out_trade_no = resHandler.getParameter("out_trade_no");
        //财付通订单号
        String transaction_id = resHandler
                .getParameter("transaction_id");
        //金额,以分为单位
        String total_fee = resHandler.getParameter("total_fee");
        //如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
        String discount = resHandler.getParameter("discount");
        //支付结果
        String trade_state = resHandler.getParameter("trade_state");
        System.out.println("params:" + resHandler.allParameters);

        //判断签名及结果
        if ("0".equals(trade_state)) {
            //------------------------------
            //即时到账处理业务开始
            //------------------------------

            //处理数据库逻辑
            //注意交易单不要重复处理
            //注意判断返回金额

            //------------------------------
            //即时到账处理业务完毕
            //------------------------------

            System.out.println("success 后台通知成功");
            //给财付通系统发送成功信息，财付通系统收到此结果后不再进行后续通知
        } else {
            System.out.println("fail 支付失败");
        }
        resHandler.sendToCFT("success");
    }


}
