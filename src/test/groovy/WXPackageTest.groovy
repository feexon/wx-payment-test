import com.tencent.weixin.WXPackage
import org.junit.Test

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

/**
 * @author Administrator
 * @version 1.0 2014/7/14,16:44
 */
class WXPackageTest {
    public static final String PARTNER_KEY = "8934E7D15453E97507EF794CF7B0519D"

    @Test
    public void defaultValues() throws Exception {
        WXPackage _package = new WXPackage();
        assert _package.bank_type == "WX";
        assert _package.fee_type == "1";
        assert _package.input_charset == "GBK";
    }

    @Test
    public void params() throws Exception {

        def actual = testPackage().params().collect { "$it.key=$it.value" }.join('&')
        assertThat(actual, equalTo("bank_type=WX&body=支付测试&fee_type=1&input_charset=UTF-8&notify_url=http://weixin.qq.com&out_trade_no=7240b65810859cbf2a8d9f76a638c0a3&partner=1900000109&spbill_create_ip=196.168.1.1&time_start=20140714175026&total_fee=1"));
    }

    @Test
    public void signKey() throws Exception {
        assertThat(testPackage().signKey(PARTNER_KEY), equalTo("C811A73ACAEF9A6E340551FB09627231"));
    }

    private WXPackage testPackage(Map overrides = [:]) {
        return new WXPackage(
                bank_type: 'WX',
                body: '支付测试',
                fee_type: '1',
                input_charset: 'UTF-8',
                notify_url: 'http://weixin.qq.com',
                out_trade_no: '7240b65810859cbf2a8d9f76a638c0a3',
                partner: '1900000109',
                spbill_create_ip: "196.168.1.1",
                total_fee: '1',
                time_start: '20140714175026',
                *: overrides
        )
    }

    @Test
    public void sign() throws Exception {
        String expected = "bank_type=WX&body=%E6%94%AF%E4%BB%98%E6%B5%8B%E8%AF%95&fee_type=1&input_charset=UTF-8&notify_url=http%3A%2F%2Fweixin.qq.com&out_trade_no=7240b65810859cbf2a8d9f76a638c0a3&partner=1900000109&spbill_create_ip=196.168.1.1&time_start=20140714175026&total_fee=1&sign=C811A73ACAEF9A6E340551FB09627231";
        assertThat(testPackage().sign(PARTNER_KEY), equalTo(expected))
    }

    @Test
    public void "replace + with %20"() throws Exception {
        String expected = "bank_type=WX&body=%E6%94%AF%E4%BB%98%20%E6%B5%8B%E8%AF%95&fee_type=1&input_charset=UTF-8&notify_url=http%3A%2F%2Fweixin.qq.com&out_trade_no=7240b65810859cbf2a8d9f76a638c0a3&partner=1900000109&spbill_create_ip=196.168.1.1&time_start=20140714175026&total_fee=1&sign=4F1989069FB9AC659883BBFFF6DECC93";
        assertThat(testPackage(body: '支付 测试').sign(PARTNER_KEY), equalTo(expected))
    }

    @Test
    public void excludeNullProperties() throws Exception {
        assert !testPackage(time_start: null).params().containsKey("time_start")
    }


}
