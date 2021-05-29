package com.tuotuo.demo.util;

import com.google.gson.Gson;
import com.tuotuo.demo.resp.TuoTuoBaseResponse;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author xch
 * @Date 2021/5/24 17:16
 * @Version 1.0
 **/
public class MsgAnalysisTest {
    public static void main(String[] args) {
        //公私钥是另外一对
        PublicKey tuoTuoPublicKey = SecurityKeyUtil.getPubKeyByString(Constant.PUBLIC_KEY_T, Constant.RSA_ALGORITHM);
        PrivateKey mctPrivateKey = SecurityKeyUtil.getPriKeyByString(Constant.PRIVATE_KEY_T, Constant.RSA_ALGORITHM);
        //工具类转换
//        TuoTuoBaseResponse response = new Gson().fromJson(responseContent, TuoTuoBaseResponse.class);
        String version = "1.0";
        String merchantNo = "1224";
        String encryptedData = "SJdLppMe3K3RB1+fv0ziLA==";
        String encryptedKey = "FkypSNPvbq+WPE/onNYSHLchxLHg6MJzE07S2pcQ/hvfjCbBjuzfKUb6HAjPLZRsGf0n2pLpprDzJe3sv1K7P2NZPf/DJ0+fh1pgTAuUNO/kqTMm6Vj/O0saMmO3csgXUrTCtylqzO23xTOQ0V9wcb1dluwKoRoA57HI/VZYVHeS/1X2yOZA/v7ys9veMsCJ7LrFLR73rCWoYTpNNRmFAwS7bgPLwJuI9eK7KmAcQLgLG8vfS/LMjhUEAW2ys7SMDIBmHbQ0+xRy/99RfieQjUyvWf9jCy4DOJz+GEk1jYHAiSPArAxwlaNrBPKpfd0L17Ao+CpykAGPiEZvapwA+g==";
        String signedData = "MpwEVaGMdo8W3xpaWArtFDgNq5xuQ4posShk2M3JfvycfuAISSMdKX5CCyKCvU4xOi33yTT2rHWYzNeKwM9wFgoJZmUXaoLQdarW1609f/XVehV7WBjtTecXndIR+Y6Cba4/FOypr1VMnPCQ+uYGGzxoFTgeK65+pw7HiFjuacTDyQRpcj0eOqH+4lH9n20zyVUnpJjXc+gEHtngZVtTsSyiZEqP9D/UmW1IJiEcqHBLbaLm6RlEQt9JUhDToQ14JJ4l9daf5ipQKbIGcnj+Axnok9llWp5fqQeF+PIu++IJTK4tkHMJoDBNjni2QRnVKeZeljshnTTc0CmeVEqFqQ==";
        String toSignData = "encryptedData=" + encryptedData + "&encryptedKey=" + encryptedKey + "&merchantNo=" + merchantNo + "&version=" + version;
        boolean result = SecuritySignUtil.checkSign(toSignData, signedData, tuoTuoPublicKey, Constant.SHA_SIGN_ALGORITHM);
        System.out.println("analysisResult验签结果:" + result);
        if (!result) {
            System.out.println("******[analysisResult toSignData = " + toSignData + "]******");
            System.out.println("******[analysisResult signedData = " + signedData + "]******");
        }
        /**
         * 商户私钥解密AESKey
         */
        String AESKey = "";
        String decryptData = "";
        try {
            AESKey = SecurityEncryptUtil.decrypt(encryptedKey, mctPrivateKey, Constant.RSA_ENCRYPT_ALGORITHM);
            decryptData = SecurityEncryptUtil.decryptByAES(encryptedData, AESKey, Constant.AES_ENCRYPT_ALGORITHM);
        } catch (Exception e) {
            System.out.println("******[analysisResult encryptedData = " + encryptedData + "]******");
        }
        System.out.println("analysisResult 接收明文信息:" + decryptData);
    }
}
