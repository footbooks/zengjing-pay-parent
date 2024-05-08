package com.mayikt.pay.template.impl;

import com.mayikt.pay.template.AbstractPayCallbackTemplate;
import org.springframework.stereotype.Component;

@Component
public class WeChatPayCallbackTemplateImpl extends AbstractPayCallbackTemplate {
    @Override
    protected String asyncService() {
        return "微信回调";
    }

    @Override
    protected String resultOK() {
        return "ok";
    }

    @Override
    protected String resultFail() {
        return "fail";
    }

    @Override
    protected boolean verifySignature() {
        return Boolean.TRUE;
    }
}
