//package com.mayikt.pay.interceptor;
//
//import com.alibaba.fastjson.JSONObject;
//import com.mayikt.pay.config.BodyReaderWrapper;
//import com.mayikt.pay.entity.MerchantSecretKeyDO;
//import com.mayikt.pay.service.MerchantSecretKeyService;
//import com.mayikt.pay.utils.SignUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
///**
// * @author 余胜军
// * @ClassName InterceptionVerification
// */
//@Component
//@Slf4j
//public class InterceptionVerification implements HandlerInterceptor {
//    @Autowired
//    private MerchantSecretKeyService merchantSecretKeyService;
//
//    /**
//     * 验证请求
//     *
//     * @param request
//     * @param response
//     * @param handler
//     * @return
//     * @throws Exception
//     */
//    @Override
//    //拦截所有的请求
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 1.获取json参数
//        String parametersStr = getParameters(request);
//        if (parametersStr == null) {
//            setResponseError(response, "parametersStr is null");
//            return Boolean.FALSE;
//        }
//        JSONObject parameters = JSONObject.parseObject(parametersStr);
//        if (parameters == null) {
//            setResponseError(response, "parameters is null");
//            return Boolean.FALSE;
//        }
//        // 2.验证key是否在有效期范围内
//        String appId = request.getHeader("appId");
//        if (StringUtils.isEmpty(appId)) {
//            setResponseError(response, "appId is null");
//            return Boolean.FALSE;
//        }
//        String appKey = request.getHeader("appKey");
//        if (StringUtils.isEmpty(appKey)) {
//            setResponseError(response, "appKey is null");
//            return Boolean.FALSE;
//        }
//        // 根据appid +秘钥查询数据
//        MerchantSecretKeyDO merchantSecretKeyDO = merchantSecretKeyService.getAppIdKeyMerchantSecret(appId, appKey);
//        if (merchantSecretKeyDO == null) {
//            setResponseError(response, "appid或者appkey错误");
//            return Boolean.FALSE;
//        }
//        // 3.验证参数签名
//        String sign = request.getParameter("sign");
//        if (StringUtils.isEmpty(sign)) {
//            setResponseError(response, "sign is null");
//            return Boolean.FALSE;
//        }
//        String timestamp = request.getParameter("timestamp");
//        if (StringUtils.isEmpty(sign)) {
//            setResponseError(response, "timestamp is null");
//            return Boolean.FALSE;
//        }
//        String saltKey = merchantSecretKeyDO.getSaltKey();
//        boolean verifyJsonResult = SignUtil.verifyJson(sign, timestamp, parameters.toJSONString(),saltKey);
//        if (!verifyJsonResult) {
//            log.error("<验证签名参数失败>");
//            return Boolean.FALSE;
//        }
//        //验证是否允许访问该权限
//        String servletPath = request.getServletPath();
//        String permissionList = merchantSecretKeyDO.getPermissionList();
//        boolean result = existsPermission(servletPath, permissionList);
//        if (!result){
//            setResponseError(response,"权限不足");
//            return Boolean.FALSE;
//        }
//        // 4.验证签名通过 则放行可以执行
//        return Boolean.TRUE;
//    }
//
//    //获取参数
//    public static String getParameters(HttpServletRequest request) throws IOException {
//        BodyReaderWrapper apiSignRequestWrapper = new BodyReaderWrapper(request);
//        BufferedReader streamReader = new BufferedReader(new InputStreamReader(apiSignRequestWrapper.getInputStream(), "UTF-8"));
//        StringBuilder responseStrBuilder = new StringBuilder();
//        String inputStr;
//        while ((inputStr = streamReader.readLine()) != null)
//            responseStrBuilder.append(inputStr);
//        return responseStrBuilder.toString();
//    }
//
//    public void setResponse(HttpServletResponse response,Integer code, String msg) throws IOException {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        JSONObject result = new JSONObject();
//        result.put("code",code);
//        result.put("msg",msg);
//        response.getWriter().println(result.toJSONString());
//        response.getWriter().flush();
//    }
//
//    public void setResponseOK(HttpServletResponse response,String msg) throws IOException {
//        setResponse(response,200,msg);
//    }
//    public void setResponseError(HttpServletResponse response,String msg) throws IOException {
//        setResponse(response,500,msg);
//    }
//
//    /**
//     * 判断是否存在该权限
//     *
//     * @return
//     */
//    private boolean existsPermission(String servletPath,
//                                     String permissionList) {
//        String[] permissionUrls = permissionList.split(",");
//        for (String permissionUrl :
//                permissionUrls) {
//            if (permissionUrl.equals(servletPath)) {
//                return Boolean.TRUE;
//            }
//
//        }
//        return Boolean.FALSE;
//    }
//
//}