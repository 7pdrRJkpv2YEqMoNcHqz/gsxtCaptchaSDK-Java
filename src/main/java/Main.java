import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.jndi.toolkit.url.Uri;
import com.yuduan.geetest.dto.Code;
import com.yuduan.geetest.dto.GeetestInput;
import com.yuduan.geetest.dto.GeetestResult;
import com.yuduan.geetest.service.Geetest;
/**
 * Created by aw on 2018/5/31.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        //三代汉字
        String resp = HttpClient.get("http://www.geetest.com/demo/gt/register-click?t=1527475267971");
        //三代滑块
         //String resp = HttpClient.get("http://www.geetest.com/demo/gt/register-slide?t=1527479423500");
        //二代滑块
        //String resp = HttpClient.get("http://id.ourgame.com/id.ourgame.com/captcha!lzCaptcha.do?t=0.6296260015670256");

        JSONObject json = JSON.parseObject(resp);
        String gt = json.get("gt").toString();
        String challenge = json.get("challenge").toString();

        GeetestInput geetestInput = new GeetestInput();
        geetestInput.setChallenge(challenge);
        geetestInput.setGt(gt);
        geetestInput.setUser("");
        geetestInput.setReferer(new Uri("http://www.gsxt.gov.cn/"));

        GeetestResult result = Geetest.recognition(geetestInput);
        if (result.getCode().equals(Code.Success.getValue())) {
            //识别成功
            System.out.println("challenge:" + result.getChallenge());
            System.out.println("validate:" + result.getValidate());
            System.out.println("seccode:" + result.getSecCode());
            System.out.println("params:" + result);
        } else if (result.getCode().equals(Code.InMaintenance)) {
            //系统维护，后续逻辑
        } else if (result.getCode().equals(Code.BadIp)) {
            //IP被极验封了，该IP短时间内（预计封1-3个小时）不可用于极验3代。此处将该IP加入黑名单
        } else if (result.getCode().equals(Code.UserError)) {
            //帐号到期，触发告警通知，提示财务或负责人
        } else {
            //常规错误，记录SDK返回的错误描述
            System.out.println(result.getMessage());
        }
    }
}
