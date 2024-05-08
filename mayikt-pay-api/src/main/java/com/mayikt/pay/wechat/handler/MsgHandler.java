package com.mayikt.pay.wechat.handler;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mayikt.pay.wechat.builder.TextBuilder;
import com.mayikt.pay.wechat.handler.constant.Constant;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.Constants;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.mayikt.pay.wechat.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }
        //1.获取到用户输入的内容
        String content = wxMessage.getContent();
        //官方提供的测试key
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("key","free");
        hashMap.put("appid","0");
        hashMap.put("msg",content);
        try{
            String result = HttpUtil.get("http://api.qingyunke.com/api.php", hashMap);
            if (StringUtils.isEmpty(result)){
                return setResult("系统忙，请稍后重试。。。",wxMessage,weixinService);
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            Integer code = jsonObject.getInteger("result");
            if (!Constant.QINGYUNKE_RESULT_OK.equals(code)){
                return setResult("系统忙，请稍后重试。。。",wxMessage,weixinService);
            }
            String msg = jsonObject.getString("content");
            return setResult(msg,wxMessage,weixinService);
        }catch (Exception e){
            logger.info("e:{}",e);
            return setResult("系统错误，请稍后重试。。。",wxMessage,weixinService);
        }
//        //TODO 组装回复消息
//        String content = "收到信息内容：" + JsonUtils.toJson(wxMessage);
    }
    private WxMpXmlOutMessage setResult(String content, WxMpXmlMessage wxMessage,
                                        WxMpService service){
        return new TextBuilder().build(content, wxMessage, service);
    }
}
