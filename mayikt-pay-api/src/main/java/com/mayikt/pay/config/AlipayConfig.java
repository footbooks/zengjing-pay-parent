package com.mayikt.pay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "9021000133679785";

    // 商户私钥，您的PKCS8格式RSA2私钥 对应支付宝中：应用私钥:
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCPDDlJaoTr1YxAMjR8/vu69O9dPcAmT1wNFDIAZTsR3FUxuLbY4df1jWhrmkTi1kTK4yFIMbgr60fKC+FgVsV5UWWM6MPApXmljHNVymLopR8h/bAhnMxN6/d+vM/ucb83bK38VGVRDjlHvJnVxG2gVg65QAQnQzfczfVmZ2uri9zl6aQTAAGBLtoU18i4yQ76hfAdJAe27A/VvRUfdRL0ZGlzw8dbigjEaNECO3/1SmCfG1Vqhy1UoSS4uYazBRlRlftUKwYVvADPx8+qO0YhFdhbrEjTc9YzqAaZPAYBq9tviip4iUxD5jAsKGptoFUQU3Dnav/M7Cg7wrWh+u61AgMBAAECggEAcyBqZqzlB40Z0HzXg5YP8Tq5/rf5EpwiQKB1UZ4neUt0AY1hE5JIJedGF/vT3yCFwygjnB/v0uEMKQ+AtvF/AxHAgjS/fydujQ9Jr80v538lXEz5WUQBpeD4krbRPl2rgKbmLEOcOy0Qaqzevo3VkQpR1UQxU4wkxH+6Nk2b+TZlxvGFviNEkeZtNqz8jEfBrOXMpPVsHdPaIxCCcZ0YnTnTdOCyUDxAhInycCh4ftcsiufCk8qpKeGTe0YqA7gprGgmrWB4KJifbCHH6eq1wWP8jrwtG3I5kj0M/aV/LnwkWrcNStFfGIdlfF+jzPlC2KFXguiO8yDSZ3f6kNeSLQKBgQDMOo/RNiINwRIyYeZdze1GH3dyumlYOrey9L5vQ7c9abYrHTtuvbnF/tolkvnG1/zMQkkow9iRD2HHnzdAy5EeKKx6TA4nDOqyE/UsfG412N0ZtW2Vq44n0akdS0mYFfbbyMnwS0NQWNvpIGZ4z0/cTlAZohrk0GTVx19jmiHtgwKBgQCzT1ELjwGmv4oLKr30WBh/bF1Mh/Xeg8f0TNAfzY/NU/Wt8RfFr4B+fF+UqodTFjB50XR4ORBOvMbkZc2r4eZVXW2kKfXtvYfR69VCE1nfCt6Ku3/3VdqPx2rLc6qVT+liOP2uyKbCXEfTQFNf3RW67Tzehe2BpSu7sokjmQL1ZwKBgGUaaLAtmFYuyo9en+i4VUptsXh+iEBBcUeKmewDHeetOZ3GbvG3wwwdymjnhffGXUPRXN5tdfz6HHHyhCvzmvfPMvOoSylR364g6zMGtpi5r1tW0dVShKF5zbrO1rQPy5GqqS3oVH6mPtf+TjxohSSPMGBAPAgikoiylqp8V2d5AoGBAIAWyO5Y2fUkHpibj9ZcmCNuZbel6B1sRgpeVM5jKbajo3z+HN064a95aU3qA4F2+GoNFgZLvTqB4BKIJsQCO2f/tbLvxp5uJffvYAgzip+qRA8WofMtgVP0QHXVyqWe0Dk6PP3+mCF/ik9+RBDW/g7qKfFL+r4CH5uVjiG/zD1BAoGAcvOaeyXR3N8LYlcKkRDtjOuE/lv+aB7ex99snMotb7e38f5nm91M4NTqTGwE9AaJnX1CBJqngbIX2oAiR+i1iOSp//tqJSYkEtR4lNpzwigtQBC1czdOVsB/o0sycUlFgbzI9YdIa7dxoHoCkQMo9UOIxeL2Ma9u9JT0Dne5aww=";
    /**
     * 支付宝中的支付宝公钥:
     */
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjhBF4stONq0uMsh6uHh/OOxcxMpsUek6fkXKkjjWfiyCaNlyON7mBaHEYWGabwtSE90dVUxGSE8y0kWyHgZp0RyeenhGDfcBTfXifuGnDVsmT+3DE20HzpWo5ehp61cdEiaDguj5h/yFU5HI4XAXhwLPFiMfA/CjJjhWBF+T712ZficOdrU0Xlnnahp/RRK3krKukQQwNH2ijEINS/pT47Hr2UUDyIhEIVJ1uHgmPE2g6ffqj9WNLYdJwaeBpQfxX5I9D4oQRtdwVSb4JZD66i2A2Bdwb7GTxJOLhP9kE+VP4q77HsLVxgXAB+peos/hPB7pfgnDqVMcnqivoqNNBQIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://zengjing666.natapp1.cc/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://zengjing666.natapp1.cc/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";


    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

