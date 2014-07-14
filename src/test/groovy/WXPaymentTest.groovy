import com.tencent.weixin.WXPayment
import org.junit.Test

/**
 * @author Administrator
 * @version 1.0 2014/7/14,16:36
 */
class WXPaymentTest {
    @Test
    public void createInstance() throws Exception {
        WXPayment payment = new WXPayment(appId: "appId");
        assert payment.appId == "appId";
        assert payment.timeStamp.length() <= 32;
        assert payment.nonceStr.length() <= 32;
        assert payment.signType=="SHA1";
    }
}
