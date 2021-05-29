package com.tuotuo.demo.util;

import com.alibaba.fastjson.JSON;
import com.tuotuo.demo.req.TuoTuoManageReq;
import com.tuotuo.demo.resp.BaseResp;
import com.tuotuo.demo.resp.RespCodeEnum;
import com.tuotuo.demo.resp.TuoTuoBaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @Author xch
 * @Date 2021/5/24 16:38
 * @Version 1.0
 **/
public class TuoTuoManageFacade {
    private static Logger log = LoggerFactory.getLogger(TuoTuoManageFacade.class);
    /**
     * 调用请求
     * */
    public BaseResp execute(TuoTuoManageReq tuoTuoManageReq) {
        return ServiceTemplate.service(() -> {
            log.info("TuoTuoManageFacade.execute req:" + JSON.toJSONString(tuoTuoManageReq));
            BaseResp sendResp = send(tuoTuoManageReq);
            if (null != sendResp && RespCodeEnum.SUCC.getCode().equals(sendResp.getCode())) {
                String resp = String.valueOf(sendResp.getData());
                log.info("TuoTuoManageFacade.execute resp:" + resp);
                if (resp != null && StringUtils.isNotBlank(resp)) {
                    //工具类转换
                    TuoTuoBaseResponse tuoTuoBaseResponse = new Gson().fromJson(resp, TuoTuoBaseResponse.class);
                    return analysisResult(tuoTuoBaseResponse);
                } else {
                    return BaseResp.error("三方响应为空");
                }
            } else {
                return sendResp;
            }

        });
    }

    private BaseResp send(TuoTuoManageReq tuoTuoManageReq) {
        BaseResp<TuoTuoBaseResponse> baseResp = getTuoTuoBaseResponse(tuoTuoManageReq.getMerchantNo(), tuoTuoManageReq.getVersion(), tuoTuoManageReq.getParamObj());
        if (null == baseResp || !RespCodeEnum.SUCC.getCode().equals(baseResp.getCode())) {
            return baseResp;
        }
        TuoTuoBaseResponse tuoTuoBaseResponse = baseResp.getData();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(tuoTuoManageReq.getUrl());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Constant.HTTP_TIME_OUT).setConnectTimeout(Constant.HTTP_TIME_OUT).build();
        httpPost.setConfig(requestConfig);
        String respMessage = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("version", tuoTuoBaseResponse.getVersion()));
            nameValuePairs.add(new BasicNameValuePair("merchantNo", tuoTuoBaseResponse.getMerchantNo()));
            nameValuePairs.add(new BasicNameValuePair("encryptedData", tuoTuoBaseResponse.getEncryptedData()));
            nameValuePairs.add(new BasicNameValuePair("encryptedKey", tuoTuoBaseResponse.getEncryptedKey()));
            nameValuePairs.add(new BasicNameValuePair("signedData", tuoTuoBaseResponse.getSignedData()));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Constant.UTF8));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                respMessage = EntityUtils.toString(entity, Constant.UTF8);
            }
        } catch (Exception e) {
            respMessage = "";
        }
        return BaseResp.success(respMessage);
    }

    private BaseResp<TuoTuoBaseResponse> getTuoTuoBaseResponse(String merchantNo, String version, Object paramObj) {
        PublicKey tuoTuoPublicKey = SecurityKeyUtil.getPubKeyByString(Constant.PUBLIC_KEY, Constant.RSA_ALGORITHM);
        PrivateKey mctPrivateKey = SecurityKeyUtil.getPriKeyByString(Constant.PRIVATE_KEY, Constant.RSA_ALGORITHM);
        /*
         * 各接口业务请求数据--请参考接口文档
         * 需要作出json格式  待加密
         */
        String origData = new Gson().toJson(paramObj);
        //随机生成16位AESKey--用于加密业务数
        String AESKey = SecurityKeyUtil.createAesKey();
        //AESKey加密业务数据
        String encryptedData = SecurityEncryptUtil.encryptByAES(origData, AESKey, Constant.AES_ENCRYPT_ALGORITHM);
        //公钥加密AESKey
        String encryptedKey = SecurityEncryptUtil.encrypt(AESKey, tuoTuoPublicKey, Constant.RSA_ENCRYPT_ALGORITHM);
        String toSignData = "encryptedData=" + encryptedData + "&encryptedKey=" + encryptedKey + "&merchantNo=" + merchantNo + "&version=" + version;
        log.info("TuoTuoManageFacade.send.toSignData(签名参数):" + toSignData);
        log.info("TuoTuoManageFacade.send.AESKey:" + AESKey);
        //用商户私钥对拼接后的数据做签名
        String signedData = SecuritySignUtil.sign(toSignData, mctPrivateKey, Constant.SHA_SIGN_ALGORITHM);
        log.info("TuoTuoManageFacade.send.signedData(已签名):" + signedData);
        return BaseResp.success(buildTuoTuoBaseResponse(merchantNo, version, encryptedData, encryptedKey, signedData));
    }

    private TuoTuoBaseResponse buildTuoTuoBaseResponse(String merchantNo, String version, String encryptedData, String encryptedKey, String signedData) {
        TuoTuoBaseResponse tuoTuoBaseResponse = new TuoTuoBaseResponse();
        tuoTuoBaseResponse.setEncryptedData(encryptedData);
        tuoTuoBaseResponse.setEncryptedKey(encryptedKey);
        tuoTuoBaseResponse.setSignedData(signedData);
        tuoTuoBaseResponse.setVersion(version);
        tuoTuoBaseResponse.setMerchantNo(merchantNo);
        return tuoTuoBaseResponse;
    }
    /**
     * 数据加密
     * */
    public BaseResp encryData(String merchantNo, String version, Object paramObj) {
        return ServiceTemplate.service(() -> {
            BaseResp<TuoTuoBaseResponse> tuoTuoBaseResponse = getTuoTuoBaseResponse(merchantNo, version, paramObj);
            return BaseResp.success(JSON.toJSONString(tuoTuoBaseResponse.getData()));
        });
    }
    /**
     * 数据解密
     * */
    public BaseResp analysisResult(TuoTuoBaseResponse tuoTuoBaseResponse) {
        return ServiceTemplate.service(() -> {
            String version = tuoTuoBaseResponse.getVersion();
            String merchantNo = tuoTuoBaseResponse.getMerchantNo();
            String encryptedData = tuoTuoBaseResponse.getEncryptedData();
            String encryptedKey = tuoTuoBaseResponse.getEncryptedKey();
            String signedData = tuoTuoBaseResponse.getSignedData();
            PublicKey tuoTuoPublicKey = SecurityKeyUtil.getPubKeyByString(Constant.PUBLIC_KEY, Constant.RSA_ALGORITHM);
            PrivateKey mctPrivateKey = SecurityKeyUtil.getPriKeyByString(Constant.PRIVATE_KEY, Constant.RSA_ALGORITHM);
            String toSignData = "encryptedData=" + encryptedData + "&encryptedKey=" + encryptedKey + "&merchantNo=" + merchantNo + "&version=" + version;
            boolean result = SecuritySignUtil.checkSign(toSignData, signedData, tuoTuoPublicKey, Constant.SHA_SIGN_ALGORITHM);
            log.info("TuoTuoManageFacade.analysisResult验签结果:" + result);
            if (!result) {
                log.warn("******[TuoTuoManageFacade.analysisResult toSignData = " + toSignData + "]******");
                log.warn("******[TuoTuoManageFacade.analysisResult signedData = " + signedData + "]******");
                return BaseResp.error("验签失败");
            }
            String AESKey = "";
            String decryptData = "";
            try {
                AESKey = SecurityEncryptUtil.decrypt(encryptedKey, mctPrivateKey, Constant.RSA_ENCRYPT_ALGORITHM);
                decryptData = SecurityEncryptUtil.decryptByAES(encryptedData, AESKey, Constant.AES_ENCRYPT_ALGORITHM);
            } catch (Exception e) {
                log.warn("******[TuoTuoManageFacade.analysisResult 密文 = " + encryptedData + "]******");
                return BaseResp.error("解密失败");
            }
            log.info("TuoTuoManageFacade.analysisResult 接收明文信息:" + decryptData);
            return BaseResp.success(decryptData);
        });
    }


}
